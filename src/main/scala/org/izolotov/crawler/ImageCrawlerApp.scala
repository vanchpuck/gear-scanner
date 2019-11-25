package org.izolotov.crawler

import java.net.URL
import java.sql.Timestamp
import java.time.{Clock, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import org.apache.commons.cli.{BasicParser, Options}
import org.apache.derby.iapi.services.io.ArrayInputStream
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.date_format
import org.izolotov.crawler.ProductCrawlerApp.{ErrorsOutputPathArgKey, OutDirFormatter}
import org.izolotov.crawler.parser.BinaryDataParser

import scala.reflect.runtime.universe._
import scala.util.{Failure, Success}

object ImageCrawlerApp extends Logging {

  private lazy implicit val Spark = SparkSession.builder
    .appName("Image crawler app")
    .getOrCreate()

  val CrawConfFilePath = "image-crawler-app/crawl-conf.yml"

  object CliArg extends Enumeration {
    val urlsPath = "urls-path"
    val userAgent = "user-agent"
    val fetchDelay = "fetcher-delay"
    val fetchTimeout = "fetcher-timeout"
    val partitionsNumber = "partitions-number"
    val outputBucket = "output-bucket"
    val threadsNumber = "threads-number"
    val tableName = "table-name"
    val tableRegion = "table-region"
    val errorOutputPath = "error-output-path"
  }
  import CliArg._

  val DynamoDBTimestampFormat = "yyyy-MM-dd HH:mm:ss"
  private implicit val UtcClock = Clock.systemUTC()
  val OutDirFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

  case class ImageStoreAttempt(url: String,
                               timestamp: Timestamp,
                               httpCode: Option[Int],
                               responseTime: Option[Long],
                               fetchError: Option[String],
                               key: Option[String],
                               s3Error: Option[String])

  def main(args: Array[String]): Unit = {
    import Spark.implicits._
    import com.audienceproject.spark.dynamodb.implicits._
    val options = new Options
    options.addOption("u", urlsPath, true, "Path to file containing URL for crawling")
    options.addOption("a", userAgent, true, "User agent string")
    options.addOption("d", fetchDelay, true, "Fetcher delay in milliseconds")
    options.addOption("t", fetchTimeout, true, "Fetcher timeout in milliseconds")
    options.addOption("p", partitionsNumber, true, "Number of partitions the URLs will be partitioned on before being crawled")
    options.addOption("o", outputBucket, true, "Output path")
    options.addOption("T", threadsNumber, true, "Number of threads in the crawl queue")
    options.addOption("n", tableName, true, "DynamoDB table to store image metadata")
    options.addOption("r", tableRegion, true, "DynamoDB table region")
    options.addOption("e", errorOutputPath, true, "Error records output path")
    val parser = new BasicParser
    val cmd = parser.parse(options, args)

    val parserClass = classOf[BinaryDataParser]

    // TODO make it possible to pass conf file as a parameter
    logInfo(s"Reading the host crawl settins")
    val crawlConf: HostCrawlConfiguration = HostCrawlConfigurationReader.read(this.getClass.getClassLoader.getResourceAsStream(CrawConfFilePath))
    val outPath = cmd.getOptionValue(outputBucket)
    val currTimestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
    val errorsOutPath = s"${cmd.getOptionValue(errorOutputPath)}/${currTimestamp.format(OutDirFormatter)}"

    logInfo(s"Reading URLs")
    val urls = Spark.read
      .option("delimiter", "\t")
      .option("header", true)
      .csv(cmd.getOptionValue(urlsPath))
      .distinct()
      .as[UncrawledURL]

    logInfo(s"Starting the crawling")
    val result = ProductCrawler.crawl(
      cmd.getOptionValue(userAgent),
      urls,
      Option(cmd.getOptionValue(partitionsNumber)).getOrElse("1").toInt,
      Option(cmd.getOptionValue(fetchTimeout)).map(_.toLong).getOrElse(Long.MaxValue),
      parserClass,
      typeTag[CrawlAttempt[Array[Byte]]],
      cmd.getOptionValue(fetchDelay).toLong,
      Option(cmd.getOptionValue(threadsNumber)).map(_.toInt).getOrElse(Runtime.getRuntime.availableProcessors()),
      hostConf = crawlConf.getHostsAsScala
    ).map {
      attempt =>
        val s3StoreTry: Option[util.Try[String]] = attempt.document.map {
          data =>
            util.Try {
              val url: URL = new URL(attempt.url)
              val key = url.getHost + url.getFile
              val metadata = new ObjectMetadata()
              metadata.setContentLength(data.length)
              val inStream = new ArrayInputStream(data)
              val xferMgr = TransferManagerBuilder.standard.build
              val xfer = xferMgr.upload(outPath, key, inStream, metadata)
              val res = xfer.waitForUploadResult()
              res.getBucketName + "/" + res.getKey
            }
        }
        val (key, err) = s3StoreTry.map {
          s3Try =>
            s3Try match {
              case Success(value) => (Some(value), None)
              case Failure(exception) => (None, Some(exception.toString))
            }
        }.getOrElse(None, None)
        ImageStoreAttempt(attempt.url, attempt.timestamp, attempt.httpCode, attempt.responseTime, attempt.fetchError, key, err)
    }.persist()

    result.filter(record => record.key.isEmpty)
        .write
        .option("header", "true")
        .csv(errorsOutPath)

    Option(cmd.getOptionValue(tableName)).foreach {
      tableName: String =>
        logInfo(s"Persisting the crawled images metadata into DynamoDB table...")
        result.toDF()
          .withColumn("timestamp", date_format($"timestamp", DynamoDBTimestampFormat))
          .write
          .option("region", cmd.getOptionValue(tableRegion))
          .dynamodb(tableName)

    }
  }

}
