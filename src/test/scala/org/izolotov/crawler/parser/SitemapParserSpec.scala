package org.izolotov.crawler.parser

import java.net.URL

import org.scalatest.flatspec.AnyFlatSpec

class SitemapParserSpec extends AnyFlatSpec {

  ignore should "..." in {
    val inStream = this.getClass.getClassLoader.getResourceAsStream("parser/origin/black-diamond/sitemap.xml")
    val sitemaps = SitemapParser.parse(new URL("https://www.blackdiamondequipment.com"), inStream)
//    sitemaps.foreach(println)
  }

}
