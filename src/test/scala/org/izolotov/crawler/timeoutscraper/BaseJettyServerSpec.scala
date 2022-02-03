//package org.izolotov.crawler.timeoutscraper
//
//import org.eclipse.jetty.server.Server
//import org.eclipse.jetty.server.handler.AbstractHandler
//import org.izolotov.crawler.DelayFetcherTest
//import org.izolotov.crawler.timeoutscraper.FixedDelayModeratorSpec.Port
//import org.scalatest.{BeforeAndAfter, FlatSpec}
//
//class BaseJettyServerSpec(handler: AbstractHandler) extends FlatSpec with BeforeAndAfter {
//
//  var server = new Server()
//
//  before {
//    server.setHandler(handler)
//    server.start()
//  }
//
//  after {
//    server.stop()
//  }
//
//}
