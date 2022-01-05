package org.izolotov.crawler.v4

import org.apache.http.HttpEntity
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.FlatSpec

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class UtilV3 extends FlatSpec {

  case class QueueItem(url: String, depth: Int)

  class CrawlingQueue(data: Iterable[String]) extends Iterator[String]{
    val queue: mutable.Queue[String] = mutable.Queue()
    queue ++= data

    def add(url: String): Unit = {
      queue += url
    }

    override def hasNext: Boolean = {
      println("hasNext")
      queue.length > 0
    }

    override def next(): String = {
      println("next")
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

  trait HttpResponse[A] {
    def protocolVersion(): String

    def statusCode(): Int

    def statusText(): String

    def headers(): Map[String, Iterable[String]]

    def body(): A
  }

  case class HttpResponseImpl[A](protocolVersion: String, statusCode: Int, statusText: String, headers: Map[String, Iterable[String]], body: A) extends HttpResponse[A]

  trait Fetcher[Raw] {
    def fetch(url: String): Raw
  }

  trait Parser[Raw, Doc] {
    def parse(raw: Raw): Doc
  }

  object Crawler {
    def read(data: Iterable[String]): FetcherBuilder = {
      val q = new mutable.Queue[String]()
      q ++= data
      new FetcherBuilder(new CrawlingQueue(data))
    }
  }

  sealed trait Redirectable[Doc]{

  }

  sealed trait Redirect[Doc] extends Redirectable[Doc] {
    def target(): String
  }

  case class Direct[Doc](doc: Doc) extends Redirectable[Doc]
  case class HeaderRedirect[Doc](target: String) extends Redirect[Doc]
  case class MetaRedirect[Doc](target: String, doc: Doc) extends Redirect[Doc]

  class FetcherBuilder(queue: CrawlingQueue) {
    def fetch[Raw](fetcher: Fetcher[Raw]): ParserBuilder[Raw] = {
      new ParserBuilder[Raw](queue, fetcher.fetch)
    }
  }

  class ParserBuilder[Raw](queue: CrawlingQueue, fetcher: String => Raw) {
//    def parse[Doc](parser: Parser[Raw, Redirectable[Doc]]): ProcessorBuilder[Doc] = {
    def parse[Doc](parser: Parser[Raw, Redirectable[Doc]]): RedirectHandlerBuilder[Doc] = {
      new RedirectHandlerBuilder[Doc](queue, fetcher.andThen(parser.parse))
    }
  }

  trait RedirectHandler {
    def handle[A](redirectable: Redirectable[A], queue: CrawlingQueue): Redirectable[A]
  }

//  object DefaultRedirectHandler extends RedirectHandler {
//    def handle[A](redirectable: Redirectable[A], queue: CrawlingQueue): Redirectable[A] = {
//      redirectable match {
//        case d: Direct[A] => d
//        case hr: HeaderRedirect[A] => {
//          println("Add: " + hr.target)
//          queue.add(hr.target)
//          hr
//        }
//        case mr: MetaRedirect[A] => mr
//      }
//    }
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

    def followRedirect(deep: Int): ProcessorBuilder[Doc] = {
      new ProcessorBuilder[Doc](queue, parser.andThen(a))
    }
  }

  class ProcessorBuilder[Doc](queue: CrawlingQueue, redirectHandler: String => Redirectable[Doc]) {
    def process(processor: Redirectable[Doc] => Unit): PipelineRunner = {
      new PipelineRunner(queue, redirectHandler.andThen(processor))
    }
  }

  class PipelineRunner(queue: CrawlingQueue, writer: String => Unit) {
    def crawl(): Unit = {
      queue.foreach{
        println("step")
        url => writer.apply(url)
      }
    }
  }

  class DefaultHttpFetcher()(implicit httpClient: CloseableHttpClient) extends Fetcher[CloseableHttpResponse] {
    override def fetch(url: String): CloseableHttpResponse = {
      println("Fetch: " + url)
      val httpGet = new HttpGet(url);
      val response: CloseableHttpResponse = httpClient.execute(httpGet)
      response
    }
  }

  case class Prod[Body](code: Int, body: Body)

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

  case class Holder(title: String)

  implicit val httpClient = HttpClients.createDefault()

  it should ".." in {
    Crawler.read(Seq("http://example.com"))
      .fetch(new DefaultHttpFetcher())
      .parse(new HttpResponseParser(KantParser.parse))
      .followRedirect(1)
      .process(u => println(u))
      .crawl()
  }

}
