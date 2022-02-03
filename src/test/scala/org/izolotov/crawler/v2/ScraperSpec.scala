//package org.izolotov.crawler.v2
//
//import java.net.URL
//import java.time.Clock
//
//import org.apache.http.impl.client.HttpClients
//import org.izolotov.crawler.timeoutscraper.HttpFetcher
//import org.scalatest.flatspec.AnyFlatSpec
//
//class ScraperSpec extends AnyFlatSpec{
//
//
//  it should ".." in {
//    implicit val client = HttpClients.createDefault()
//    implicit val clock = Clock.systemUTC()
//    implicit val parser1 = new HttpParser()
//    implicit val parser2 = new ArcteryxParser
//    implicit val fetcher1 = new HttpFetcher()
//    implicit val fetcher2 = new ChromeSeleniumFetcher("", "")
//
////    implicit val tFetcher = new TargetFetcher()
//    //    implicit val targetFetcher = new TargetFetcher()
//    val target = new URL("sdf").getHost match {
//      case _ => {
//        val webPage = new HttpWebPage("sdf")
//        val resp = new DefaultScraper[String, ScrapingAttempt[String]]().extract(webPage)
//        resp
//      }
////      case "q" => new SeleniumWebPage("sdf")
//    }
////    val resp = new DefaultScraper[String, ScrapingAttempt[String]]().extract(HttpWebPage("https://alpindustria.ru/catalog/alpinistskoe-snaryajenie/verevki-stropy-repshnury/-35009/?id=347256"))
////    val resp = new DefaultScraper[String, ScrapingAttempt[String]]().extract(target)
////    println(resp)
//  }
//
//}
