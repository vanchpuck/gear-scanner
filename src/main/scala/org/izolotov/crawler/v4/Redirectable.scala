package org.izolotov.crawler.v4

case class Redirectable[+A](redirectUrl: Option[String], response: A)
