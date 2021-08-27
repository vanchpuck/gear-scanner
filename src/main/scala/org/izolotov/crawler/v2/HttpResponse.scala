package org.izolotov.crawler.v2

case class HttpResponse[T](httpCode: Int, document: Option[T])
