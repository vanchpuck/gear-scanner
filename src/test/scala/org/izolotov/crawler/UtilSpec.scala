package org.izolotov.crawler

import org.scalatest.FlatSpec

class UtilSpec extends FlatSpec {

  behavior of "Price parser"

  it should "fail is the price can't be parsed" in {
    assertThrows[NumberFormatException](Util.parsePrice("illegal price value"))
  }
}
