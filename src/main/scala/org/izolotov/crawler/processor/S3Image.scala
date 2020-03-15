package org.izolotov.crawler.processor

object S3Image {
  val Kind = "image"
}

case class S3Image(key: String,
                   error: Option[String] = None)
