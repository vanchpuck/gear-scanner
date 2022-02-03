package org.izolotov.crawler.v4

import scala.concurrent.Future
// TODO approach with implicit queue-decorator
abstract class QueuedSelector[Attempt]()(implicit queue: DataSource) extends Selector[Attempt] {
  def extract(url: String): Attempt

  def extractF(url: String): Future[Attempt] = {
    null
  }
}
