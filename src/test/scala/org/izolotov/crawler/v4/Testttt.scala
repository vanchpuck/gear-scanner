package org.izolotov.crawler.v4

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.HttpClients
import org.jsoup.Jsoup
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.{ExecutionContext, Future}

class Testttt extends AnyFlatSpec {

  import scala.collection.JavaConverters._

  def f1(s: String): Boolean = {
    if (s == "1") {
      true
    } else {
      false
    }
  }
  def f2(s: String): Boolean = {
    if (s == "2") {
      true
    } else {
      false
    }
  }

  it should "..." in {
    val predicate1: String => Boolean = f1
    val predicate2: String => Boolean = f2
    val pf: PartialFunction[String, String] = {case f if predicate1.apply(f) => "Ok"}
    val pf1: PartialFunction[String, String] = {case f if predicate2.apply(f) => "Ok1"}
    val dpf: PartialFunction[String, String] = {case d => "default"}

    val a = pf.andThen(str => str.toUpperCase).orElse(pf1).andThen(str => str.toUpperCase).orElse(dpf).apply("2")
    println(a)
//    val httpClient = HttpClients.createDefault()
//    val httpGet = new HttpGet("http://example.com");
//    val response: CloseableHttpResponse = httpClient.execute(httpGet)
//    val doc = Jsoup.parse(response.getEntity.getContent, "UTF-8", "http://example.com")
//    response.close()
//    println(response.getStatusLine.getStatusCode)
//    println(doc.getAllElements.asScala.foreach(el => println(el.toString)))
//    val doc1 = Jsoup.parse(response.getEntity.getContent, "UTF-8", "http://example.com")
  }

}
