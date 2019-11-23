package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser

class ReiParser extends Parser[Product] {

  val StoreName = "www.rei.com"

  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    val file = url.getFile
    if (file.startsWith("/rei-garage/")) {
      ReiGarageParser.parse(url, inStream, charset)
    } else {
      ReiCoopParser.parse(url, inStream, charset)
    }
  }
}
