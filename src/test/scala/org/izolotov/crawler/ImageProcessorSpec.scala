package org.izolotov.crawler

import org.scalatest.FlatSpec

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import ImageProcessorSpec._
import org.izolotov.crawler.processor.{ImageProcessor, S3Image}

object ImageProcessorSpec {
  val ImageURL1 = "http://image1.com"
  val ImageURL2 = "http://image2.com"

  implicit class ImageStore(val map: mutable.Map[String, Array[Byte]]) {
    val NullValueErrorMessage = "Value can't be null"
    def putNonNull(key: String, value: Array[Byte]) = {
      require(value != null, NullValueErrorMessage)
      map.put(key, value)
    }
  }
}

class ImageProcessorSpec extends FlatSpec {

  it should "add image to ImageStore and place record to CrawlDB" in {
    import ImageProcessorSpec.ImageStore
    val imageStore: mutable.Map[String, Array[Byte]] = mutable.Map[String, Array[Byte]]()
    val crawlDB: ListBuffer[CrawlAttempt[S3Image]] = new ListBuffer

    val imageData: Map[String, Array[Byte]] = Map(
      ImageURL1 -> Array(10.toByte),
      ImageURL2 -> null
    )
    val attempts = imageData.toList.map(img => CrawlAttempt(img._1, 0L, None, None, None, Some(img._2)))

    val processor = new ImageProcessor(imageStore.putNonNull, crawlDB.+=)
    attempts.foreach(processor.process)

    val crawlDBExpected = Seq(
      CrawlAttempt(ImageURL1, 0L, None, None, None, Some(S3Image(ImageURL1, None))),
      CrawlAttempt(ImageURL2, 0L, None, None, None, Some(S3Image(ImageURL2, Some(s"java.lang.IllegalArgumentException: requirement failed: ${imageStore.NullValueErrorMessage}"))))
    )

    assert(crawlDBExpected.toSet == crawlDB.toSet)
    assert(imageData.filter(rec => rec._2 != null).toSet == imageStore.toSet)
  }

}
