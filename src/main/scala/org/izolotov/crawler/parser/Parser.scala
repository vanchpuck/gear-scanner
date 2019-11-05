package org.izolotov.crawler.parser

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

trait Parser[A] {

  def parse(url: URL, inStream: InputStream, charset: Charset): A

}
