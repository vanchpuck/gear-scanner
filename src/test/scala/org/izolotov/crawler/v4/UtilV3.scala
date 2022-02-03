package org.izolotov.crawler.v4

import java.net.URL
import java.util.concurrent.Executors

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.apache.http.HttpEntity
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.flatspec.AnyFlatSpec
//import org.scalatest.FlatSpec
import shapeless.ops.hlist.ToTraversable

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class UtilV3 extends AnyFlatSpec {

  case class QueueItem(url: String, depth: Int)

  class CrawlingQueue(data: Iterable[String]) extends Iterator[String]{
    val queue: mutable.Queue[String] = mutable.Queue()
    queue ++= data

    def add(url: String): Unit = {
      queue += url
    }

    override def hasNext: Boolean = {
      val a = queue.length > 0
//      println(s"hasNext: ${a}")
      queue.length > 0
    }

    override def next(): String = {
//      println("next")
      queue.dequeue()
    }
  }

//  class PipelineReader {
//    def read(data: Iterable[String]): PipelineStarter = {
//      new PipelineStarter(new DataHolder[String](data))
//    }
//  }

  class DataHolder[In](data: Iterable[In]) {
    def getData() = data
  }

//  class PipelineStarter(dataHolder: DataHolder[String]) {
//    def start[A](action: String => A): PipelineBuilder[String, A] = {
//      new PipelineBuilder[String, A](dataHolder, action)
//    }
//  }

//  class PipelineBuilder[In, A](dataHolder: DataHolder[In], execution: In => A) {
//    def add[B](action: A => B): PipelineBuilder[In, B] = {
//      new PipelineBuilder[In, B](dataHolder, execution.andThen(action))
//    }
//    def complete[B](action: A => B): Pipeline[In, B] = {
//      new Pipeline[In, B](dataHolder, execution.andThen(action))
//    }
//  }

//  class PipelineFinisher[In, A](dataHolder: DataHolder[In], execution: In => A) {
//    def complete[B](action: A => B): Pipeline[In, B] = {
//      new Pipeline[In, B](dataHolder, execution.andThen(action))
//    }
//  }
//
//  class Pipeline[A, B](dataHolder: DataHolder[A], execution: A => B) {
//    def execute(): Unit = {
//      dataHolder.getData().foreach(url => println(execution.apply(url)))
////      execution.apply(in)
//    }
//  }

  trait Fetcher[Raw] {
    def fetch(url: String): Raw
  }

  trait Parser[Raw, Doc] {
    def parse(raw: Raw): Doc
  }

  object Crawler {
    def read(data: Iterable[String]): InitialBranchBuilder = {
      val q = new mutable.Queue[String]()
      q ++= data
//      new FetcherBuilder(new CrawlingQueue(data))
      new InitialBranchBuilder(new CrawlingQueue(data))
    }
  }

//  trait FetcherBranchBuilder {
//    def fetch[Raw](fetcher: String => Raw): ParserBranchBuilder[Raw]
//  }

//  trait Builder {
//
//  }

  class FetcherBranchBuilder(queue: CrawlingQueue, predicate: String => Boolean) {
    def fetch[Raw](fetcher: String => Raw): ParserBranchBuilder[Raw] = {
      new ParserBranchBuilder[Raw](queue, {case url if predicate.apply(url) => {println("FetcherBranchBuilder"); fetcher.apply(url)}})
    }
  }
  class SuccessiveFetcherBranchBuilder[Doc](queue: CrawlingQueue, predicate: String => Boolean, partial: PartialFunction[String, Doc]) {
    def fetch[Raw](fetcher: String => Raw): SuccessiveParserBranchBuilder[Raw, Doc] = {
      new SuccessiveParserBranchBuilder[Raw, Doc](queue, {case url if predicate.apply(url) => fetcher.apply(url)}, partial)
    }
  }
  class FinalFetcherBranchBuilder[Doc](queue: CrawlingQueue, partial: PartialFunction[String, Doc]) {
    def fetch[Raw](fetcher: String => Raw): FinalParserBranchBuilder[Raw, Doc] = {
      new FinalParserBranchBuilder[Raw, Doc](queue, {case url => fetcher.apply(url)}, partial)
    }
  }

  class ParserBranchBuilder[Raw](queue: CrawlingQueue, fetcher: PartialFunction[String, Raw]) {
    def parse[Doc](parser: Raw => Doc): SubsequentBranchBuilder[Doc] = {
      new SubsequentBranchBuilder[Doc](queue, fetcher.andThen(parser))
    }
  }
  class SuccessiveParserBranchBuilder[Raw, Doc](queue: CrawlingQueue, fetcher: PartialFunction[String, Raw], partial: PartialFunction[String, Doc]) {
    def parse(parser: Raw => Doc): SubsequentBranchBuilder[Doc] = {
      new SubsequentBranchBuilder[Doc](queue, partial.orElse(fetcher.andThen(parser)))
    }
  }
  class FinalParserBranchBuilder[Raw, Doc](queue: CrawlingQueue, fetcher: PartialFunction[String, Raw], partial: PartialFunction[String, Doc]) {
    def parse(parser: Raw => Doc): FinalBranchBuilder[Doc] = {
      new FinalBranchBuilder[Doc](queue, partial.orElse(fetcher.andThen(parser)))
    }
  }

  class SubsequentBranchBuilder[Doc](queue: CrawlingQueue, partialParser: PartialFunction[String, Doc]) {
    def when(predicate: String => Boolean): SuccessiveFetcherBranchBuilder[Doc] = {
      new SuccessiveFetcherBranchBuilder(queue, predicate, partialParser)
    }

    def otherwise(): FinalFetcherBranchBuilder[Doc] = {
      new FinalFetcherBranchBuilder[Doc](queue, partialParser)
    }
  }
  class FinalBranchBuilder[Doc](queue: CrawlingQueue, parser: String => Doc) {
    def write()(implicit writer: Doc => Unit): PipelineRunner = {
      new PipelineRunner(queue, parser.andThen(writer))
    }
  }

  class InitialBranchBuilder(queue: CrawlingQueue) {
    def fetch[Raw](fetcher: String => Raw): ParserBuilder[Raw] = {
      new FetcherBuilder(queue).fetch(fetcher)
    }

    def when(predicate: String => Boolean): FetcherBranchBuilder = {
      new FetcherBranchBuilder(queue, predicate)
    }

  }

  sealed trait Redirectable[Doc] extends Product {}

  sealed trait Redirect[Doc] extends Redirectable[Doc] {
    def target(): String
  }

  case class Direct[Doc](doc: Doc) extends Redirectable[Doc]
  case class HeaderRedirect[Doc](target: String) extends Redirect[Doc]
  case class MetaRedirect[Doc](target: String, doc: Doc) extends Redirect[Doc]

  class FetcherBuilder(queue: CrawlingQueue) {
    def fetch[Raw](fetcher: String => Raw): ParserBuilder[Raw] = {
      new ParserBuilder[Raw](queue, fetcher)
    }
  }

  class ParserBuilder[Raw](queue: CrawlingQueue, fetcher: String => Raw) {
    def parse[Doc](parser: Raw => Redirectable[Doc]): ProcessorProxyBuilder[Doc] = {
      new ProcessorProxyBuilder[Doc](queue, fetcher.andThen(parser))
    }
  }
//
//  trait RedirectHandlerBuilderInt[Doc] {
//    def followRedirect(deep: Int): WriterBuilder[Doc]
//  }
//
//  trait WriterBuilderInt[Doc] {
//    def write(processor: Redirectable[Doc] => Unit): PipelineRunner
//  }

  // TODO rename to proxy
  class ProcessorProxyBuilder[Doc](queue: CrawlingQueue, parser: String => Redirectable[Doc]) {
    def followRedirect(deep: Int): WriterBuilder[Doc] = {
      new RedirectHandlerBuilder[Doc](queue, parser: String => Redirectable[Doc]).followRedirect(deep)
    }

    def write(processor: Redirectable[Doc] => Unit): PipelineRunner = {
      new WriterBuilder[Doc](queue, parser).write(processor)
    }

  }


//  trait RedirectHandler {
//    def handle[A](redirectable: Redirectable[A], queue: CrawlingQueue): Redirectable[A]
//  }

  class RedirectHandlerBuilder[Doc](var queue: CrawlingQueue, parser: String => Redirectable[Doc]) {

    val a: (Redirectable[Doc]) => Redirectable[Doc] = {
      r =>
        r match {
          case d: Direct[Doc] => d
          case hr: HeaderRedirect[Doc] => {
            println("Add: " + hr.target)
            queue.add(hr.target)
            hr
          }
          case mr: MetaRedirect[Doc] => mr
        }
    }

    def followRedirect(deep: Int): WriterBuilder[Doc] = {
      new WriterBuilder[Doc](queue, parser.andThen(a))
    }
  }

  class WriterBuilder[Doc](queue: CrawlingQueue, redirectHandler: String => Redirectable[Doc]) {
    def write(processor: Redirectable[Doc] => Unit): PipelineRunner = {
      new PipelineRunner(queue, redirectHandler.andThen(processor))
    }
  }

  val executorService1 = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder().setDaemon(true).build)
  val executorService2 = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder().setDaemon(true).build)
  val executorService3 = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder().setDaemon(true).build)
  val globalExecutor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setDaemon(true).build)
  val globalExecutor1 = Executors.newFixedThreadPool(3, new ThreadFactoryBuilder().setDaemon(true).build)
  val ec1 = ExecutionContext.fromExecutor(globalExecutor1)

  class PipelineRunner(queue: CrawlingQueue, writer: String => Unit) {
    val moderator = new FixedDelayModerator(2000L)
    val map = Map(
      "example.com" -> (new FixedDelayModerator(3000L), ec1),
      "www.facebook.com" -> (new FixedDelayModerator(3000L), ec1),
      "google.com" -> (new FixedDelayModerator(3000L), ec1)
    )
    def crawl(): Unit = {
      val q = new FetcherQueue(2, 3000L)
      queue.foreach{
        url => {
//          println("step")
//          moderator.extract(url, writer.apply)
          q.fetch(new URL(url), writer.apply)
        }
      }
//      println("Finish")
    }
  }

  trait Fetcher1[Raw] {
    def fetch(url: String): Raw
  }

  class DefaultHttpFetcher()(implicit httpClient: CloseableHttpClient) extends Fetcher1[CloseableHttpResponse] {
    override def fetch(url: String): CloseableHttpResponse = {
      println("Fetch: " + url)
      val httpGet = new HttpGet(url)
      val response: CloseableHttpResponse = httpClient.execute(httpGet)
      response
    }
  }

  case class Prod[Body](code: Int, body: Body)

  class SimpleHttpResponseParser[Body](bodyParser: HttpEntity => Body) extends Parser[CloseableHttpResponse, Prod[Body]] {
    override def parse(response: CloseableHttpResponse): Prod[Body] = {
      val d = bodyParser.apply(response.getEntity)
      newProd(response, d)
    }

    private def newProd(response: CloseableHttpResponse, body: Body): Prod[Body] = {
      Prod(response.getStatusLine.getStatusCode, body)
    }
  }

  class HttpResponseParser[Body](bodyParser: HttpEntity => Redirectable[Body]) extends Parser[CloseableHttpResponse, Redirectable[Prod[Body]]] {
    override def parse(response: CloseableHttpResponse): Redirectable[Prod[Body]] = {
      response.getStatusLine.getStatusCode match {
        case 302 => HeaderRedirect("http://example.com")
        case _ => bodyParser.apply(response.getEntity) match {
          case d: Direct[Body] => Direct(newProd(response, d.doc))
          case r: MetaRedirect[Body] => MetaRedirect("http://example.com", newProd(response, r.doc))
        }
      }
    }

    private def newProd(response: CloseableHttpResponse, body: Body): Prod[Body] = {
      Prod(response.getStatusLine.getStatusCode, body)
    }
  }

  abstract class JSoupParser[A] {
    def parse(entity: HttpEntity): Redirectable[A] = {
      val document = Jsoup.parse(entity.getContent, ContentType.getOrDefault(entity).getCharset.toString, "http://example.com")
      Direct(parseDocument(document))
    }

    def parseDocument(document: Document): A
  }

  object KantParser extends JSoupParser[String] {
    override def parseDocument(document: Document): String = {
      document.title()
    }
  }

  object EquipParser extends JSoupParser[Int] {
    override def parseDocument(document: Document): Int = {
      document.siblingIndex()
    }
  }

  case class Holder(title: String)

  implicit val httpClient = HttpClients.createDefault()

  def examplePredicate(url: String): Boolean = {if (url.startsWith("http://example.com")) true else false}
  def facebookPredicate(url: String): Boolean = {if (url.startsWith("https://www.facebook.com")) true else false}
  def googlePredicate(url: String): Boolean = {if (url.startsWith("https://google.com")) true else false}


  implicit def write(doc: Product): Unit = {
    println(doc)
//    println(doc)
  }
//  new FinalBranchBuilder[Direct[String]](null, null).write()
//  new Direct[String]("str").productIterator.foreach(println)

  it should ".." in {
    println("!")
    Crawler.read(
      Seq(
        "http://example.com?a=1", "http://example.com?a=2", "http://example.com?a=3", "http://example.com?a=4",
        "https://www.facebook.com/?a=1",
        "https://google.com?a=1", "https://google.com?a=2"
      ))
      .when(examplePredicate)
        .fetch(new DefaultHttpFetcher().fetch)
        .parse(new HttpResponseParser(KantParser.parse).parse)
      .when(facebookPredicate)
        .fetch(new DefaultHttpFetcher().fetch)
        .parse(new HttpResponseParser(KantParser.parse).parse)
      .otherwise
        .fetch(new DefaultHttpFetcher().fetch)
        .parse(new HttpResponseParser(KantParser.parse).parse)
//        .write(u => {println(s"${System.currentTimeMillis()} ${u}")})
        .write()
        .crawl()


//    Crawler.read(
//      Seq(
//        "http://example.com?a=1", "http://example.com?a=2", "http://example.com?a=3", "http://example.com?a=4",
//        "https://www.facebook.com/?a=1",
//        "https://google.com?a=1", "https://google.com?a=2"
//      ))
//      .fetch(new DefaultHttpFetcher().fetch)
//      .parse(new HttpResponseParser(KantParser.parse).parse)
//      .followRedirect(1)
//      .write(u => {println(s"${System.currentTimeMillis()} ${u}")})
//      .crawl()

    Thread.sleep(25000L)
    case class A(a: Int, b: Int)
//    val p: Serializable = A(1, 2)

  }

}
