package org.izolotov.crawler.v4

trait RedirectCatcher[A] {

  def getRedirectUrl(content: A): Option[String]

}
