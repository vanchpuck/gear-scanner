package org.izolotov.crawler.v2

sealed trait Target {

  def url: String
}
case class HttpWebPage(url: String) extends Target
object SeleniumWebPage {
  def apply(url: String): SeleniumWebPage = new SeleniumWebPage(url)
}

class SeleniumWebPage(val url: String) extends Target
