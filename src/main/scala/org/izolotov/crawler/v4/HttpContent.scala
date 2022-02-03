package org.izolotov.crawler.v4

case class HttpContent[A](document: A, redirectUrl: Option[String]) extends Content[A] {
  override def postProcess(): Content[A] = ???

  override def write()(implicit writer: Writer[A]): Unit = ???
}
