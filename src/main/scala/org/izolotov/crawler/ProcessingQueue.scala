package org.izolotov.crawler

trait ProcessingQueue[A] {

  def add(message: A): Unit

  def pull(numOfMessages: Int = 1): Iterable[A]

}