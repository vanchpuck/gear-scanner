package org.izolotov.crawler

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.spark.sql.{Dataset, SaveMode}
import org.eclipse.jetty.server.Server
import org.scalatest.FlatSpec

class CategoryCrawlerSpec extends FlatSpec with DataFrameSuiteBase {

  behavior of "Product Category crawler"

  var server: Server = null

  ignore should "return the correct list on product URLs" in {
    import spark.implicits._
    val urls: Dataset[UncrawledURL] = spark.read
      .option("header", "true")
      .csv(this.getClass.getResource("/category-crawler/input-urls/climbing-all.csv").getPath).as[UncrawledURL]

    val crawlConf = HostCrawlConfigurationReader.read(this.getClass.getResourceAsStream("/category-crawler/crawl-conf.yml"))

    val crawlerd = new CategoryCrawler(12, "Product rank bot V0.9", 7000L,
      10000L, hostConf = crawlConf.getHostsAsScala()).crawl(urls)
    crawlerd.withColumnRenamed("value", "url")
      .repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .option("header", true)
      .csv("/tmp/product_urls")
  }

}
