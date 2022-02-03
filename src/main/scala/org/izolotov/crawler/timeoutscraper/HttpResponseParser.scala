//package org.izolotov.crawler.timeoutscraper
//
//import java.io.InputStream
//import java.net.URL
//import java.nio.charset.Charset
//
//import org.apache.http.HttpStatus
//import org.apache.http.client.methods.CloseableHttpResponse
//import org.apache.http.entity.ContentType
//
//abstract class HttpResponseParser[A] {
//
//  def parse(response: CloseableHttpResponse): A = {
//    response.getEntity.getContentEncoding
//    val doc = parseContent(new URL(fetchAttempt.url), response.getEntity.getContent, ContentType.getOrDefault(response.getEntity).getCharset)
//    val response = fetchAttempt.response
//    val statusCode = response.getStatusLine.getStatusCode
//    val content: Option[A] = statusCode match {
//      case HttpStatus.SC_OK => Some(parseContent(new URL(fetchAttempt.url), response.getEntity.getContent, ContentType.getOrDefault(response.getEntity).getCharset))
//      case _ => None
//    }
//    HttpDocument(
//      fetchAttempt.url, fetchAttempt.timestamp, fetchAttempt.fetchTime, statusCode, content
//    )
//  }
//
//  protected def parseContent(categoryUrl: URL, inStream: InputStream, charset: Charset): A
//}
