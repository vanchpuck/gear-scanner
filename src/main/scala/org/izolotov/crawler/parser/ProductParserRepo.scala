package org.izolotov.crawler.parser

import java.io.InputStream
import java.nio.charset.Charset

import org.izolotov.crawler.Product

object ProductParserRepo {

  def parse(host: String, url: String, inStream: InputStream, charset: Charset): Product = {
    // TODO add default case
    host match {
      case "localhost" => JsonParser.parse(url, inStream, charset)
      case AlpindustriaParser.StoreName => AlpindustriaParser.parse(url, inStream, charset)
      case TramontanaParser.StoreName => TramontanaParser.parse(url, inStream, charset)
      case KantParser.StoreName => KantParser.parse(url, inStream, charset)
      case EquipParser.StoreName => EquipParser.parse(url, inStream, charset)
      case PlanetaSportParser.StoreName => PlanetaSportParser.parse(url, inStream, charset)
    }
  }

}
