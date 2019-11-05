package org.izolotov.crawler.parser.category

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset



object CategoryParserRepo {

  def parse(host: String, url: URL, inStream: InputStream, charset: Charset): Category = {
    // TODO add default case
    host match {
      case AlpindustriaParser.StoreName => AlpindustriaParser.parse(url, inStream, charset)
      case TramontanaParser.StoreName => TramontanaParser.parse(url, inStream, charset)
      case KantParser.StoreName => KantParser.parse(url, inStream, charset)
      case EquipParser.StoreName => EquipParser.parse(url, inStream, charset)
      case SportMarafonParser.StoreName => SportMarafonParser.parse(url, inStream, charset)
      case BackcountryParser.StoreName => BackcountryParser.parse(url, inStream, charset)
      case ReiCoopParser.StoreName => ReiCoopParser.parse(url, inStream, charset)
      case TrekkinnParser.StoreName => TrekkinnParser.parse(url, inStream, charset)
      case DenSurkaParser.StoreName => DenSurkaParser.parse(url, inStream, charset)
    }
  }

}
