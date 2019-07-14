package org.izolotov

package object crawler {

  case class UncrawledURL(url: String)

  case class HostURL(url: String, host: String)

}
