//package org.izolotov.crawler.timeoutscraper
//
//import java.net.URL
//import java.time.{Clock, Instant, ZoneId}
//
//import org.apache.http.impl.client.HttpClients
//import org.eclipse.jetty.server.Server
//import org.eclipse.jetty.server.handler.{ContextHandler, ResourceHandler}
//import org.izolotov.crawler.timeoutscraper.FixedDelayModeratorSpec.Port
//import org.scalatest.{BeforeAndAfter, FlatSpec}
//
//class SplashFetcherSpec extends FlatSpec with BeforeAndAfter {
//
//  var server = new Server(8083)
//  implicit val httpClient = HttpClients.createDefault()
//  implicit val clock: Clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.of("UTC"))
//
//  before {
//    val path = getClass.getClassLoader.getResource("javascript/helloworld.html").getFile
//    println(path)
//    val ctx = new ContextHandler("/")
//    /* the server uri path */
//    val resHandler = new ResourceHandler()
//    resHandler.setResourceBase(path)
//    ctx.setHandler(resHandler)
//    server.setHandler(ctx)
//    server.start()
//  }
//
//  after {
//    server.stop()
//  }
////  val path = getClass.getClassLoader.getResource("test_pages/simple_page.html").getFile
////  println(path)
////  val ctx = new ContextHandler("/files")
////  /* the server uri path */
////  val resHandler = new ResourceHandler()
////  resHandler.setResourceBase(path)
////  ctx.setHandler(resHandler)
////  server.setHandler(ctx)
////  server.start()
//
//  it should "..." in {
//    println(server.getURI)
//    val fetcher = new SplashFetcher(new URL("http://0.0.0.0:8050"))
////    val attempt = fetcher.fetch("https://www.blackdiamondequipment.com/en_US/product/raven-ultra-ice-axe/")
//    val attempt = fetcher.fetch("http://localhost:8083/")
//    println(attempt.response.getStatusLine.getStatusCode)
//    import org.apache.http.util.EntityUtils
//    val entity = attempt.response.getEntity
//    val responseString = EntityUtils.toString(entity, "UTF-8")
//    System.out.println(responseString)
////    Thread.sleep(10000L)
//  }
//
//}
