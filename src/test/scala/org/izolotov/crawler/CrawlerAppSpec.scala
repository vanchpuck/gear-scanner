package org.izolotov.crawler

import org.scalatest.FlatSpec
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

class CrawlerAppSpec extends FlatSpec {

  it should "run the whole crawler cycle for category" in {
    val queue = new SQSCrawlQueue(SqsClient.builder.region(Region.US_EAST_2).build, "CrawlQueue")
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/ledovoe-snaryajenie/ledobury/", "category"))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/i/product/l/232494_1.jpg", "image"))
//    queue.add(CrawlQueueRecord("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/-31825/?id=334968", "product"))
    CrawlerApp.main(Array(
      "--user-agent", "GearBot/0.1",
      "--delay", "2000",
      "--timeout", "10000",
      "--sqs-max-miss-count", "3",
      "--aws-region", "us-east-2",
      "--sqs-queue-name", "CrawlQueue",
      "--sqs-wait-time", "2",
      "--crawl-table", "products",
      "--image-bucket-arn", "arn:aws:s3:us-east-2:848625190772:accesspoint/internet"
    ))
  }

}
