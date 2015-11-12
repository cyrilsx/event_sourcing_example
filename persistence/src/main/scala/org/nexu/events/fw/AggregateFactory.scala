package org.nexu.events.fw

import org.nexu.events.domain.Aggregate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Build an aggregate from events store in db.
 */
object AggregateFactory {

  val eventStore = new EventStore

  def buildAggregate(clazz: Class[_ <: Aggregate], aggregateId: String) : Future[Aggregate] = {
    eventStore.findAll(aggregateId).map(
      eventsInDb => {
        var aggregrate: Aggregate = clazz.newInstance()
        eventsInDb.foreach(event => aggregrate = aggregrate.replay(event))
        aggregrate
      }
    )
  }

}
