package org.izolotov.crawler.parser.category

import java.net.URL

object Category {
  val Kind = "category"
}

case class Category(nextURL: Option[URL], productURLs: Iterable[URL])
