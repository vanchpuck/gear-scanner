package org.izolotov.crawler.v4

import org.apache.http.client.HttpClient

class SimpleSelector()(implicit httpClient: HttpClient) extends Selector[String] {
  override def extract(url: String): String = {
    null
  }
}
