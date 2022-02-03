package org.izolotov.crawler.timeoutscraper

trait FetchingAttempt[Metadata, Content] {

  def metadata(): Metadata

  def content(): Content
}
