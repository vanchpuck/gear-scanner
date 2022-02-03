//package org.izolotov.crawler.v4
//
//abstract class RedirectScraper[A] extends Extractor[A] {
//
//  def extractRedirectable(target: QueueItem): ScrapingAttempt[A] = {
//    val attempt = extract(target.url())
//    attempt.redirectUrl.map(redirect => target.queue().add(redirect))
//    processContent(attempt.content)
//  }
//
//  def getRed
//
//}
