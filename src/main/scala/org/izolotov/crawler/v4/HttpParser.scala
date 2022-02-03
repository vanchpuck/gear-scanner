//package org.izolotov.crawler.v4
//
//import java.net.URL
//
//import org.apache.http.client.methods.CloseableHttpResponse
//
//abstract class HttpParser[Doc] extends Parser[CloseableHttpResponse, Redirectable[Doc]] {
//  override def parse(url: URL, response: CloseableHttpResponse): Redirectable[Doc] = {
//    Redirectable(None, parseContent(url, response))
//  }
//
//  abstract def parseContent(url: URL, response: CloseableHttpResponse): Doc
//}
