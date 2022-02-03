//package org.izolotov.crawler.timeoutscraper
//
//import java.net.URL
//import java.util.concurrent.{ConcurrentHashMap, Executors}
//
//import scala.collection.JavaConverters._
//import scala.concurrent.{ExecutionContext, Future}
//import ScrapingCore.{FetchingException, _}
//
//object ScrapingCore {
//  class FetchingQueueManager(threadsNumber: Int)(implicit queueFactory: () => FetchingQueue) extends AutoCloseable {
//
//    val executor = Executors.newFixedThreadPool(threadsNumber)
//    implicit val executionContext = ExecutionContext.fromExecutor(executor)
//
//    private val queueMap: scala.collection.concurrent.Map[String, FetchingQueue] = new ConcurrentHashMap[String, FetchingQueue]().asScala
//
//    def fetch[T](url: String, fetcher: (String) => T): Future[T] = {
//      val host = new URL(url).getHost
//      val queue = queueMap.getOrElseUpdate(
//        host,
//        queueFactory.apply()
//      )
//      queue.add(url, fetcher)
//    }
//
//    override def close(): Unit = {
//      executor.shutdown()
//      queueMap.clear()
//    }
//  }
//
//  class FetchingException(cause: Throwable) extends Exception(cause)
//
//  class ParsingException(cause: Throwable) extends Exception(cause)
//
//}
//
//class ScrapingCore(fetcherThreadsNum: Int)
//                  (implicit queueFactory: () => FetchingQueue) {
//
////  import scala.concurrent.ExecutionContext.Implicits.global
//
//  val queueManager = new FetchingQueueManager(fetcherThreadsNum)
//
//  // TODO как будто, нужно убрать фетчер и парсер из имплиситов. На нужно передавать не объект, а класс
//  def extract[Fetched, Parsed](url: String, fetcher: (String) => Fetched, parser: (Fetched) => Parsed): Future[Parsed] = queueManager.fetch(url, fetcher)
//    .transform(s => s, e => new FetchingException(e))
//    .map(fetchAttempt => parser.apply(fetchAttempt))
//    // TODO reconsider the exception wrapping logic
//    .transform(s => s, e => {
//      e match {
//        case fExc: FetchingException => fExc
//        case any: Throwable => new ParsingException(any)
//      }
//    })
//
//
//
//}
