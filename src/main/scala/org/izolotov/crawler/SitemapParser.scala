package org.izolotov.crawler

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import crawlercommons.sitemaps.SiteMapParser
import org.apache.commons.io.IOUtils

object SitemapParser extends Parser[Seq[SiteMap]]{

  private val parser = new SiteMapParser

  override def parse(url: String, inStream: InputStream, charset: Charset = null): Seq[SiteMap] = {
    import scala.collection.JavaConversions._
    val sitemap = parser.parseSiteMap(IOUtils.toByteArray(inStream), new URL(url))
    if (sitemap.isIndex) {
      val index = sitemap.asInstanceOf[crawlercommons.sitemaps.SiteMapIndex]
      index.getSitemaps.map(u => SiteMap(url, u.getUrl.toString, true)).toList
    } else {
      val index = sitemap.asInstanceOf[crawlercommons.sitemaps.SiteMap]
      index.getSiteMapUrls.map(u => SiteMap(url, u.getUrl.toString, false)).toList
    }
  }

}
