package org.izolotov.crawler.v4

import java.net.URL

import org.apache.http.HttpStatus
import org.izolotov.crawler.v2.{HttpResponse, Parsable, SeleniumFetchingAttempt}
import org.openqa.selenium.devtools.DevTools
import org.openqa.selenium.remote.RemoteWebDriver

abstract class SeleniumParser[T] extends Parser[SeleniumFetchingAttempt, HttpResponse[T]] {
  override def parse(url: URL, attempt: SeleniumFetchingAttempt): HttpResponse[T] = {
    val httpCode = attempt.responseData.getStatus
    val document: Option[T] = if (httpCode != HttpStatus.SC_OK) None else {
      val document = parseContent(url.toString, attempt.driver, attempt.devTools)
      Some(document)
    }
    HttpResponse(httpCode, document)
  }

  def parseContent(url: String, driver: RemoteWebDriver, devTools: DevTools): T
}
