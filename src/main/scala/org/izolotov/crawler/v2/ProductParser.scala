package org.izolotov.crawler.v2

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.product.Product

abstract class ProductParser extends BaseHttpParser[Product] {
  override def parseContent(url: URL, inStream: InputStream, charset: Charset): Product = {
    try {
      parseDocument(url, inStream, charset)
    } catch {
      case e: Exception => new Product(url = url.toString, store = url.getHost, parseError = Some(e.toString))
    }
  }

  def parseDocument(url: URL, inStream: InputStream, charset: Charset): Product
}
