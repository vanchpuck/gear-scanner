package org.izolotov.crawler.v5

trait Processor[Attempt] {

  def process(attempt: Attempt): Unit

}
