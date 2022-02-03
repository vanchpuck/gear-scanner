package org.izolotov.crawler.v3

import CrawlerAppSpec._
import org.scalatest.flatspec.AnyFlatSpec

object CrawlerAppSpec {
  trait TestWebPage extends WebPage
  case class HttpWebPage(url: String) extends TestWebPage
  case class SeleniumWebPage(url: String) extends TestWebPage

  class Aa[A] {

  }
}

class CrawlerAppSpec extends AnyFlatSpec {



  it should "..." in {
    val a: TestWebPage = "sdfsd" match {
      case "" => HttpWebPage("sd")
      case _ => SeleniumWebPage("s")
    }
    import scala.reflect.runtime.universe.{typeOf, TypeTag}
    println(typeOf[Aa[Int]].toString)

//    val className = HttpWebPage.getClass.getCanonicalName
//    println(new Aa[String]().getClass.getClasses)
  }

}
