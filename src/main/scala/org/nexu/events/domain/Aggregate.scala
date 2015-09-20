package org.nexu.events.domain

import org.nexu.events.command.Command
import org.nexu.events.event.Event


trait Aggregate {

  def onCommand: PartialFunction[Command, Event]

  def replay(event: Event): Aggregate

  def getAggregateId: String

  def getVersion: Long

}
