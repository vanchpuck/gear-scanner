package org.izolotov.crawler.parser

import java.io.InputStream
import java.nio.charset.Charset

trait Parser[A] {

  def parse(url: String, inStream: InputStream, charset: Charset): A

}
