package org.izolotov.crawler.parser.origin

import java.net.URL
import java.nio.charset.Charset

import org.scalatest.FlatSpec

class GrivelParserSpec extends FlatSpec {

  val parser = GrivelParser
  val originalDir = "parser/origin/grivel"

  it should "extract product names and next URL" in {
    val original = parser.parse(
      new URL("http://localhost/urls"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-next.html"),
      Charset.forName("UTF-8")
    )
    val expectedProducts = Seq(
      OriginProduct("grivel", "G1", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/PHL_RAG01A04F_crampons_G1_NC_2013_600x.png?v=1569299440"),
      OriginProduct("grivel", "G10", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/PHL_RA072A04F_crampons_G10_NC_2013_600x.png?v=1569300667"),
      OriginProduct("grivel", "G10 wide", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_g10_wide_nc_600x.png?v=1569314363"),
      OriginProduct("grivel", "Air Tech", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_at_nm_600x.png?v=1569314378"),
      OriginProduct("grivel", "G12", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_g12_nm_600x.png?v=1569314422"),
      OriginProduct("grivel", "Air Tech Light", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_at_light_nm_600x.png?v=1569314398"),
      OriginProduct("grivel", "Air Tech Light Wide", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_at_light_nc_wide_600x.png?v=1569314401"),
      OriginProduct("grivel", "Ski Tour", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_skitour_600x.png?v=1569314461"),
      OriginProduct("grivel", "Ski Tour New Matic", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_skitour_nm_600x.png?v=1569314461"),
      OriginProduct("grivel", "Haute Route", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_hauteroute_600x.png?v=1569314462"),
      OriginProduct("grivel", "Ski Race", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_ski_race_600x.png?v=1569314463"),
      OriginProduct("grivel", "G14", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_g14nm2020_600x.png?v=1591793071"),
      OriginProduct("grivel", "Rambo 4", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_rambo_4_600x.png?v=1569314454"),
      OriginProduct("grivel", "G20 Plus", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_g20pluspuntenere_600x.png?v=1591627946"),
      OriginProduct("grivel", "G22 Plus", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_g22plusnere_600x.png?v=1591626953"),
      OriginProduct("grivel", "Racing", "http://cdn.shopify.com/s/files/1/0030/4044/4451/products/crampons_racing_600x.png?v=1569314459")
    )
    assert(original.products.toSet == expectedProducts.toSet)
    assert(original.nextURL == Some(new URL("http://localhost/collections/crampons?page=2")))
  }

  it should "not extract next URL if one doesn't exist" in {
    val original = parser.parse(
      new URL("http://localhost/has-not-next"),
      this.getClass.getClassLoader.getResourceAsStream(s"$originalDir/has-not-next.html"),
      Charset.forName("UTF-8")
    )
    assert(original.nextURL == None)
  }

}
