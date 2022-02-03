package org.izolotov.crawler

import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.JavaConverters._

class HostCrawlConfigurationReaderSpec extends AnyFlatSpec {

  behavior of "HostCrawlConfigurationReader"

  it should "successfully read valid configuration file" in {
    val inStream = this.getClass.getResourceAsStream("/host-crawl-conf/crawl-conf.yml")
    val actual: HostCrawlConfiguration = HostCrawlConfigurationReader.read(inStream)
    assert(actual.hosts.asScala("www.trekkinn.com").cookies.asScala == Map("id_pais" -> "164"))
    assert(actual.hosts.asScala("www.trekkinn.com").fetchDelay == 100)
    assert(actual.hosts.asScala("www.rei.com").fetchDelay == 50)
  }

}
