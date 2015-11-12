package org.nexu.events.event

import java.util.UUID

import org.nexu.events.domain.{Calendar, Aggregate}

/**
 * Created by cyril on 20.09.15.
 */
class CalendarCreated(calendar: Calendar) extends Event {
  override def getId: UUID = UUID.randomUUID()

  override def getAggregate = calendar
}
