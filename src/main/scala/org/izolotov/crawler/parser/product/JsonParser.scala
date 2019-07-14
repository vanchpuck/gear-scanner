package org.izolotov.crawler.parser.product

import java.io.{InputStream, InputStreamReader}
import java.net.URL
import java.nio.charset.Charset

import io.circe.generic.semiauto.deriveDecoder
import io.circe.parser
import org.apache.commons.io.IOUtils
//import Product
import org.izolotov.crawler.parser.Parser

object JsonParser extends Parser[Product] {

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    implicit val staffDecoder = deriveDecoder[Product]
    parser.decode[Product](IOUtils.toString(new InputStreamReader(inStream))) match {
      case Right(product) => product
      case Left(error) => new Product(url = url, store = new URL(url).getHost, parseError = Some("Parsing error"))
    }
  }

}
