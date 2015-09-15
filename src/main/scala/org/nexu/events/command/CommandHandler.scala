package org.nexu.events.command

import com.websudos.phantom.dsl.ResultSet
import org.nexu.events.event.Event
import org.nexu.events.fw.{EventStore, AggregateFactory}

import scala.concurrent.Future

/**
 * Command Handler receives Command and apply it to the aggregate.
 * Event is generated from the aggregate and store in nosql db (cassandra)
 * Then event is publish to the event bus for further processing is needed.
 */
class CommandHandler {

  /** Build the aggregate */
  private val aggregateFactory = AggregateFactory
  /** Store event*/
  private val eventStore = new EventStore

  /**
   *
   *
   * @param command
   * @return Future of generated event
   */
  def sendCommand(command: _ <: Command) : Future[Event] = {
    val aggregate = aggregateFactory.buildAggregate(command.targetClassAggregate, command.targetAggregateId)
    aggregate.map(aggregate => {
      val event = aggregate.onCommand.apply(command)
      eventStore.store(event)
      event
    })
  }

}
