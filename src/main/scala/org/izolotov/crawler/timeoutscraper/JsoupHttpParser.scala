//package org.izolotov.crawler.timeoutscraper
//
//import java.io.InputStream
//import java.net.URL
//import java.nio.charset.Charset
//
//import org.izolotov.crawler.parser.Parser
//import org.jsoup.Jsoup
//import org.jsoup.nodes.Document
//
//abstract class JsoupHttpParser[T] extends HttpResponseParser[T]{
//
//  override def parseContent(categoryUrl: URL, inStream: InputStream, charset: Charset): T = {
//    parse(categoryUrl, Jsoup.parse(inStream, charset.name(), categoryUrl.toString))
//  }
//
//  protected def parse(categoryUrl: URL, doc: Document): T
//}
