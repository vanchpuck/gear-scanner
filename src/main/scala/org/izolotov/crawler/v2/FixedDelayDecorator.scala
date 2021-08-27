//package org.izolotov.crawler.v2
//
//import java.util.concurrent.locks.ReentrantLock
//import java.util.concurrent.{Executors, TimeUnit}
//
//import org.izolotov.crawler.v2.{Fetcher, Scraper, Target}
//
//import scala.concurrent.duration.Duration
//import scala.concurrent.{Await, ExecutionContext, Future}
//
//class FixedDelayDecorator[R](scraper: Scraper[R], delay: Long) extends Scraper[R] {
//
//  private val delayLock = new ReentrantLock
//  private val fetcherLock = new ReentrantLock
//  private val lockedForTimeout = delayLock.newCondition
//
//  private var prevFetchTime: Long = 0
//
//  private class Delayer(val delay: Long) extends Runnable {
//    override def run(): Unit = {
//      delayLock.lock()
//      try {
//        Thread.sleep(delay)
//      }
//      finally {
//        lockedForTimeout.signal()
//        delayLock.unlock()
//      }
//    }
//  }
//
//  implicit val processingContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))
//
//  override def extract[T <: Target, Raw, Doc](target: T)(implicit fetcher: Fetcher[T, Raw], parser: Parsable[Raw, Doc]): R = {
//    fetcherLock.lock()
//    try {
//      delayLock.lock()
//      val nextFetchTime = prevFetchTime + delay
//      val remainingDelay = nextFetchTime - System.currentTimeMillis
//      if (System.currentTimeMillis < nextFetchTime) new Thread(new Delayer(remainingDelay)).start()
//      while (System.currentTimeMillis < nextFetchTime) {
//        lockedForTimeout.await(remainingDelay, TimeUnit.MILLISECONDS)
//      }
//      try {
//        scraper.extract(target)
//      } finally {
//        prevFetchTime = System.currentTimeMillis
//        delayLock.unlock()
//      }
//    } finally fetcherLock.unlock()
//  }
//}
//
