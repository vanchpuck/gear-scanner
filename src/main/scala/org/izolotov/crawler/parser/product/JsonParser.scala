package org.izolotov.crawler.parser.product

import java.io.{InputStream, InputStreamReader}
import java.net.URL
import java.nio.charset.Charset

import io.circe.generic.semiauto.deriveDecoder
import io.circe.parser
import org.apache.commons.io.IOUtils
//import Product
import org.izolotov.crawler.parser.Parser

class JsonParser extends Parser[Product] {

  override def parse(url: URL, inStream: InputStream, charset: Charset): Product = {
    val urlString = url.toString
    try {
      implicit val staffDecoder = deriveDecoder[Product]
      parser.decode[Product](IOUtils.toString(new InputStreamReader(inStream))) match {
        case Right(product) => product
        case Left(error) => new Product(url = urlString, store = url.getHost, parseError = Some("Parsing error"))
      }
    } catch {
      case e: Exception => new Product(url = urlString, store = "json_store", parseError = Some(e.toString))
    }
  }

}
