package org.nexu.events.command

import akka.actor.{ActorRef, Props, ActorSystem}
import org.nexu.events.event.{EventNotifier, Event}
import org.nexu.events.fw.{EventStore, AggregateFactory}
import scala.concurrent.ExecutionContext.Implicits.global
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

  implicit val system = ActorSystem("event-system")

  /** Notify event for readers */
  private val eventNotifier: ActorRef = system.actorOf(Props.apply(classOf[EventNotifier]))

  /**
   *
   * @param command
   * @return Future of generated event
   */
  def sendCommand[T <: Command](command: T) : Future[Event] = {
    val aggregate = aggregateFactory.buildAggregate(command.targetClassAggregate, command.targetAggregateId)
    aggregate.map(aggregate => {
      val event = aggregate.onCommand.apply(command)
      eventStore.store(event)
      eventNotifier ! event
      event
    })
  }

}
