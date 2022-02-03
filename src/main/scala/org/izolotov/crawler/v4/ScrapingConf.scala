package org.izolotov.crawler.v4

case class ScrapingConf(scraper: (String) => Unit, delay: Long, timeout: Long)
