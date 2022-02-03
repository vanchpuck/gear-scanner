package org.izolotov.crawler.v4

import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.{ConcurrentHashMap, Executors, LinkedBlockingDeque}

import com.google.common.util.concurrent.ThreadFactoryBuilder

import scala.concurrent.{Await, ExecutionContext, Future}
import FetcherQueue._

import scala.collection.concurrent
import scala.collection.concurrent.TrieMap
import scala.concurrent.duration.Duration

object FetcherQueue {
  case class HostQueue(moderator: FixedDelayModerator, counter: AtomicInteger)
}

class FetcherQueue(nt: Int, delay: Long) {

  val queue = new LinkedBlockingDeque[FixedDelayModerator](10)
  queue.add(new FixedDelayModerator(delay))
  queue.add(new FixedDelayModerator(delay))

  case class Q(executionContext: ExecutionContext, moderator: FixedDelayModerator)
//  case class Q(executionContext: ExecutionContext, pool: LinkedBlockingDeque[FixedDelayModerator]) {
//    def crawl(url: URL, extractor: String => Unit): Future[Unit] = {
//      val f = Future {
//        val res = pool.take()
//        (res, res.extract(url.toString, extractor))
//      }(executionContext)
//      f.onComplete{Q(getEC(), queue)
//        f =>
//          f.get
//      }(executionContext)
//    }
//  }

  val map = collection.mutable.Map[String, Q]()

  val globalExecutor = Executors.newFixedThreadPool(nt, new ThreadFactoryBuilder().setDaemon(true).build)
  val ec = ExecutionContext.fromExecutor(globalExecutor)

  def fetch(url: URL, extractor: String => Unit): Future[Unit] = {
    val q = map.getOrElseUpdate(url.getHost, Q(getEC(), new FixedDelayModerator(delay)))
    Future {
      val f = Future {
        q.moderator.extract(url.toString, extractor)// extractor.apply(url.toString)
      }(ec)
      Await.result(f, Duration.Inf)
    }(q.executionContext)
  }

  private def getEC(): ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

}
