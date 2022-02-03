package org.izolotov.crawler

import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.scalatest.flatspec.AnyFlatSpec

class HtmlUnitSpec extends AnyFlatSpec {

  it should "..." in {
    import com.gargoylesoftware.htmlunit.BrowserVersion
    import com.gargoylesoftware.htmlunit.WebClient
    import com.gargoylesoftware.htmlunit.WebResponse
    val webClient = new WebClient(BrowserVersion.FIREFOX)
    webClient.getOptions.setJavaScriptEnabled(true)
    webClient.getOptions.setRedirectEnabled(true)
    webClient.getOptions.setThrowExceptionOnScriptError(false)
    webClient.getOptions.setCssEnabled(true)
    val page = webClient.getPage("https://arcteryx.com/ca/en/c/mens/shell-jackets").asInstanceOf[HtmlPage]
    val response = page.getWebResponse
    val content = response.getContentAsString
    println(content.contains("ZETA SL"))
//    println(content)
  }

}
