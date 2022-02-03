package org.izolotov.crawler.parser.product

import java.io.File

import org.izolotov.crawler.parser.product.BackcountryParserSpec.ResourceDir
import org.jsoup.Jsoup
import org.scalatest.flatspec.AnyFlatSpec

class Testtt extends AnyFlatSpec{

  it should "sdf" in {
    println("!!!")
    val f = new File("/media/izolotov/f163581d-53e6-4529-80d8-b822a479c7ab/dev/gear-scanner/src/test/resources/parser/product/backcountry-parser/full-price.html")
    val doc = Jsoup.parse(f, "UTF-8")
//    println(doc)
    println(doc.getElementsByClass("qa-brand-name"))

  }

}
