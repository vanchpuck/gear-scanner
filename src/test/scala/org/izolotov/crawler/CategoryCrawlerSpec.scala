package org.izolotov.crawler

import java.net.URL
import java.util.concurrent.TimeUnit

import com.holdenkarau.spark.testing.DatasetSuiteBase
import org.apache.spark.sql.{Dataset, SaveMode}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.{ContextHandler, ResourceHandler}
import org.eclipse.jetty.util.resource.Resource
import org.scalatest.{BeforeAndAfter, FlatSpec}
import pl.allegro.tech.embeddedelasticsearch.{EmbeddedElastic, PopularProperties}

import scala.util.{Failure, Success, Try}

/**
  * For debugging purposes
  */
class CategoryCrawlerSpec extends FlatSpec with DatasetSuiteBase {

  behavior of "Product Category crawler"

  var server: Server = null

  it should "return the correct list on product URLs" in {
    import spark.implicits._
    val urls: Dataset[UncrawledURL] = Seq(
      UncrawledURL("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/koshki-snegostupy/"),
      UncrawledURL("https://tramontana.ru/catalog/koshki_2/"),
      UncrawledURL("https://sport-marafon.ru/catalog/alpinistskie-koshki/"),
      UncrawledURL("https://www.backcountry.com/mountaineering-crampons"),
      UncrawledURL("https://www.kant.ru/catalog/mountaineering/crampons/"),
      UncrawledURL("https://www.rei.com/c/crampons"),

      UncrawledURL("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/kaski/"),
      UncrawledURL("https://sport-marafon.ru/catalog/alpinistskie-kaski/"),
      UncrawledURL("https://tramontana.ru/catalog/kaski_sportivnye/"),
      UncrawledURL("https://www.backcountry.com/climbing-helmets"),
      UncrawledURL("https://www.kant.ru/catalog/mountaineering/kaska-dlya-alpinizma/"),
      UncrawledURL("https://www.rei.com/search?q=helmet+for+climbing"),

      UncrawledURL("https://tramontana.ru/catalog/ledobury/"),
      UncrawledURL("https://sport-marafon.ru/catalog/ledobury/"),
      UncrawledURL("https://www.backcountry.com/ice-climbing-screws"),
      UncrawledURL("https://www.rei.com/c/snow-and-ice-protection"),
      UncrawledURL("https://www.kant.ru/catalog/mountaineering/ice-snow-anchors/"),

      UncrawledURL("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/ledovoe-snaryajenie/"),
      UncrawledURL("https://tramontana.ru/catalog/ledoruby_1/"),
      UncrawledURL("https://sport-marafon.ru/catalog/ledoruby-ledovye-instrumenty/"),
      UncrawledURL("https://www.kant.ru/catalog/mountaineering/ice-axes/"),
      UncrawledURL("https://www.backcountry.com/ice-climbing-tools"),
      UncrawledURL("https://www.backcountry.com/ice-axes"),
      UncrawledURL("https://www.rei.com/c/ice-axes"),
      UncrawledURL("https://www.rei.com/c/ice-tools"),
      UncrawledURL("https://www.rei.com/c/ice-axe-and-ice-tool-accessories"),

      UncrawledURL("https://tramontana.ru/catalog/fonari_1/"),
      UncrawledURL("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/fonari/"),
      UncrawledURL("https://sport-marafon.ru/catalog/nalobnye-fonari/"),
      UncrawledURL("https://www.kant.ru/catalog/outdoor/lights/"),
      UncrawledURL("https://www.backcountry.com/headlamps"),
      UncrawledURL("https://www.rei.com/c/headlamps")
    ).toDS()

    val crawlerd = new CategoryCrawler(12, "Product rank bot V0.9", 4000L, 10000L).crawl(urls)
    crawlerd.withColumnRenamed("value", "url")
      .write
      .mode(SaveMode.Overwrite)
      .option("header", true)
      .csv("/tmp/product_urls")
  }

}
