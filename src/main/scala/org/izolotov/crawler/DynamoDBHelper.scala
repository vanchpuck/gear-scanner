package org.izolotov.crawler

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder
import com.typesafe.scalalogging.Logger
import org.scanamo.{DynamoFormat, Scanamo, Table}

import DynamoDBHelper._

object DynamoDBHelper {
  private val Log = Logger[DynamoDBHelper]
}

class DynamoDBHelper(tableName: String, region: String) {

  private val client = AmazonDynamoDBAsyncClientBuilder.standard().withRegion(region).withCredentials(new ProfileCredentialsProvider()).build()
  private val scanamo = Scanamo(client)

  def save[A](data: A)(implicit format: DynamoFormat[A]): Unit = {
    val table = new Table[A](tableName)(format)
    Log.info(s"Saving in DynamoDB: $data")
    scanamo.exec{
      table.put(data)
    }
  }
}
