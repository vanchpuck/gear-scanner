package org.izolotov.crawler.v3

import java.net.{URI, URL}
import java.util.Optional
import java.util.function.Consumer

import org.apache.commons.io.IOUtils
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.openqa.selenium.chrome.ChromeOptions
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.openqa.selenium.devtools.v91.network.Network
import org.openqa.selenium.devtools.v91.network.model.{Response, ResponseReceived}
import org.openqa.selenium.devtools.{CdpVersionFinder, Connection, DevTools}
import org.openqa.selenium.remote.http.ClientConfig
import org.openqa.selenium.remote.RemoteWebDriver
import ChromeSeleniumFetcher._

object ChromeSeleniumFetcher {

  var WebSocketDebuggerUrl = "webSocketDebuggerUrl"

  class ResponseListener(url: String) extends Consumer[ResponseReceived]{

    private var _response: Response = null

    def response(): Response ={
      synchronized {
        if (_response == null) {
          throw new IllegalStateException("Response hasn't been received yet")
        }
        _response
      }
    }

    override def accept(event: ResponseReceived): Unit = {
      synchronized {
        if (_response == null) {
          _response = event.getResponse
        }
      }
    }
  }

}

class ChromeSeleniumFetcher(driverUrl: String, debuggerUrl: String)(implicit httpClient: CloseableHttpClient) extends Fetcher[SeleniumFetchingAttempt] {

  override def fetch(url: String): SeleniumFetchingAttempt = {
    val debuggerPort = new URL(debuggerUrl).getPort
    val driver = new RemoteWebDriver(new URL(driverUrl), initCapabilities(debuggerPort))
    val responseListener = new ResponseListener(url)
    val devTools = initDevTools(driver, httpClient, debuggerUrl)
    devTools.createSessionIfThereIsNotOne()
    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))
    devTools.addListener(Network.responseReceived(), responseListener)
    driver.get(url)
    SeleniumFetchingAttempt(driver, devTools, responseListener.response)
  }

  private def initCapabilities(debuggerPort: Int): ChromeOptions = {
    val opts = new ChromeOptions()
    opts.addArguments(s"--remote-debugging-port=${debuggerPort}")
    opts.addArguments("--headless")
    opts
  }

  private def initDevTools(
                            driver: RemoteWebDriver,
                            httpClient: CloseableHttpClient,
                            debuggerUrl: String): DevTools = {
    val wsDebuggerUrl = getWebSocketDebuggerUrl(httpClient, debuggerUrl)
    val cdpInfo = new CdpVersionFinder().`match`(driver.getCapabilities.getBrowserVersion).get()
    val connection = new Connection(org.openqa.selenium.remote.http.HttpClient.Factory.createDefault()
      .createClient(ClientConfig.defaultConfig.baseUri(new URI(wsDebuggerUrl))), wsDebuggerUrl)
    new DevTools(cdpInfo.getDomains, connection)
  }

  private def getWebSocketDebuggerUrl(httpClient: CloseableHttpClient, debuggerUrl: String): String = {
    implicit val formats = DefaultFormats
    val getRequest = new HttpGet(debuggerUrl)
    getRequest.addHeader("accept", "application/json")
    val response = httpClient.execute(getRequest)
    try {
      if (response.getStatusLine.getStatusCode != HttpStatus.SC_OK) {
        throw new IllegalStateException("Can't get the DevTools details. Make sure that Chrome driver launched in debugging mode")
      }
      val rawStr = IOUtils.toString(response.getEntity.getContent)
      val debuggerUrl = parse(rawStr).asInstanceOf[JArray].arr.head.extractOpt[Map[String, String]]
        .map(map => map(WebSocketDebuggerUrl)).get
      debuggerUrl
    } finally {
      response.close()
    }
  }
}

