package org.izolotov.crawler.parser
import java.io.{FileInputStream, InputStream}
import java.net.URL
import java.nio.charset.Charset

import org.apache.commons.io.IOUtils

class BinaryDataParser extends Parser[Array[Byte]] {

  override def parse(url: URL, inStream: InputStream, charset: Charset): Array[Byte] = {
    IOUtils.toByteArray(inStream)
  }

}
