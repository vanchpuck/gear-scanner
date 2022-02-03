package org.izolotov.crawler.v4

class StringSelector extends Selector[String] {
  override def extract(url: String): String = {
    "Ok"
  }
}
