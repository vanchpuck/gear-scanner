package org.izolotov.crawler

import java.net.{MalformedURLException, URL}
import java.time.Clock
import java.util.concurrent.Executors

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.typesafe.scalalogging.Logger
import org.izolotov.crawler.parser._
import org.izolotov.crawler.processor.{CategoryProcessor, ImageProcessor, Processor, ProductProcessor}
import org.rogach.scallop.ScallopConf
import org.scanamo.DynamoFormat
import org.scanamo._
import org.scanamo.syntax._
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object CrawlerApp {

  case class HostConf[A](conf: CrawlConf, parser: Parser[A], processor: Processor[A])

  class RecordConfHelper(userAgent: String,
                                    delay: Long,
                                    timeout: Long,
                                    productProcessor: ProductProcessor,
                                    categoryProcessor: CategoryProcessor,
                                    imageProcessor: ImageProcessor) {

    private def crawlerConf[A](parser: Parser[A], delay: Long = this.delay, cookies: Option[Map[String, _]] = None): (CrawlConf, Parser[A]) = {
      (CrawlConf(userAgent, delay, timeout, cookies), parser)
    }

    def getHostConf(record: CrawlQueueRecord): HostConf[_] = {
      record.urlType match {
        case "product" => {
          val conf = new URL(record.url).getHost match {
            case "alpindustria.ru" => crawlerConf(new product.AlpindustriaParser())
            case "tramontana.ru" => crawlerConf(new product.TramontanaParser())
            case "www.trekkinn.com" => crawlerConf(new product.TrekkinnParser(),cookies = Some(Map("id_pais" -> 164)))
            case "www.densurka.ru" => crawlerConf(new product.DenSurkaParser(), delay = 10000L)
            case "www.kant.ru" => crawlerConf(new product.KantParser())
            case "sport-marafon.ru" => crawlerConf(new product.SportMarafonParser())
            case "www.planeta-sport.ru" => crawlerConf(new product.PlanetaSportParser())
            case "www.equip.ru" => crawlerConf(new product.EquipParser())
            case "www.backcountry.com" => crawlerConf(new product.BackcountryParser())
            case "www.rei.com" => crawlerConf(new product.ReiParser())
          }
          HostConf(conf._1, conf._2, productProcessor)
        }
        case "category" => {
          val conf = new URL(record.url).getHost match {
            case "alpindustria.ru" => crawlerConf(category.AlpindustriaParser)
            case "tramontana.ru" => crawlerConf(category.TramontanaParser)
            case "www.kant.ru" => crawlerConf(category.KantParser)
            case "www.equip.ru" => crawlerConf(category.EquipParser)
            case "www.backcountry.com" => crawlerConf(category.BackcountryParser)
            case "www.rei.com" => crawlerConf(category.ReiCoopParser)
            case "www.trekkinn.com" => crawlerConf(category.TrekkinnParser)
            case "www.densurka.ru" => crawlerConf(category.DenSurkaParser)
            case "sport-marafon.ru" => crawlerConf(category.SportMarafonParser)
          }
          HostConf(conf._1, conf._2, categoryProcessor)
        }
        case "image" => {
          val conf = crawlerConf(new BinaryDataParser())
          HostConf(conf._1, conf._2, imageProcessor)
        }
      }
    }
  }

  class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
    val userAgent = opt[String](required = true)
    val delay = opt[Long](default = Some(0L))
    val timeout = opt[Long](default = Some(Long.MaxValue))
    val awsRegion = opt[String](required = true)
    val sqsClassifierQueueName = opt[String](required = true)
    val sqsQueueName = opt[String](required = true)
    val sqsDlQueueName = opt[String](required = false)
    val sqsWaitTime = opt[Int](default = Some(0))
    val sqsMaxMissCount = opt[Int](default = Some(3))
    val crawlTable = opt[String](required = true)
    val imageBucketArn = opt[String](required = true)
    val crawlerThreads = opt[Int](default = Some(Runtime.getRuntime.availableProcessors()))
    verify()
  }

  private val Log = Logger[CrawlerApp.type]

  implicit val clock = Clock.systemUTC()

  def crawl[A](records: Iterable[CrawlQueueRecord],
               conf: (CrawlQueueRecord) => HostConf[_])
              (implicit crawlingContext: ExecutionContext, processingContext: ExecutionContext): Unit = {
    implicit val awaitContext = ExecutionContext.global

    val futures = records.map{
      record => {
        Log.info(s"Processing record: $record")
        val hostConf = conf(record)
        val parser = hostConf.parser.asInstanceOf[Parser[A]]
        val processor = hostConf.processor.asInstanceOf[Processor[A]]
        val crawlConf = hostConf.conf
        Crawler.crawl(record.url)(clock, crawlingContext, crawlConf, parser)
          .map(attempt => processor.process(attempt))(processingContext)
      }
    }
    Await.result(Future.sequence(futures), Duration.Inf)
  }

  def processQueue(queue: SQSQueue[CrawlQueueRecord],
                   conf: (CrawlQueueRecord) => HostConf[_],
                   maxEmptyRespCount: Int = 3,
                   sqsWaitTime: Int = 0)
                  (implicit crawlingContext: ExecutionContext, processingContext: ExecutionContext): Unit = {
    var count = 0;
    while (count < maxEmptyRespCount) {
      val records: Iterable[CrawlQueueRecord] = queue.pull()
      if (records.isEmpty) {
        count += 1
      } else {
        count = 0
        try {
          crawl(records, conf)(crawlingContext, processingContext)
        } catch {
          case e: Exception => Log.warn(s"Error during the record batch processing: ${e.toString}")
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    import org.scanamo.generic.auto._
    implicit val urlStringFormat = DynamoFormat.coercedXmap[URL, String, MalformedURLException](new URL(_))(_.toString)
    implicit val iterableListFormat = DynamoFormat.xmap[Iterable[URL], List[URL]](
      list => Right(list))(_.toList)
    implicit val Formats = org.json4s.DefaultFormats
    val conf = new Conf(args)
    val sqsClient = SqsClient.builder.region(Region.of(conf.awsRegion())).build
    val classifierQueue = new SQSQueue[CrawlAttempt[product.Product]](sqsClient, conf.sqsClassifierQueueName())
    val crawlQueue = new SQSQueue[CrawlQueueRecord](sqsClient, conf.sqsQueueName())
    val deadLetterQueue = new SQSQueue[CrawlAttempt[_]](sqsClient, conf.sqsDlQueueName())
    val dynamo = new DynamoDBHelper(conf.crawlTable(), conf.awsRegion())
    val imageStore = new ImageStore(conf.imageBucketArn())
    val crawlConfHelper = new RecordConfHelper(
      conf.userAgent(),
      conf.delay(),
      conf.timeout(),
      new ProductProcessor(crawlQueue.add, classifierQueue.add, deadLetterQueue.add),
      new CategoryProcessor(crawlQueue.add, dynamo.save, deadLetterQueue.add),
      new ImageProcessor(imageStore.upload, dynamo.save, deadLetterQueue.add)
    )
    val crawlingContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(conf.crawlerThreads(), new ThreadFactoryBuilder().setDaemon(true).build))
    val processingContext = ExecutionContext.global
    processQueue(crawlQueue, crawlConfHelper.getHostConf, conf.sqsMaxMissCount())(crawlingContext, processingContext)
  }

}
