package org.izolotov.crawler.v2

import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

import org.apache.http.{HttpEntity, HttpStatus}
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.ContentType

import scala.util.Try

abstract class BaseHttpParser[T] extends Parsable[CloseableHttpResponse, HttpResponse[T]] {

  override def parse(url: String, raw: CloseableHttpResponse): HttpResponse[T] = {

    val httpCode = raw.getStatusLine.getStatusCode
    val document: Option[T] = if (httpCode != HttpStatus.SC_OK) None else {
      val entity = raw.getEntity
      val document = parseContent(new URL(url), entity.getContent, ContentType.getOrDefault(entity).getCharset)
      val httpEntity = raw.getEntity
      httpEntity.getContent.close()
      raw.close()
      Some(document)
    }
    HttpResponse(httpCode, document)
  }

  def parseContent(url: URL, inStream: InputStream, charset: Charset): T
}
