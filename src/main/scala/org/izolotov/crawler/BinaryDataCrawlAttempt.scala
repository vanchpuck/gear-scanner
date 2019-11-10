package org.izolotov.crawler

import java.sql.Timestamp

class BinaryDataCrawlAttempt(url: String,
                             timestamp: Timestamp,
                             httpCode: Option[Int],
                             responseTime: Option[Long],
                             fetchError: Option[String],
                             image: Option[Array[Byte]]) {

}
