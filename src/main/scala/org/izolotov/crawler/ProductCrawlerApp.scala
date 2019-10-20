package org.izolotov.crawler

import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.{Clock, LocalDateTime, ZoneId}

import org.apache.commons.cli.{BasicParser, Options}
import org.apache.commons.httpclient.HttpStatus
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


object ProductCrawlerApp extends Logging {

  val UrlsPathArgKey = "urls-path"
  val UserAgentArgKey = "user-agent"
  val FetcherDelayArgKey = "fetcher-delay"
  val FetcherTimeoutArgKey = "fetcher-timeout"
  val PartitionsNumberArgKey = "partitions-number"
  val ErrorsOutputPathArgKey = "errors-output-path"
  val CrawledOutputPathArgKey = "crawled-output-path"
  val ElasticNodesArgKey = "elastic-nodes"
  val ElasticAuthUserArgKey = "elastic-user"
  val ElasticAuthPasswordArgKey = "elastic-password"
  val ThreadsNumberArgKey = "threads-number"
  val TableRegionArgKey = "table-region"
  val TableNameArgKey = "table-name"

  val CrawlConfFileName = "crawl-conf.yml"

  val DynamoDBTimestampFormat = "yyyy-MM-dd HH:mm:ss"
  val OutDirFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

  private implicit val UtcClock = Clock.systemUTC()

  private lazy implicit val Spark = SparkSession.builder
    .appName("SiteMap crawler app")
    .getOrCreate()

  case class ErrorRecord(url: String,
                         timestamp: Timestamp,
                         httpCode: Option[Int],
                         responseTime: Option[Long],
                         fetchError: Option[String],
                         parseError: Option[String])

  // TODO handle the case when several urls point to single item
  def main(args: Array[String]): Unit = {
    import Spark.implicits._
    import com.audienceproject.spark.dynamodb.implicits._
    val options = new Options
    options.addOption("u", UrlsPathArgKey, true, "Path to file containing URL for crawling")
    options.addOption("a", UserAgentArgKey, true, "User agent string")
    options.addOption("d", FetcherDelayArgKey, true, "Fetcher delay in milliseconds")
    options.addOption("t", FetcherTimeoutArgKey, true, "Fetcher timeout in milliseconds")
    options.addOption("p", PartitionsNumberArgKey, true, "Number of partitions the URLs will be partitioned on before being crawled")
    options.addOption("e", ErrorsOutputPathArgKey, true, "Crawling errors output path")
    options.addOption("c", CrawledOutputPathArgKey, true, "Debug output path")
    options.addOption("T", ThreadsNumberArgKey, true, "Number of threads in the crawl queue")
    options.addOption("r", TableRegionArgKey, true, "DynamoDB Table region")
    options.addOption("n", TableNameArgKey, true, "DynamoDB Table name")
    val parser = new BasicParser
    val cmd = parser.parse(options, args)

    val currTimestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
    val crawledOutPath = s"${cmd.getOptionValue(CrawledOutputPathArgKey)}/${currTimestamp.format(OutDirFormatter)}"
    val errorsOutPath = s"${cmd.getOptionValue(ErrorsOutputPathArgKey)}/${currTimestamp.format(OutDirFormatter)}"

    // TODO make it possible to pass conf file as a parameter
    logInfo(s"Reading the host crawl settins")
    val crawlConf: HostCrawlConfiguration = HostCrawlConfigurationReader.read(this.getClass().getClassLoader().getResourceAsStream(CrawlConfFileName))

    logInfo(s"Reading URLs")
    val urls = Spark.read
      .option("delimiter", "\t")
      .option("header", true)
      .csv(cmd.getOptionValue(UrlsPathArgKey)).as[UncrawledURL]

    logInfo(s"Starting the crawling")
    val crawled  = new ProductCrawler(
      Option(cmd.getOptionValue(PartitionsNumberArgKey)).getOrElse("1").toInt,
      cmd.getOptionValue(UserAgentArgKey),
      Option(cmd.getOptionValue(FetcherTimeoutArgKey)).map(_.toLong).getOrElse(Long.MaxValue),
      cmd.getOptionValue(FetcherDelayArgKey).toLong,
      Option(cmd.getOptionValue(ThreadsNumberArgKey)).map(_.toInt).getOrElse(Runtime.getRuntime.availableProcessors()),
      hostConf = crawlConf.getHostsAsScala
    )
      .crawl(urls)
      .persist()

    logInfo(s"Persisting the crawling results into $crawledOutPath")
    crawled.write
      .parquet(crawledOutPath)

    logInfo(s"Persisting the crawling errors info $errorsOutPath")
    crawled
      .filter(rec =>
        rec.httpCode != Some(HttpStatus.SC_OK)
          || rec.fetchError.isDefined
          || rec.document.filter(doc => doc.parseError.isDefined).isDefined
      )
      .map(rec => ErrorRecord(rec.url, rec.timestamp, rec.httpCode, rec.responseTime, rec.fetchError, rec.document.map(doc => doc.parseError).flatten))
      .write
      .option("header", true)
      .csv(errorsOutPath)

    // We don't write to DynamoDB during unit testing
    Option(cmd.getOptionValue(TableNameArgKey)).foreach {
      tableName =>
        logInfo(s"Persisting the crawled products into DynamoDB table...")
        crawled.toDF()
          .withColumn("timestamp", date_format($"timestamp", DynamoDBTimestampFormat))
          .write
          .option("region", cmd.getOptionValue(TableRegionArgKey))
          .dynamodb(tableName)
    }
  }

}
