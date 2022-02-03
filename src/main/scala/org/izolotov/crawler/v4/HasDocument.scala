package org.izolotov.crawler.v4

trait HasDocument[Doc] {
  def doc(): Doc
}
