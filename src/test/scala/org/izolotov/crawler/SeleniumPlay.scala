package org.izolotov.crawler

import java.io.{File, InputStreamReader}
import java.lang.Boolean
import java.net.{InetAddress, InetSocketAddress, URI, URL}
import java.util
import java.util.{Objects, Optional}
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.logging.Level

import com.browserup.bup.BrowserUpProxyServer
import com.browserup.bup.client.ClientUtil
import com.browserup.bup.filters.{RequestFilter, ResponseFilter}
import com.browserup.bup.mitm.TrustSource
import com.browserup.bup.proxy.CaptureType
import com.browserup.bup.util.{HttpMessageContents, HttpMessageInfo}
//import com.google.gson.Gson
import io.netty.handler.codec.http.{HttpRequest, HttpResponse}
//import javax.ws.rs.client.ClientBuilder
//import javax.ws.rs.core.MediaType
//import javax.ws.rs.core.Response.Status.Family
import org.apache.commons.io.IOUtils
import org.apache.http.impl.client.HttpClients
import org.json4s.DefaultFormats
import org.openqa.selenium.Proxy.ProxyType
import org.openqa.selenium.{Capabilities, Proxy}
import org.openqa.selenium.chrome.{ChromeDriver, ChromeDriverService, ChromeOptions}
import org.openqa.selenium.devtools.idealized.Domains
import org.openqa.selenium.devtools.v91.emulation.Emulation
import org.openqa.selenium.devtools.v91.network.Network
import org.openqa.selenium.devtools.v91.network.model.{RequestWillBeSent, ResponseReceived}
import org.openqa.selenium.devtools.v91.page.Page
import org.openqa.selenium.devtools.v91.performance.Performance
import org.openqa.selenium.devtools.v91.target.Target
import org.openqa.selenium.devtools.v91.target.model.{SessionID, TargetID}
import org.openqa.selenium.devtools.{CdpEndpointFinder, CdpVersionFinder, Connection, DevTools, Event}
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxOptions}
import org.openqa.selenium.logging.LogType
import org.openqa.selenium.remote.http.{ClientConfig, HttpClient}
import org.openqa.selenium.remote.{CapabilityType, DesiredCapabilities, RemoteWebDriver}
import org.scalatest.flatspec.AnyFlatSpec

import scala.reflect.ClassTag
import scala.util.parsing.json.JSON

class SeleniumPlay extends AnyFlatSpec {

  it should "..." in {
//    val url = "http://www.buran.ru/"
    val url = "http://ya.ru/"
//    val url = "https://arcteryx.com/ca/en/shop/womens/kole-down-jacket"

    import org.openqa.selenium.logging.LogType
    import org.openqa.selenium.logging.LoggingPreferences
    val logs = new LoggingPreferences
    logs.enable(LogType.BROWSER, Level.ALL)
    logs.enable(LogType.CLIENT, Level.ALL)
    logs.enable(LogType.DRIVER, Level.ALL)
    logs.enable(LogType.PERFORMANCE, Level.ALL)
    logs.enable(LogType.SERVER, Level.ALL)

    val proxy = new BrowserUpProxyServer()
    proxy.setTrustSource(TrustSource.defaultTrustSource())
    proxy.setTrustAllServers(true)
//    proxy.tls


//    val st = System.currentTimeMillis()
//      proxy.start()
////    proxy.start(8091, InetAddress.getByName("localhost"))
//      proxy.getPort
//      val et = System.currentTimeMillis() - st
//      println(et)
//    proxy.start(8091)
//    proxy.
//    println(proxy.isStarted)
//    println(proxy.getClientBindAddress)
//    println(proxy.getServerBindAddress)
//    proxy.stop()

//    val p = new Proxy();
//    p.setHttpProxy("localhost:8091");
//    p.setSslProxy("localhost:8091");

//    Thread.sleep(20000L)

//    val seleniumProxy = ClientUtil.createSeleniumProxy(proxy)
    val seleniumProxy = ClientUtil.createSeleniumProxy(InetSocketAddress.createUnresolved("localhost", proxy.getPort))

//    new Boolean(true)
//    Boolean.TRUE

//    val opts = new FirefoxOptions()
    val opts = new ChromeOptions()

//    opts.setCapability(CapabilityType.ACCEPT_SSL_CERTS, Boolean.TRUE)
//    opts.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, Boolean.TRUE)
//    opts.setCapability(CapabilityType.PROXY, seleniumProxy)

//    opts.setExperimentalOption("whitelisted-ips", "0.0.0.0")
    opts.addArguments("--remote-debugging-port=9222")
//    opts.addArguments("--remote-debugging-port")
    opts.addArguments("--headless")
//     opts.setExperimentalOption("debuggerAddress", "127.0.0.1:9222/devtools/page/CA7D4CCA6BB35AFF1CCB0C18888B47E3")
//    opts.addArguments( "--remote-debugging-address=0.0.0.0")
//      "headless", "remote-debugging-port=9222", "remote-debugging-address=0.0.0.0", "--no-sandbox", "no-sandbox")
//    opts.AddArgument("--remote-debugging-address=0.0.0.0") // this is needed as by default it is set to localhost which allows only local connections
//
//    opts.AddArgument("headless")
//    opts.setCapability("moz:debuggerAddress", "sdf:2345")
//    opts.add
//    opts.setAcceptInsecureCerts(true)
//    opts.setCapability(CapabilityType.LOGGING_PREFS, logs)
//    opts.addPreference("network.http.redirection-limit", 1)

    import scala.collection.JavaConverters._

//    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT, CaptureType.RESPONSE_HEADERS, CaptureType.REQUEST_HEADERS);

    // create a new HAR with the label "yahoo.com"
//    proxy.newHar("yahoo.com")
    proxy.clearBlacklist()

//    proxy.fil


    import org.openqa.selenium.remote.DesiredCapabilities
//    import org.openqa.selenium.remote.DesiredCapabilities
//    val capabilities = DesiredCapabilities.


    val driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), opts)


    import org.apache.http.client.methods.HttpGet
    import org.apache.http.impl.client.DefaultHttpClient
    import java.io.BufferedReader
    val httpClient = new DefaultHttpClient
    val getRequest = new HttpGet("http://localhost:9222/json")
    getRequest.addHeader("accept", "application/json")
    val response = httpClient.execute(getRequest)
    println(response.getStatusLine.getStatusCode)
    val br = new BufferedReader(new InputStreamReader(response.getEntity.getContent))

    import org.json4s._
    import org.json4s.jackson.JsonMethods._
    implicit val formats = DefaultFormats
    val respStr = IOUtils.toString(br)
    println(respStr)
    var resp = parse(respStr).asInstanceOf[JArray].arr.head.extractOpt[Map[String, String]]
    println(resp.get.get("webSocketDebuggerUrl"))
//    println(JSON.parseFull(IOUtils.toString(br)).get.asInstanceOf[Map[String, String]].get("webSocketDebuggerUrl"))
    httpClient.getConnectionManager.shutdown()
//    Thread.sleep(15000L)
    println(CdpEndpointFinder.getReportedUri("goog:chromeOptions", opts))
//    if (CdpEndpointFinder.getReportedUri("goog:chromeOptions", opts).isPresent) {
    val uri = new URI(resp.get.get("webSocketDebuggerUrl").get)
//      val uri = CdpEndpointFinder.getReportedUri("goog:chromeOptions", opts).get()

    //    println(HttpClient.Factory.createDefault())

    println(HttpClient.Factory.createDefault().createClient(ClientConfig.defaultConfig))

    println(ClientConfig.defaultConfig.baseUri(uri))
    val cdpInfo = new CdpVersionFinder().`match`(driver.getCapabilities.getBrowserVersion).get()
    println(cdpInfo)
    val connection = new Connection(HttpClient.Factory.createDefault().createClient(ClientConfig.defaultConfig.baseUri(uri)), uri.toString())
    //    new CdpVersionFinder().


    val devTools = new DevTools(cdpInfo.getDomains, connection)
    devTools.createSessionIfThereIsNotOne()
    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))
    // TODO requestIntercepted
    devTools.addListener(Network.responseReceived(), (entry: ResponseReceived) => {
      println(entry.getResponse.getUrl + " " + entry.getResponse.getStatus)
//      if (entry.getResponse.getUrl == url) {
//        println(entry.getResponse.getStatus)
//      }
      //        System.out.println("Request URI : " + entry.getRequest.getUrl + "\n" + " With method : " + entry.getRequest.getMethod + "\n")
      //        entry.getRequest.getMethod
    })

    driver.get(url)
    devTools.send(Network.disable());
    driver.quit()


//    val targetInfo = devTools.send(Target.getTargets).asScala.head
//    val targetId = targetInfo.getTargetId
//    val targetUrl = targetInfo.getUrl
//    println(targetId)
//    println(targetUrl)
//    devTools.send(Target.activateTarget(targetId))
//    val sessionId = devTools.send(Target.attachToTarget(targetId, Optional.empty())).toJson
////      new TargetID(targetId)
//    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))
//    devTools.send(Network.getCookies(Optional.empty())).iterator().asScala.foreach(c => println(c.toString))
//    driver.close()


//      val out = devTools.send(Target.sendMessageToTarget("Network.enable", Optional.of(new SessionID(sessionId)), Optional.of(targetId)))
//      println(out)
//    }
//      devTools.send(Target.sendMessageToTarget())

//      new DevTools(cdpInfo.getDomains, connection)
//      Network.enable()
//    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))

      driver
    //    new DevTools(cdpInfo.getDomains, conn)
//
//    this.connection.map((conn: Connection) => {
//      def foo(conn: Connection) = {
//        Objects.requireNonNull(cdpInfo)
//        new DevTools(cdpInfo.getDomains, conn)
//      }
//
//      foo(conn)
//    })
    //    new ChromeDriver().getDevTools
//    C
//    val driver1 = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), opts)

//    driver.manage().logs().get(LogType.BROWSER).getAll.asScala.foreach(l => println(l.getMessage))



//    driver.get("http://www.buran.ru/")

//    driver.manage.timeouts.pageLoadTimeout(10000, TimeUnit.MILLISECONDS)
//    driver1.get(url)


    import java.util.concurrent.TimeUnit
//    driver.manage.timeouts.pageLoadTimeout(60000, TimeUnit.MILLISECONDS)

//    println(driver.getPageSource)
//    println(driver.getCurrentUrl)
//    driver.manage().getCookies().asScala.foreach(l => println(l))
//    driver.manage().logs().getAvailableLogTypes.asScala.foreach(l => println(l))
//    driver.manage().logs().get(LogType.SERVER).getAll.asScala.foreach(l => println(l))

    import com.browserup.harreader.model.Har
//    val har = proxy.getHar

//    har.writeTo(new File("test.har"))
//    proxy.addHeader("sdf", "sdf")
//    proxy.getAllHeaders.asScala.foreach((k) => println(k._1 + " " + k._2))

//    har.getLog.getEntries.asScala.foreach(l => println(l))
//    proxy.get
//    driver.findElement().
//    println(driver.getPageSource.toLowerCase.contains("ZETA".toLowerCase))
  }



}
