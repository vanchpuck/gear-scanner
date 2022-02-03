package org.izolotov.crawler.parser

import java.net.URL
import java.nio.charset.Charset

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.io.InputStream

abstract class JsoupParser[T] extends Parser[T]{

  override def parse(categoryUrl: URL, inStream: InputStream, charset: Charset): T = {
    parse(categoryUrl, Jsoup.parse(inStream, charset.name(), categoryUrl.toString))
  }

  protected def parse(categoryUrl: URL, doc: Document): T
}
