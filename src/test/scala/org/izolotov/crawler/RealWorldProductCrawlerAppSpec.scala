package org.izolotov.crawler

import java.nio.file.{Files, Paths}

import com.holdenkarau.spark.testing.{DataFrameSuiteBase, Utils}
import org.scalatest.FlatSpec

class RealWorldProductCrawlerAppSpec extends FlatSpec with DataFrameSuiteBase {

  // For debug purposes
  ignore should "crawl the real world URLs" in {
    import spark.implicits._
    val outputDir = s"${Utils.createTempDir().getPath}/output"
    val errorsDir = s"${Utils.createTempDir().getPath}/errors"
    ProductCrawlerApp.main(Array(
      "--urls-path", this.getClass.getClassLoader.getResource("product-crawler-app/input-urls/real-world-urls.csv").getPath,
      "--user-agent", "NoNameYetBot/0.1",
      "--fetcher-delay", "10",
      "--fetcher-timeout", "5000",
      "--crawled-output-path", outputDir,
      "--errors-output-path", errorsDir,
      "--table-region", "us-east-2"
    ))
    val outputPath: String = Files.list(Paths.get(outputDir)).iterator().next().toString
    val result = spark.read.parquet(outputPath).select($"url", $"fetchError", $"document.parseError")
    result.show()
  }

}
