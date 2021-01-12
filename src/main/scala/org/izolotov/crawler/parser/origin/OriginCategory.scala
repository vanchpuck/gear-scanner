package org.izolotov.crawler.parser.origin

import java.net.URL

object OriginCategory {
  val Kind = "originCategory"
}

case class OriginCategory(nextURL: Option[URL], products: Iterable[OriginProduct])
