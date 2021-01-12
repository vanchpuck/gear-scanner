package org.izolotov.crawler.parser.category

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.jsoup.{Jsoup, nodes}

import scala.tools.nsc.interpreter.InputStream
import org.jsoup.nodes.Document

abstract class JsoupParser[T] extends Parser[T]{

  override def parse(categoryUrl: URL, inStream: InputStream, charset: Charset): T = {
    parse(categoryUrl, Jsoup.parse(inStream, charset.name(), categoryUrl.toString))
  }

  protected def parse(categoryUrl: URL, doc: Document): T
}
