package org.izolotov.crawler.v4

trait Response[A, B] {

  def parse()(implicit parser: Parser[A, B]): Content[B]

  def redirectUrl(): Option[String]

}
