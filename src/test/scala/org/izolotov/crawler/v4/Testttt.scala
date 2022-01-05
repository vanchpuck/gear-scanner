package org.izolotov.crawler.v4

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.HttpClients
import org.jsoup.Jsoup
import org.scalatest.FlatSpec

class Testttt extends FlatSpec {

  import scala.collection.JavaConverters._

  it should "..." in {
    val httpClient = HttpClients.createDefault()
    val httpGet = new HttpGet("http://example.com");
    val response: CloseableHttpResponse = httpClient.execute(httpGet)
    val doc = Jsoup.parse(response.getEntity.getContent, "UTF-8", "http://example.com")
    response.close()
    println(response.getStatusLine.getStatusCode)
    println(doc.getAllElements.asScala.foreach(el => println(el.toString)))
    val doc1 = Jsoup.parse(response.getEntity.getContent, "UTF-8", "http://example.com")
  }

}
