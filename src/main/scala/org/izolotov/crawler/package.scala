package org.izolotov

import scala.beans.BeanProperty
import scala.collection.JavaConverters._
import scala.collection.mutable

package object crawler {

  case class UncrawledURL(url: String)

  case class HostURL(url: String, host: String)

  class CrawlConfiguration extends Serializable {
    @BeanProperty var cookies: java.util.Map[String, String] = new java.util.HashMap[String, String]
    @BeanProperty var fetchDelay: Long = 0
    @BeanProperty var parser: String = null

    def this(cookiesMap: Map[String, String] = Map.empty, fetchDelay: Long = 0){
      this()
      this.cookies = cookiesMap.asJava
      this.fetchDelay = fetchDelay
    }

    def getCookiesAsScala(): mutable.Map[String, String] = cookies.asScala

  }

  class HostCrawlConfiguration {
    @BeanProperty var hosts: java.util.Map[String, CrawlConfiguration] =
      new java.util.HashMap[String, CrawlConfiguration]()

    def this(confMap: Map[String, CrawlConfiguration]){
      this()
      hosts = confMap.asJava
    }

    def getHostsAsScala(): Map[String, CrawlConfiguration] = hosts.asScala.toMap
  }
}
