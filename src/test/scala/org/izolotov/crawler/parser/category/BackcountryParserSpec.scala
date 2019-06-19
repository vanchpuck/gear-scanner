package org.izolotov.crawler.parser.category

import java.nio.charset.Charset

import org.scalatest.FlatSpec

class BackcountryParserSpec extends FlatSpec {

  behavior of "Backcountry product category parser"

  val parser = BackcountryParser
  val categoryDir = "parser/category/backcountry-parser"

  it should "extract next URL if one exist" in {
    val category = parser.parse(
      "http://localhost/has-next",
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL.get.toString == "http://localhost/mountaineering-crampons?page=1")
  }

  it should "not extract next URL if one doesn't exist" in {
    val category = parser.parse(
      "http://localhost/has-not-next",
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    assert(category.nextURL == None)
  }

  it should "extract URLs" in {
    val category = parser.parse(
      "http://localhost/has-not-next",
      this.getClass.getClassLoader.getResourceAsStream(s"$categoryDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    val expected = Seq(
      "http://localhost/petzl-antisnow-leopard?skid=PTZ005U-ONECOL-ONESIZ&ti=UExQIENhdDpNb3VudGFpbmVlcmluZyBDcmFtcG9uczozOjE6YmMtbW91bnRhaW5lZXJpbmctY3JhbXBvbnM=",
      "http://localhost/petzl-sarken-front-section?skid=PTZ004P-ONECOL-ONESIZ&ti=UExQIENhdDpNb3VudGFpbmVlcmluZyBDcmFtcG9uczozOjI6YmMtbW91bnRhaW5lZXJpbmctY3JhbXBvbnM=",
      "http://localhost/grivel-g14-set-alu-spacers-and-bolts?skid=GRV0116-ONECOL-ONESIZ&ti=UExQIENhdDpNb3VudGFpbmVlcmluZyBDcmFtcG9uczozOjM6YmMtbW91bnRhaW5lZXJpbmctY3JhbXBvbnM=",
      "http://localhost/petzl-telemark-toe-bail-wire?skid=PTZ0161-SS-ONSI&ti=UExQIENhdDpNb3VudGFpbmVlcmluZyBDcmFtcG9uczozOjQ6YmMtbW91bnRhaW5lZXJpbmctY3JhbXBvbnM=",
      "http://localhost/kahtoola-connect-gaiter-low?skid=KHT000E-GRE-LXL&ti=UExQIENhdDpNb3VudGFpbmVlcmluZyBDcmFtcG9uczozOjU6YmMtbW91bnRhaW5lZXJpbmctY3JhbXBvbnM=",
      "http://localhost/grivel-super-asymmetric-bar?skid=GRV0092-ONECOL-S160MM&ti=UExQIENhdDpNb3VudGFpbmVlcmluZyBDcmFtcG9uczozOjY6YmMtbW91bnRhaW5lZXJpbmctY3JhbXBvbnM=",
      "http://localhost/grivel-g10-air-tech-crampon-spare-parts?skid=GRV0109-ONECOL-COMBACX2&ti=UExQIENhdDpNb3VudGFpbmVlcmluZyBDcmFtcG9uczozOjc6YmMtbW91bnRhaW5lZXJpbmctY3JhbXBvbnM="
    )
    assert(category.productURLs.map(url => url.get.toString) == expected)
  }

}
