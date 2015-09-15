package org.nexu.events.fw

import org.nexu.events.domain.Aggregate

import scala.concurrent.Future

/**
 * Created by cyril on 12.09.15.
 */
object AggregateFactory {

  val eventStore = new EventStore

  def buildAggregate(clazz: Class[_ <: Aggregate], aggregateId: Long) : Future[Aggregate] = {
    eventStore.findAll(aggregateId).collect().map(
      eventsInDb => {
        val aggregrate: Aggregate = clazz.newInstance()
        eventsInDb.foreach(event => aggregrate = aggregrate.replay(event.jsonEventObject))
        aggregrate
      }
    )
  }

}
