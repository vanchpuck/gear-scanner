package org.izolotov.crawler.v4

import org.scalatest.flatspec.AnyFlatSpec

class UtilV2 extends AnyFlatSpec {

  trait Stage[A] {
    def execute(url: String): A

    def start(): Unit

    def urls(): Iterable[String]
  }

  trait Starter[A] extends Stage[A] {
    def start(urls: Iterable[String]): Unit = {
      urls.foreach(url => {
        println("!!")
        execute(url)
      })
    }
  }


  class FetchingStage[A](urls: Iterable[String], fetcher: String => A) extends Starter[A] {
    def execute(url: String): A = {
      fetcher.apply(url)
    }
    def start(): Unit = {
      start(urls)
    }
    def urls(): Iterable[String] = {
      urls
    }
  }

  class ParsingStage[A, B](fetchingStage: Stage[A], parser: A => B) extends Stage[B] {
    def execute(url: String): B = {
      parser.apply(fetchingStage.execute(url))
    }
    def start(): Unit = {
      fetchingStage.start()
    }
    def urls(): Iterable[String] = {
      fetchingStage.urls()
    }
  }

//  class Fork[A](prev: Stage[A], predicate: String => Boolean) {
//    def execute(url: String): A = {
//      if (predicate(url)) {
//        prev.execute(url)
//      }
//    }
//    def start(): Unit = {
//      prev.start()
//    }
//    def urls(): Iterable[String] = {
//      prev.urls()
//    }
//  }

  class WritingStage[B, C](parsingStage: ParsingStage[_, B], writer: B => C) extends Stage[C] {
    def execute(url: String): C = {
      writer.apply(parsingStage.execute(url))
    }

    def start(): Unit = {
      urls.foreach{
        url => execute(url)
      }
    }

    def urls(): Iterable[String] = {
      parsingStage.urls()
    }
  }

  object Crawl {
    def read(source: Iterable[String]): FetchingStageBuilder = {
      new FetchingStageBuilder(source)
    }
  }

  class FetchingStageBuilder(source: Iterable[String]) {
    def fetch[A](fetcher: String => A): ParsingStageBuilder[A] = {
      new ParsingStageBuilder[A](fetcher, this)
    }
    def fork(): ForkBuilder = {
      new ForkBuilder()
    }
    def build[A](fetcher: String => A): FetchingStage[A] = {
      new FetchingStage[A](source, fetcher)
    }
  }

  class ForkBuilder {

  }

  class ParsingStageBuilder[A](fetcher: String => A, fetchingStageBuilder: FetchingStageBuilder) {
    def parse[B](parser: A => B): WritingStageBuilder[A, B] = {
      new WritingStageBuilder[A, B](parser, this)
    }
    def build[B](parser: A => B): ParsingStage[A, B] = {
      new ParsingStage[A, B](fetchingStageBuilder.build(fetcher), parser)
    }
  }

  class WritingStageBuilder[A, B](parser: A => B, parsingStageBuilder: ParsingStageBuilder[A]) {
    def write[C](writer: B => C): Builder[B, C] = {
      new Builder[B, C](writer: B => C, this)
    }
    def build[C](writer: B => C): WritingStage[B, C] = {
      new WritingStage[B, C](parsingStageBuilder.build(parser), writer)
    }
  }

  class Builder[B, C](writer: B => C, writingStageBuilder: WritingStageBuilder[_, B]) {
    def start(): Unit = {
      writingStageBuilder.build(writer).start()
    }
  }




  it should ".." in {
    new WritingStage[Double, Unit](new ParsingStage[Long, Double](new FetchingStage(Seq("1", "3"), a => a.toLong), b => b.toDouble), c => println(c)).start()
    Crawl.read(Seq("10", "20")).fetch(a => a.toLong).parse(b => b.toDouble).write(c => println(c)).start()
  }

}
