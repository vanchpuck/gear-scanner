package org.izolotov.crawler

import com.holdenkarau.spark.testing.DatasetSuiteBase
import org.scalatest.FlatSpec

class ImageCrawlerAppSpec extends FlatSpec with DatasetSuiteBase {

  behavior of "Image Crawler Application"

  ignore should "..." in {
    ImageCrawlerApp.main(Array(
      "--urls-path", this.getClass.getClassLoader.getResource("image-crawler-app/input-urls/urls.csv").getPath,
      "--user-agent", "NoNameYetBot/0.1",
      "--fetcher-delay", "10",
      "--fetcher-timeout", "20000",
      "--table-name", "ImageMetadata",
      "--table-region", "us-east-2",
      "--output-bucket", "org.gear-scanner.data",
      "--error-output-path", "/tmp/errors/"
    ))

  }

}
