package org.izolotov.crawler.v3

class HostScraper[Doc] {
  def extract[Raw](url: String)(implicit hostMapper: (String) => ((String) => Raw, (String, Raw) => Doc)): Doc = {
    val fetcherParserPair = hostMapper.apply(url)
    val rawResponse = fetcherParserPair._1.apply(url)
    fetcherParserPair._2.apply(url, rawResponse)
  }
}
