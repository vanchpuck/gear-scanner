package org.izolotov.crawler.v2

trait Resp[A, B] {
  def response(): A
}
