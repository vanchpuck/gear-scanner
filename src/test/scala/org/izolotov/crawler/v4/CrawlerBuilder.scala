//package org.izolotov.crawler.v4
//
//class CrawlerBuilder {
//
//  trait UrlsQueue {
//
//  }
//
////  trait Crawler[A]{
////    def crawl(): A
////  }
//
//  trait Crawler {
//    def read(queue: UrlsQueue): Fetchable
//    def readIterable(iterable: Iterable[String]): Fetchable
//
//  }
//
//  trait Step {
//    def apply()
//  }
//
//  trait Fetchable {
//    def fetch[A](fetcher: (String) => A): Parsable[A]
//    def fork(predicate: (String) => Boolean): FetchableFork
//  }
//
//  trait Parsable[A] extends Action {
//    def ignoreRedirect(): Parsable[A]
////    def redirectDepth(): Parsable
//    def parse[B](parser: A => B): Content[B]
//  }
//
//  trait Content[A] extends Action {
//    def write(writer: A => Unit): Pipeline
//    def get()
//  }
//
//  trait Action {
//    def apply()
//  }
//
//  trait Pipeline {
//    def start()
//  }
//
//  trait FetchableFork {
//    def fetch(): ParsableFork
//  }
//
//  trait ParsableFork {
//    def ignoreRedirect(): Parsable
//    def parse(): ContentFork
//  }
//
//  trait ContentFork {
//    def fork(predicate: (String) => Boolean): FetchableFork
//    def default(): Parsable
//  }
//
//  object CrawlerImpl extends Crawler {
//    override def read(queue: UrlsQueue): Fetchable = ???
//    def readIterable(iterable: Iterable[String]): Fetchable = {
//      iterable.foreach{
//        url =>
//
//      }
//    }
//  }
//
//  class FetchableImpl() extends Fetchable {
//    override def fetch[A](fetcher: String => A): Parsable[A] = {
//
//    }
//
//    override def fork(predicate: String => Boolean): FetchableFork = ???
//  }
//
//  class PipelineImpl[A](content: Content[A]) extends Pipeline {
//    override def start(): Unit = {
//      content.apply()
//    }
//  }
//
//  class ContentImpl[A](parsable: Parsable[A]) extends Content[A] {
//    override def write(writer: Parsable[A] => Unit): Pipeline = {
//
//      new PipelineImpl[A](writer.apply(parsable))
//    }
//
//    override def get(): Unit = ???
//
//    override def apply(): Unit = ???
//  }
//
//  def predicate1: (String => Boolean) = a => a.startsWith("1")
//  def predicate2: (String => Boolean) = a => a.startsWith("2")
//
//  def fetcher1: (String => Int) = a => 1
//  def parser1: (Int => Long) = a => 1.toLong
//
//  def writer: (Long => Unit) = a => println("Writing...")
//
//  def main(args: Array[String]): Unit = {
//    CrawlerImpl.read(null)
//      .fetch(fetcher1).parse(parser1).write(writer).start()
////      .fork(predicate1).fetch().parse()
////      .fork(predicate2).fetch().parse()
////      .default().parse()
////      .write()
//  }
//
//}
