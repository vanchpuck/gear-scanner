package org.izolotov.crawler.parser.category

import java.net.URL

import scala.util.Try

case class Category(nextURL: Option[URL], productURLs: Iterable[Try[URL]])
