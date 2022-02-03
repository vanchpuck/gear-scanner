package org.izolotov.crawler.timeoutscraper

import java.util.concurrent.{Executors, TimeoutException}

import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class TimeoutScraperSpec extends AnyFlatSpec {

  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  class DummyFetcher extends BaseFetcher[String] {
    override def fetch(url: String): String = {
      Thread.sleep(100L)
      "Ok"
    }
  }

  it should "throw the TimeoutException on timeout" in {
    val thrown = intercept[Exception] {
      new TimeoutFetcher[String](new DummyFetcher(), 20L).fetch("htp://test.com")
    }
    assert(thrown.getClass === classOf[TimeoutException])
  }

  it should "return if no timeout occurs" in {
    assert(new TimeoutFetcher[String](new DummyFetcher(), 1000L).fetch("htp://test.com") === "Ok")
  }

}
