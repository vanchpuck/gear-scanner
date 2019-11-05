package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser

class DummyParser extends Parser[Product] {
  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    Product(url.toString, url.getHost, parseError = Some("No suitable parser found."))
  }
}
