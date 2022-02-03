package org.izolotov.crawler.v4

class IntSelector extends Selector[Int] {
  override def extract(url: String): Int = {
    return 999
  }
}
