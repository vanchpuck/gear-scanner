package org.izolotov.crawler

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder
import org.scanamo.{DynamoFormat, Scanamo, Table}

class DynamoDBHelper(tableName: String, region: String) {

  private val client = AmazonDynamoDBAsyncClientBuilder.standard().withRegion(region).withCredentials(new ProfileCredentialsProvider()).build()
  private val scanamo = Scanamo(client)

  def save[A](data: A)(implicit format: DynamoFormat[A]): Unit = {
    val table = new Table[A](tableName)(format)
    scanamo.exec{
      table.put(data)
    }
  }
}
