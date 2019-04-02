package org.izolotov.crawler.parser

import java.io.{InputStream, InputStreamReader}
import java.net.URL
import java.nio.charset.Charset

import org.apache.commons.io.IOUtils
import org.izolotov.crawler.{Parser, Product}
import io.circe.parser
import io.circe.generic.semiauto.deriveDecoder

object JsonParser extends Parser[Product] {

  override def parse(url: String, inStream: InputStream, charset: Charset): Product = {
    implicit val staffDecoder = deriveDecoder[Product]
    parser.decode[Product](IOUtils.toString(new InputStreamReader(inStream))) match {
      case Right(product) => product
      case Left(error) => new Product(url = url, store = new URL(url).getHost, parseError = Some("Parsing error"))
    }
  }

}
