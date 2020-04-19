package org.izolotov.crawler

import com.typesafe.scalalogging.Logger
import org.json4s.jackson.Serialization.{read, write}
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model._
import scala.collection.JavaConverters._

import SQSQueue._

object SQSQueue {
  private val Log = Logger[SQSQueue[_]]
  case class RedrivePolicy(maxReceiveCount: Int, deadLetterTargetArn: String)
}

class SQSQueue[A <: AnyRef](client: SqsClient,
                            queueName: String,
                            dlQueueName: String = null,
                            waitTimeSeconds: Int = 0)
                           (implicit formats: org.json4s.Formats,
                            manifest: scala.reflect.Manifest[A]) extends ProcessingQueue[A] {

  private val queueURL = client.getQueueUrl(GetQueueUrlRequest.builder.queueName(queueName).build).queueUrl()

  Option(dlQueueName).foreach{
    queue =>
      Log.info(s"Configuring the dead letter queue $queue for the $queueName queue")
      val dlQueueURL = client.getQueueUrl(GetQueueUrlRequest.builder.queueName(queue).build).queueUrl()
      val attrsRequest = GetQueueAttributesRequest.builder().attributeNames(QueueAttributeName.QUEUE_ARN).queueUrl(dlQueueURL).build()
      val dlQueueArn = client.getQueueAttributes(attrsRequest).attributes.asScala(QueueAttributeName.QUEUE_ARN)
      val request = SetQueueAttributesRequest.builder
        .queueUrl(queueURL)
        .attributes(Map(QueueAttributeName.REDRIVE_POLICY-> write(RedrivePolicy(1, dlQueueArn))).asJava)
        .build()
      client.setQueueAttributes(request)
  }

  def add(message: A): Unit = {
    Log.info(s"Sending to the '$queueName' SQS queue: $message")
    client.sendMessage(SendMessageRequest.builder()
      .queueUrl(queueURL)
      .messageBody(write(message)(formats))
      .build())
  }

  def pull(numOfMessages: Int = 10): Iterable[A] = {
    Log.info(s"Pulling messages from the '$queueName' SQS queue")
    val receiveMessageRequest = ReceiveMessageRequest.builder()
      .queueUrl(queueURL)
      .waitTimeSeconds(waitTimeSeconds)
      .maxNumberOfMessages(numOfMessages)
      .build()
    // TODO handle failed messages (Dead letter queue maybe)
    // TODO add logging
    client.receiveMessage(receiveMessageRequest).messages.asScala
      .map(message => (message, read[A](message.body())(formats, manifest)))
      .map{
        pair =>
          client.deleteMessage(DeleteMessageRequest.builder.queueUrl(queueURL).receiptHandle(pair._1.receiptHandle()).build())
          pair._2
      }
  }

}
