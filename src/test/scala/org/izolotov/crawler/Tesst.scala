package org.izolotov.crawler

import java.net.URL
import java.util.concurrent.Executors

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.apache.http.impl.client.HttpClients
import org.scalatest.flatspec.AnyFlatSpec
//import org.apache.spark.sql.{SaveMode, SparkSession}
//import org.apache.spark.sql.types.IntegerType
import org.izolotov.crawler.parser.origin.PetzlParser
import org.izolotov.crawler.v2.Target
import org.izolotov.crawler.v4.DataSource
import org.jsoup.Jsoup
//import org.scalatest.FlatSpec
import retry.{Defaults, Success}

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class Tesst extends AnyFlatSpec {

  trait Crwlr {
    def crwl(url: String)
  }

  class ConcreteCrwlr extends Crwlr {
    override def crwl(url: String): Unit = {
      println(url)
    }
  }

  trait Queued extends Crwlr {
    def addToQueue(url: String): Unit = {
      println(s"queued: ${this.crwl(url)}")
    }
  }


  it should "..." in {

  }

}
