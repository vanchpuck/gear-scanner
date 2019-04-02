package org.izolotov.crawler

import java.io.InputStream
import java.nio.charset.Charset

import org.apache.spark.sql.Encoder

trait Parser[A] {

  def parse(url: String, inStream: InputStream, charset: Charset): A

}
