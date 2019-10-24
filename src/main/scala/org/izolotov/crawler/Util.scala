package org.izolotov.crawler

import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.cookie.ClientCookie
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.protocol.{BasicHttpContext, HttpContext}

import scala.util.Try

/**
  * Created by izolotov on 24.12.18.
  */
object Util {

//  def parsePrice(priceStr: String): Try[Int] = {
//    Try(priceStr.replaceAll("[^0-9]", "").toInt)
//  }

  def parsePrice(priceStr: String): Float = {
    priceStr.replaceAll("[^0-9,.]", "").toFloat
  }

  def createHttpContext(host: String, crawlConf: Option[CrawlConfiguration]): Option[HttpContext] = {
    crawlConf.map{
      conf =>
        val cookieStore = new BasicCookieStore()
        conf.getCookiesAsScala().foreach {
          cookieConf: (String, String) =>
            val cookie = new BasicClientCookie(cookieConf._1, cookieConf._2);
            cookie.setDomain(host)
            cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true")
            cookie.setPath("/")
            cookieStore.addCookie(cookie)
        }
        val httpContext = new BasicHttpContext()
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore)
        httpContext
    }
  }

}
