package org.izolotov.crawler.parser.product

import java.nio.charset.Charset

import org.scalatest.FlatSpec

class JsonParserSpec extends FlatSpec {

  behavior of "Json product parser"

  it should "parse valid json" in {
    val actual = JsonParser.parse(
      "https://alpindustria.ru/petzl-d-lynx",
      getClass.getClassLoader.getResourceAsStream("parser/product/json-parser/alpindustria-petzl-d-lynx.json"),
      Charset.forName("UTF-8")
    )
    val expected = new Product(
      url = "https://alpindustria.ru/petzl-d-lynx",
      store = "alpindustria.ru",
      brand = Some("Petzl"),
      name = Some("Кошки Petzl D-Lynx"),
      category = Array("Альпинистское снаряжение", "Кошки и снегоступы"),
      price = Some(13970),
      oldPrice = None,
      currency = Some("Руб.")
    )
    assert(expected == actual)
  }

  it should "Use the None value for the absent fields" in {
    val actual = JsonParser.parse(
      "https://tramontana.ru/petzl-lynx",
      getClass.getClassLoader.getResourceAsStream("parser/product/json-parser/tramontana-petzl-lynx-no-currency.json"),
      Charset.forName("UTF-8")
    )
    assert(actual.url == "https://tramontana.ru/petzl-lynx")
    assert(actual.store == "tramontana.ru")
    // The currency field is absent
    assert(actual.currency.isEmpty)
  }

}
