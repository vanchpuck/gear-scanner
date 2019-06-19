package org.izolotov.crawler

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}

import org.apache.commons.cli.{BasicParser, Options}
import org.apache.spark.sql.SparkSession

object ProductCrawlerApp {

  val UrlsPathArgKey = "urls-path"
  val UserAgentArgKey = "user-agent"
  val FetcherDelayArgKey = "fetcher-delay"
  val FetcherTimeoutArgKey = "fetcher-timeout"
  val PartitionsNumber = "partitions-number"
  val CrawledOutputPathArgKey = "crawled-output-path"
  val CrawledOutputFormatArgKey = "crawled-output-format"
  val ElasticNodesArgKey = "elastic-nodes"
  val ElasticAuthUser = "elastic-user"
  val ElasticAuthPassword = "elastic-password"

  lazy implicit val Spark = SparkSession.builder
    .appName("SiteMap crawler app")
    .getOrCreate()

  // TODO handle the case when several urls point to single item
  def main(args: Array[String]): Unit = {
    import org.elasticsearch.spark.sql._
    import Spark.implicits._
    val options = new Options
    options.addOption("u", UrlsPathArgKey, true, "Path to file containing URL for crawling")
    options.addOption("a", UserAgentArgKey, true, "User agent string")
    options.addOption("d", FetcherDelayArgKey, true, "Fetcher delay in milliseconds")
    options.addOption("t", FetcherTimeoutArgKey, true, "Fetcher timeout in milliseconds")
    options.addOption("p", PartitionsNumber, true, "Number of partitions the URLs will be partitioned on before being crawled")
    options.addOption("c", CrawledOutputPathArgKey, true, "Crawled data output path")
    options.addOption("f", CrawledOutputFormatArgKey, true, "Crawled data output format")
    options.addOption("n", ElasticNodesArgKey, true, "List of Elasticsearch nodes to connect to")
    options.addOption("U", ElasticAuthUser, true, "Elasticsearch user name")
    options.addOption("P", ElasticAuthPassword, true, "Elasticsearch password")
    val parser = new BasicParser
    val cmd = parser.parse(options, args)

    val currTimestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
    val outPath = s"${cmd.getOptionValue(CrawledOutputPathArgKey)}/${currTimestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}"

    val urls = Spark.read
      .option("delimiter", "\t")
      .option("header", true)
      .csv(cmd.getOptionValue(UrlsPathArgKey)).as[UncrawledURL]
    val crawled  = new ProductCrawler(
      Option(cmd.getOptionValue(PartitionsNumber)).getOrElse("1").toInt,
      cmd.getOptionValue(UserAgentArgKey),
      cmd.getOptionValue(FetcherTimeoutArgKey).toLong,
      cmd.getOptionValue(FetcherDelayArgKey).toLong
    )
      .crawl(urls)
      .persist()

    crawled.write
      .format(cmd.getOptionValue(CrawledOutputFormatArgKey))
      .save(outPath)

    val esConf = Seq(
      "es.nodes" -> cmd.getOptionValue(ElasticNodesArgKey),
      "es.nodes.wan.only" -> "true",
      "es.mapping.id" -> "url"
    ) ++
      Option(cmd.getOptionValue(ElasticAuthUser)).map(user => ("es.net.http.auth.user" -> user)) ++
      Option(cmd.getOptionValue(ElasticAuthPassword)).map(password => ("es.net.http.auth.pass" -> password))

    // TODO consider duplicates
    crawled
      .filter($"httpCode" === 200 && $"fetchError".isNull && $"document.parseError".isNull)
      .select("document.*")
      .saveToEs(
        "gear/products",
        Map(esConf: _*)
      )
  }

}
