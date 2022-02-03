package org.izolotov.crawler.v3

import org.apache.http.client.methods.CloseableHttpResponse

class HttpParser {
  def parse(url: String, t: CloseableHttpResponse): String = {
    val response = t.getStatusLine.getStatusCode.toString
    t.close()
    response
  }
}
