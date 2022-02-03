//package org.izolotov.crawler.v4
//
//import scala.concurrent.Future
//
//abstract class HostQueue[A](scraper: (String) => A) {
//
//  def addToQeuue(url: String)
//
//  def add(url: String): Future[A] = {
//    new ScrapingQueue().bind()
//  }
//
//}
