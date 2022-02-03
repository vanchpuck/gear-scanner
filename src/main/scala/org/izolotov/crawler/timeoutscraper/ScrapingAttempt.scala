package org.izolotov.crawler.timeoutscraper

case class ScrapingAttempt[Document](url: String, document: Document)
