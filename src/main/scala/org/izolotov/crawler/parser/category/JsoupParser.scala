package org.izolotov.crawler.parser.category

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser
import org.jsoup.{Jsoup, nodes}

import scala.tools.nsc.interpreter.InputStream
import org.jsoup.nodes.Document

abstract class JsoupParser extends Parser[Category]{

  override def parse(categoryUrl: String, inStream: InputStream, charset: Charset): Category = {
    parse(categoryUrl, Jsoup.parse(inStream, charset.name(), categoryUrl))
  }

  protected def parse(categoryUrl: String, doc: Document): Category
}
