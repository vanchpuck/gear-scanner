package org.izolotov.crawler.v2

import java.time.Clock

trait Scraper[Doc, Attempt] {

  def extract[T <: Target, Raw](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt

}
