package org.izolotov.crawler.v3

import java.util.concurrent.TimeUnit

import org.izolotov.crawler.parser.origin.{OriginCategory, OriginProduct}
import org.izolotov.crawler.parser.product.Product
import org.openqa.selenium.{By, JavascriptExecutor, WebElement}
import org.openqa.selenium.devtools.DevTools
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import retry.Success

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.{Duration, FiniteDuration}

import ArcteryxParser._

object ArcteryxParser {
  val Brand = "arcteryx"
}

class ArcteryxParser extends SeleniumParser[String] {

  implicit val notNull = Success[String](_ != null)

  override def parseContent(url: String, driver: RemoteWebDriver, devTools: DevTools): String = {
    try {
      val products = driver.findElements(By.cssSelector("div.product-grid > div")).asScala.map{
        element =>
          driver.asInstanceOf[JavascriptExecutor].executeScript("arguments[0].scrollIntoView(true);", element)
          val imgUrlFuture = retry.Backoff(delay = FiniteDuration.apply(50, TimeUnit.MILLISECONDS)).apply(() => Future {
            element.findElement(By.cssSelector("div.qa--product-tile__main-image-container"))
              .findElement(By.cssSelector("img.primary"))
              .getAttribute("src")
          })
          val imgUrl = Await.result(imgUrlFuture, Duration.Inf)
          val name = element.findElement(By.cssSelector("div.product-tile-name")).getText
          OriginProduct(Brand, name, imgUrl)
      }
//      OriginCategory(None, products)
      "Ok"
    } finally {
      driver.close()
    }
  }
}
