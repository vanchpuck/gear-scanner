package org.izolotov.crawler.v4

trait Content[A] {

  def document(): A

  def redirectUrl(): Option[String]

  def postProcess(): Content[A]

  def write()(implicit writer: Writer[A]): Unit

}
