package org.izolotov.crawler.v4

object Util {

  object CrawlerBuilder {
    // передавать ридер как параметр
    // собираем пайплайн
    def read(iterable: Iterable[String]): FetcherBuilder = {
      new FetcherBuilder()
    }
  }

  class FetcherBuilder() {
    def withFetcher[A](fetcher: String => A): ParserBuilder[A] = {
      new ParserBuilder(fetcher, this)
    }
    def build[A](fetcher: String => A): Fetcher[A] = {
      new Fetcher[A](fetcher)
    }
  }

  class ParserBuilder[A](fetcher: String => A, fetcherBuilder: FetcherBuilder) {
    def withParser[B](parser: A => B): WriterBuilder[A,B] = {
      new WriterBuilder[A, B](parser, this)
    }
    def build[B](parser: A => B): Parser[A, B] = {
      new Parser[A, B](fetcherBuilder.build(fetcher), parser)
    }
  }

  class WriterBuilder[A, B](parser: A => B, parserBuilder: ParserBuilder[A]) {
    def withWriter(writer: B => Unit): Starter[A, B] = {
      new Starter(writer, this)
    }
    def build(writer: B => Unit): Writer[B] = {
      new Writer[B](parserBuilder.build(parser), writer)
    }
  }

  class Starter[A, B](writer: B => Unit, writerBuilder: WriterBuilder[A, B]) {
    def build(): Writer[B] = {
      writerBuilder.build(writer)
    }
  }

  class Fetcher[A](fetcher: (String) => A) extends Extractor[A] {
    def extract(url: String): A = {
      fetcher.apply(url)
    }
  }

  class Parser[A, B](fetcher: Fetcher[A], parser: (A) => B) extends Extractor[B] {
    def extract(url: String): B = {
      parser.apply(fetcher.extract(url))
    }
  }

  class Writer[B](parser: Parser[_, B], writer: (B) => Unit) extends Extractor[Unit] {
    def extract(url: String): Unit = {
      writer.apply(parser.extract(url))
    }

    def build(): (String) => Unit = {
      this.extract
    }
  }



  class Processor[A](iterable: Iterable[String]) {
    def processQueue(extractor: Extractor[A]): Unit = {
      iterable.foreach{
        url => extractor.extract(url)
      }
    }
  }

}
