package org.izolotov.crawler

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.scalatest.FlatSpec

class SiteMapCrawlerAppSpec extends FlatSpec with DataFrameSuiteBase{

  // SiteMapCrawlerApp is not ready for the full test
  ignore should "say hello" in {
    SiteMapCrawlerApp.main(Array(
      this.getClass.getClassLoader.getResource("sitemap-crawler-app/input-urls.csv").getPath,
      "/tmp/crampons-alpindustria",
      "NoNameYetBot/0.1",
      "2000"
    ))
  }

}
