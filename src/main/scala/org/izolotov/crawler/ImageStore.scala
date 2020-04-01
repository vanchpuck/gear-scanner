package org.izolotov.crawler

import com.amazonaws.services.s3.model.ObjectMetadata
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{PutObjectRequest, PutObjectResponse}

class ImageStore(bucketArn: String) {

  val s3 = S3Client.builder.build

  def upload(key: String, data: Array[Byte]): PutObjectResponse = {
    val metadata = new ObjectMetadata()
    metadata.setContentLength(data.length)
    val request: PutObjectRequest = PutObjectRequest.builder().bucket(bucketArn).key(key).build()
    val body: RequestBody = RequestBody.fromBytes(data)
    s3.putObject(request, body)
  }

}
