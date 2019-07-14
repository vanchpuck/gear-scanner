package org.izolotov.crawler.parser.product

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.izolotov.crawler.parser.Parser

object ReiParser extends Parser[Product] {

  val StoreName = "rei.com"

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    val file = new URL(url).getFile
    if (file.startsWith("/rei-garage/")) {
      ReiGarageParser.parse(url, inStream, charset)
    } else {
      ReiCoopParser.parse(url, inStream, charset)
    }
  }
}
