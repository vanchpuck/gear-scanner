package org.izolotov.crawler.v4

import java.net.URL

import org.openqa.selenium.WebDriver

class BlackDiamondParser extends Parser[WebDriver, Int] {
  override def parse(url: URL, response: WebDriver): Int = {
    1
  }
}
