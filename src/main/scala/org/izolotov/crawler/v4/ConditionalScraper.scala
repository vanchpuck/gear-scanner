package org.izolotov.crawler.v4

class ConditionalScraper[A](default: (String) => A, conditionals: ((String => Boolean), (String) => A)*) {

  def extract(url: String): A = {
    conditionals.find(conditional => conditional._1.apply(url))
      .map(conditional => conditional._2)
      .getOrElse(default)
      .apply(url)
  }

}
