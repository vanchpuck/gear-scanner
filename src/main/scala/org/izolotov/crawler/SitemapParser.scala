package org.izolotov.crawler

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import crawlercommons.sitemaps.SiteMapParser
import org.apache.commons.io.IOUtils
import org.izolotov.crawler.parser.Parser

object SitemapParser extends Parser[Seq[SiteMap]]{

  private val parser = new SiteMapParser

  override def parse(url: URL, inStream: InputStream, charset: Charset = null): Seq[SiteMap] = {
    import scala.collection.JavaConversions._
    val urlString = url.toString
    val sitemap = parser.parseSiteMap(IOUtils.toByteArray(inStream), url)
    if (sitemap.isIndex) {
      val index = sitemap.asInstanceOf[crawlercommons.sitemaps.SiteMapIndex]
      index.getSitemaps.map(u => SiteMap(urlString, u.getUrl.toString, true)).toList
    } else {
      val index = sitemap.asInstanceOf[crawlercommons.sitemaps.SiteMap]
      index.getSiteMapUrls.map(u => SiteMap(urlString, u.getUrl.toString, false)).toList
    }
  }

}
