package org.izolotov.crawler.parser

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import crawlercommons.sitemaps.SiteMapParser
import org.apache.commons.io.IOUtils

object SitemapParser extends Parser[SiteMap]{

  private val parser = new SiteMapParser

  override def parse(url: URL, inStream: InputStream, charset: Charset = null): SiteMap = {
    import scala.collection.JavaConverters._
    val urlString = url.toString
    val sitemap = parser.parseSiteMap(IOUtils.toByteArray(inStream), url)
    if (sitemap.isIndex) {
      val index = sitemap.asInstanceOf[crawlercommons.sitemaps.SiteMapIndex]
      UrlSet(urlString, index.getSitemaps.asScala.map(u => u.getUrl.toString))
    } else {
      val index = sitemap.asInstanceOf[crawlercommons.sitemaps.SiteMap]
      SitemapIndex(urlString, index.getSiteMapUrls.asScala.map(u => u.getUrl.toString))
    }
  }

}
