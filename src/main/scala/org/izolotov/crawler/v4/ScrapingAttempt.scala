package org.izolotov.crawler.v4

// TODO consider to use Try for content
case class ScrapingAttempt[+Content] (url: String, redirectUrl: Option[String], content: Content)
