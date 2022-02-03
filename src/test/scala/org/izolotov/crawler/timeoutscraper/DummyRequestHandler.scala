package org.izolotov.crawler.timeoutscraper

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import DummyRequestHandler._

object DummyRequestHandler {
  val DummyContent = "Dummy page"
}

class DummyRequestHandler extends AbstractHandler {
  override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    response.setContentType("text/plain;charset=utf-8")
    response.getWriter.print(DummyContent)
    response.setStatus(HttpServletResponse.SC_OK)
    baseRequest.setHandled(true)
  }
}
