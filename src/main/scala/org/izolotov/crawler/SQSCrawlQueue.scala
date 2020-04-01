package org.izolotov.crawler

import com.typesafe.scalalogging.Logger
import org.izolotov.crawler.SQSCrawlQueue._
import org.json4s.jackson.Serialization.{read, write}
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.{DeleteMessageRequest, GetQueueUrlRequest, ReceiveMessageRequest, SendMessageRequest}

import scala.collection.JavaConverters._

object SQSCrawlQueue {
  private val Log = Logger[SQSCrawlQueue]
}

class SQSCrawlQueue(client: SqsClient, queueName: String, waitTimeSeconds: Int = 0) extends CrawlQueue {

  private implicit val Formats = org.json4s.DefaultFormats

  private val queueURL = client.getQueueUrl(GetQueueUrlRequest.builder.queueName(queueName).build).queueUrl()

  def add(message: CrawlQueueRecord): Unit = {
    client.sendMessage(SendMessageRequest.builder()
      .queueUrl(queueURL)
      .messageBody(write(message))
      .build())
  }

  def pull[A](numOfMessages: Int = 10): Iterable[CrawlQueueRecord] = {
    Log.info("Pulling messages from SQS")
    val receiveMessageRequest = ReceiveMessageRequest.builder()
      .queueUrl(queueURL)
      .waitTimeSeconds(waitTimeSeconds)
      .maxNumberOfMessages(numOfMessages)
      .build()
    // TODO handle failed messages (Dead letter queue maybe)
    // TODO add logging
    client.receiveMessage(receiveMessageRequest).messages.asScala
      .map(message => (message, read[CrawlQueueRecord](message.body())))
      .map{
        pair =>
          client.deleteMessage(DeleteMessageRequest.builder.queueUrl(queueURL).receiptHandle(pair._1.receiptHandle()).build())
          pair._2
      }
  }

}
