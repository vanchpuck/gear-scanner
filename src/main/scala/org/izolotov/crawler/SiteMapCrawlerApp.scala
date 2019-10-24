package org.izolotov.crawler

import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession

object SiteMapCrawlerApp {

  lazy implicit val Spark = SparkSession.builder
    .appName("SiteMap crawler app")
    .getOrCreate()

  // TODO CLI options
  def main(args: Array[String]): Unit = {
    import Spark.implicits._
    val urls = Spark.read
      .option("delimiter", "\t")
      .option("header", true)
      .csv(args(0)).as[UncrawledURL]
    SiteMapCrawler(args(2), args(3).toLong).crawl(urls)
      .flatMap(sm => sm.sitemaps.get)
      .filter($"isIndex" === false)
      .select("siteMapUrl")//.as("url")
      .withColumnRenamed("siteMapUrl", "url")
//        .show()
//      // TODO remove debug code
//      .filter($"url".rlike("^.+://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy.*$"))
////      .filter($"siteMapUrl".startsWith("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/"))
////      .filter($"siteMapUrl" =!= "https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/")
      .write
//      .option("delimiter", "\t")
      .option("header", true)
      .csv(args(1))
  }
}
