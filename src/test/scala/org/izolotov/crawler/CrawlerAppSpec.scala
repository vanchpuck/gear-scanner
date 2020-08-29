package org.izolotov.crawler

import org.scalatest.FlatSpec
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

class CrawlerAppSpec extends FlatSpec {

  ignore should "run the whole crawler cycle for category" in {
    implicit val Formats = org.json4s.DefaultFormats
    val queue = new SQSQueue[CrawlQueueRecord](SqsClient.builder.region(Region.US_EAST_2).build, "CrawlQueue")
//    queue.add(CrawlQueueRecord("https://tramontana.ru/catalog/termosy_1/", "category"))
//    queue.add(CrawlQueueRecord("sport-marafon.ru/catalog/alpinistskie-kaski/", "category"))
//    queue.add(CrawlQueueRecord("https://tramontana.ru/catalog/koshki_2/", "category"))
//    queue.add(CrawlQueueRecord("https://www.rei.com/product/130561/camp-usa-stalker-universal-crampons", "product"))
    queue.add(CrawlQueueRecord("https://tramontana.ru/product/petzl_koshki_sarken/", "product"))
//    queue.add(CrawlQueueRecord("https://tramontana.ru/brands/petzl/ledovoe_snaryazhenie/", "category"))
    "https://tramontana.ru/brands/petzl/ledovoe_snaryazhenie/"

    CrawlerApp.main(Array(
      "--user-agent", "GearBot/0.1",
      "--delay", "2000",
      "--timeout", "10000",
      "--sqs-max-miss-count", "3",
      "--aws-region", "us-east-2",
      "--sqs-classifier-queue-name", "ProductClassifierQueue",
      "--sqs-queue-name", "CrawlQueue",
      "--sqs-dl-queue-name", "DeadLetterCrawlQueue",
      "--sqs-wait-time", "2",
      "--crawl-table", "GearScannerTest",
      "--image-bucket-arn", "arn:aws:s3:us-east-2:848625190772:accesspoint/internet"
    ))
  }

}
