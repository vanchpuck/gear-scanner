package org.izolotov.crawler

import java.nio.file.{Files, Paths}
import java.util.concurrent.TimeUnit

import com.holdenkarau.spark.testing.{DatasetSuiteBase, Utils}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.{ContextHandler, ResourceHandler}
import org.eclipse.jetty.util.resource.Resource
import org.izolotov.crawler.parser.product.Product
import org.scalatest.{BeforeAndAfter, FlatSpec}
import pl.allegro.tech.embeddedelasticsearch.{EmbeddedElastic, PopularProperties}

import scala.util.{Failure, Success, Try}

object ProductCrawlerAppSpec {
  val JettyPort: Int = 8088
  val ElasticPort: Int = 8800
  val ElasticIndexName = "gear"
}

class ProductCrawlerAppSpec extends FlatSpec with DatasetSuiteBase with BeforeAndAfter{

  behavior of "Product crawler app"

  var server: Server = null
  var embeddedElastic: EmbeddedElastic = null


  before {
    server = new Server(ProductCrawlerAppSpec.JettyPort)
    val resourceHandler = new ResourceHandler()
    val contextHandler = new ContextHandler()
    contextHandler.setContextPath("/")
    resourceHandler.setBaseResource(Resource.newClassPathResource("/product-crawler-app/product-json"))
    contextHandler.setHandler(resourceHandler)
    server.setHandler(contextHandler)
    server.start

    embeddedElastic = EmbeddedElastic.builder()
      .withElasticVersion("5.6.8")
      .withSetting(PopularProperties.HTTP_PORT, ProductCrawlerAppSpec.ElasticPort)
      .withSetting(PopularProperties.CLUSTER_NAME, "gear")
      .withStartTimeout(30, TimeUnit.SECONDS)
      .withIndex(ProductCrawlerAppSpec.ElasticIndexName)
      .build()
    embeddedElastic.start()
  }

  after {
    Try(server.stop()) match {
      case Success(_) => //just do nothing
      case Failure(exc) => exc.printStackTrace()
    }
    Try(embeddedElastic.stop()) match {
      case Success(_) => //just do nothing
      case Failure(exc) => exc.printStackTrace()
    }
  }

  ignore should "successfully crawl specified web pages" in {
    import spark.implicits._
    val outputDir = s"${Utils.createTempDir().getPath}/output"
    ProductCrawlerApp.main(Array(
      "--urls-path", this.getClass.getClassLoader.getResource("product-crawler-app/input-urls/urls.csv").getPath,
      "--user-agent", "NoNameYetBot/0.1",
      "--fetcher-delay", "10",
      "--fetcher-timeout", "2000",
      "--crawled-output-path", outputDir,
      "--crawled-output-format", "parquet",
      "--elastic-nodes", s"localhost:${ProductCrawlerAppSpec.ElasticPort}"
    ))

    val expectedCrawled: Dataset[ProductCrawlAttempt] = Seq(
      ProductCrawlAttempt(
        url = "http://localhost:8088/tramontana-petzl-lynx.json",
        httpCode = Some(200),
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/tramontana-petzl-lynx.json",
          store = "tramontana.ru",
          brand = "Petzl",
          name = "Кошки PETZL Lynx",
          category = Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
          price = 17590,
          currency = "Руб."
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/tramontana-petzl-lynx-duplicate.json",
        httpCode = Some(200),
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/tramontana-petzl-lynx-duplicate.json",
          store = "tramontana.ru",
          brand = "Petzl",
          name = "Кошки PETZL Lynx",
          category = Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
          price = 17590,
          currency = "Руб."
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/tramontana-petzl-lynx-sale.json",
        httpCode = Some(200),
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/tramontana-petzl-lynx-sale.json",
          store = "tramontana.ru",
          brand = "Petzl",
          name = "Кошки PETZL Lynx",
          category = Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
          price = 15000,
          oldPrice = Some(17590),
          currency = "Руб."
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/alpindustria-bd-cyborg.json",
        httpCode = Some(200),
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/alpindustria-bd-cyborg.json",
          store = "alpindustria.ru",
          brand = "Black Diamond",
          name = "Кошки Black Diamond Cyborg Pro Crampon",
          category = Seq("Альпинистское снаряжение", "Кошки и снегоступы"),
          price = 19200,
          currency = "Руб."
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/alpindustria-cassin-blade-runner-error.json",
        httpCode = Some(200),
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/alpindustria-cassin-blade-runner-error.json",
          store = "localhost",
          parseError = Some("Parsing error")
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/no-such-page.json",
        httpCode = Some(404),
        responseTime = Some(1L),
        document = None,
        fetchError = None
      )
    ).toDS()

    val outputPath: String = Files.list(Paths.get(outputDir)).iterator().next().toString
    val actualCrawled: Dataset[ProductCrawlAttempt] = spark.read.parquet(outputPath)
      .withColumn("responseTime", lit(1L))
      .as[ProductCrawlAttempt]
    assertDatasetEquals(expectedCrawled, actualCrawled)

    val expectedIndex = Array(
      "{\"url\":\"http://localhost:8088/alpindustria-bd-cyborg.json\",\"store\":\"alpindustria.ru\",\"brand\":\"Black Diamond\",\"name\":\"Кошки Black Diamond Cyborg Pro Crampon\",\"category\":[\"Альпинистское снаряжение\",\"Кошки и снегоступы\"],\"price\":19200.0,\"currency\":\"Руб.\"}",
      "{\"url\":\"http://localhost:8088/tramontana-petzl-lynx-duplicate.json\",\"store\":\"tramontana.ru\",\"brand\":\"Petzl\",\"name\":\"Кошки PETZL Lynx\",\"category\":[\"Альпинизм и скалолазание\",\"Ледовое снаряжение\",\"Кошки\"],\"price\":17590.0,\"currency\":\"Руб.\"}",
      "{\"url\":\"http://localhost:8088/tramontana-petzl-lynx-sale.json\",\"store\":\"tramontana.ru\",\"brand\":\"Petzl\",\"name\":\"Кошки PETZL Lynx\",\"category\":[\"Альпинизм и скалолазание\",\"Ледовое снаряжение\",\"Кошки\"],\"price\":15000.0,\"oldPrice\":17590.0,\"currency\":\"Руб.\"}",
      "{\"url\":\"http://localhost:8088/tramontana-petzl-lynx.json\",\"store\":\"tramontana.ru\",\"brand\":\"Petzl\",\"name\":\"Кошки PETZL Lynx\",\"category\":[\"Альпинизм и скалолазание\",\"Ледовое снаряжение\",\"Кошки\"],\"price\":17590.0,\"currency\":\"Руб.\"}"
    )

    val actualIndex = embeddedElastic.fetchAllDocuments(ProductCrawlerAppSpec.ElasticIndexName).toArray

    assert(expectedIndex.deep == actualIndex.deep)
  }

}
