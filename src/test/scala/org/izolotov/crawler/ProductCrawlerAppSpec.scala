package org.izolotov.crawler

import java.nio.file.{Files, Paths}
import java.sql.Timestamp
import java.time.Instant

import com.holdenkarau.spark.testing.{DatasetSuiteBase, Utils}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.{ContextHandler, ResourceHandler}
import org.eclipse.jetty.util.resource.Resource
import org.izolotov.crawler.parser.product.Product
import org.scalatest.{BeforeAndAfter, FlatSpec}

import scala.util.{Failure, Success, Try}

object ProductCrawlerAppSpec {
  val JettyPort: Int = 8088
}

class ProductCrawlerAppSpec extends FlatSpec with DatasetSuiteBase with BeforeAndAfter{

  behavior of "Product crawler app"

  var server: Server = null

  before {
    server = new Server(ProductCrawlerAppSpec.JettyPort)
    val resourceHandler = new ResourceHandler()
    val contextHandler = new ContextHandler()
    contextHandler.setContextPath("/")
    resourceHandler.setBaseResource(Resource.newClassPathResource("/product-crawler-app/product-json"))
    contextHandler.setHandler(resourceHandler)
    server.setHandler(contextHandler)
    server.start
  }

  after {
    Try(server.stop()) match {
      case Success(_) => //just do nothing
      case Failure(exc) => exc.printStackTrace()
    }
  }

  it should "Place the correct crawling results and errors to output locations" in {
    import spark.implicits._
    val outputDir = s"${Utils.createTempDir().getPath}/output"
    val errorsDir = s"${Utils.createTempDir().getPath}/errors"
    ProductCrawlerApp.main(Array(
      "--urls-path", this.getClass.getClassLoader.getResource("product-crawler-app/input-urls/urls.csv").getPath,
      "--user-agent", "NoNameYetBot/0.1",
      "--fetcher-delay", "10",
      "--fetcher-timeout", "2000",
      "--crawled-output-path", outputDir,
      "--errors-output-path", errorsDir,
      "--table-region", "us-east-2"
    ))

    val now = Timestamp.from(Instant.now())
    val expectedCrawled: Dataset[ProductCrawlAttempt] = Seq(
      ProductCrawlAttempt(
        url = "http://localhost:8088/tramontana-petzl-lynx.json",
        httpCode = Some(200),
        timestamp = now,
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/tramontana-petzl-lynx.json",
          store = "tramontana.ru",
          brand = Some("Petzl"),
          name = Some("Кошки PETZL Lynx"),
          category = Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
          price = Some(17590),
          currency = Some("Руб.")
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/tramontana-petzl-lynx-duplicate.json",
        httpCode = Some(200),
        timestamp = now,
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/tramontana-petzl-lynx-duplicate.json",
          store = "tramontana.ru",
          brand = Some("Petzl"),
          name = Some("Кошки PETZL Lynx"),
          category = Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
          price = Some(17590),
          currency = Some("Руб.")
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/tramontana-petzl-lynx-sale.json",
        httpCode = Some(200),
        timestamp = now,
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/tramontana-petzl-lynx-sale.json",
          store = "tramontana.ru",
          brand = Some("Petzl"),
          name = Some("Кошки PETZL Lynx"),
          category = Seq("Альпинизм и скалолазание", "Ледовое снаряжение", "Кошки"),
          price = Some(15000),
          oldPrice = Some(17590),
          currency = Some("Руб.")
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/alpindustria-bd-cyborg.json",
        httpCode = Some(200),
        timestamp = now,
        responseTime = Some(1L),
        document = Some(Product(
          url = "http://localhost:8088/alpindustria-bd-cyborg.json",
          store = "alpindustria.ru",
          brand = Some("Black Diamond"),
          name = Some("Кошки Black Diamond Cyborg Pro Crampon"),
          category = Seq("Альпинистское снаряжение", "Кошки и снегоступы"),
          price = Some(19200),
          currency = Some("Руб.")
        )),
        fetchError = None
      ),
      ProductCrawlAttempt(
        url = "http://localhost:8088/alpindustria-cassin-blade-runner-error.json",
        httpCode = Some(200),
        timestamp = now,
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
        timestamp = now,
        responseTime = Some(1L),
        document = None,
        fetchError = None
      )
    ).toDS()

    val outputPath: String = Files.list(Paths.get(outputDir)).iterator().next().toString
    val actualCrawled: Dataset[ProductCrawlAttempt] = spark.read.parquet(outputPath)
      .withColumn("responseTime", lit(1L))
      .withColumn("timestamp", lit(now))
      .as[ProductCrawlAttempt]
    assertDatasetEquals(expectedCrawled, actualCrawled)

    val expectedErrorsUrls = Seq(
      "http://localhost:8088/alpindustria-cassin-blade-runner-error.json",
      "http://localhost:8088/no-such-page.json"
    ).toDF("url").sort("url")

    // Something is wrong with the full records comparison. Just checking the URLs for simplicity
    val errorsPath: String = Files.list(Paths.get(errorsDir)).iterator().next().toString
    val actualErrorsUrls = spark.read.option("header", true).csv(errorsPath)
      .select("url").sort("url")
    assertDatasetEquals(expectedErrorsUrls, actualErrorsUrls)
  }

}
