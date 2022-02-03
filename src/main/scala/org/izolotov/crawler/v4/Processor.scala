package org.izolotov.crawler.v4

trait Processor[A, B] {

  def process(attempt: A): B

}
