package org.izolotov.crawler

import org.scalatest.flatspec.AnyFlatSpec

class UtilSpec extends AnyFlatSpec {

  behavior of "Price parser"

  it should "fail is the price can't be parsed" in {
    assertThrows[NumberFormatException](Util.parsePrice("illegal price value"))
  }
}
