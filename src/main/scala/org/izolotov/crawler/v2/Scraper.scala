package org.izolotov.crawler.v2

import java.time.Clock

trait Scraper[Doc] {

  def extract[T <: Target, Raw, Attempt >: ResponseTrait[Doc]](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): Attempt

}
