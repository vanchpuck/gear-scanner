package org.izolotov.crawler

import java.io.InputStream

import org.izolotov.crawler.ProductCrawlerApp.CrawlConfFileName
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.introspector.BeanAccess

object HostCrawlConfigurationReader {

  def read(inputStream: InputStream): HostCrawlConfiguration = {
    val yaml = new Yaml(new Constructor(classOf[HostCrawlConfiguration]))
    yaml.setBeanAccess(BeanAccess.FIELD)
    yaml.load(inputStream).asInstanceOf[HostCrawlConfiguration]
  }

}
