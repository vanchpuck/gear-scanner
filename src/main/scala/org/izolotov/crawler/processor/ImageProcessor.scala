package org.izolotov.crawler.processor

import java.net.URL

import org.izolotov.crawler.CrawlAttempt

import scala.util.{Failure, Success, Try}

class ImageProcessor(addToImageDB: (String, Array[Byte]) => Unit,
                     storeToDB: (CrawlAttempt[S3Image]) => Unit,
                     addToDLQueue: (CrawlAttempt[Array[Byte]]) => Unit = null) extends Processor[Array[Byte]] {

  override def process(attempt: CrawlAttempt[Array[Byte]]): CrawlAttempt[Array[Byte]] = {
    val s3Img: Option[S3Image] = attempt.document.map{
      data =>
        val s3Key = getS3Key(attempt.url)
        Try(addToImageDB.apply(s3Key, data)) match {
          case Success(_) => S3Image(s3Key, None)
          case Failure(e) => S3Image(s3Key, Some(e.toString))
        }
    }
    s3Img.getOrElse(Option(addToDLQueue).map(function => function(attempt)))
    // TODO remove file from S3 if can't write to Dynamo
    storeToDB.apply(CrawlAttempt(attempt.url, attempt.timestamp, attempt.httpCode, attempt.responseTime, attempt.fetchError, s3Img))
    attempt
  }

  private def getS3Key(urlString: String): String = {
    val url = new URL(urlString)
    url.getHost + url.getFile
  }
}
