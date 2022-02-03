package org.izolotov.crawler.v2

import java.net.URL
import java.util.concurrent.Executors

import org.izolotov.crawler.v2
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.{CapabilityType, RemoteWebDriver}
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object SeleniumScraperSpec {
  class Scraper() {
    val opts = new FirefoxOptions()
    opts.setCapability(CapabilityType.ACCEPT_SSL_CERTS, java.lang.Boolean.TRUE)
    opts.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, java.lang.Boolean.TRUE)
    val driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), opts)

    def getContent(url: String): String = {
      driver.get(url)
      val source = driver.getPageSource
      driver.close()
      source
    }
  }
}

class SeleniumScraperSpec extends AnyFlatSpec {

  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(12))

  it should "..." in {
    val f1 = Future{
      val st = System.currentTimeMillis()
      new v2.SeleniumScraperSpec.Scraper().getContent("https://commons.apache.org/proper/commons-pool/apidocs/org/apache/commons/pool2/PooledObjectFactory.html")
      println("###############")
      println(System.currentTimeMillis() - st)
    }
    val f2 = Future{
      val st = System.currentTimeMillis()
      new v2.SeleniumScraperSpec.Scraper().getContent("https://commons.apache.org/proper/commons-pool/")
      println("!!!!!!!!!!!!!!")
      println(System.currentTimeMillis() - st)
    }
    val f3 = Future{
      val st = System.currentTimeMillis()
      new v2.SeleniumScraperSpec.Scraper().getContent("https://commons.apache.org/proper/commons-pool/apidocs/org/apache/commons/pool2/impl/SoftReferenceObjectPool.html#addObject--")
      println("%%%%%%%%%%%%%%%%")
      println(System.currentTimeMillis() - st)
    }
    Await.result(Future.sequence(Seq(f1, f2, f3)), Duration.Inf)
    Thread.sleep(25000L)
  }

}
