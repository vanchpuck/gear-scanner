package org.izolotov.crawler.parser

sealed trait SiteMap{
  def url: String
  def urls: Iterable[String]
}

case class UrlSet(url: String, urls: Iterable[String]) extends SiteMap
case class SitemapIndex(url: String, urls: Iterable[String]) extends SiteMap
