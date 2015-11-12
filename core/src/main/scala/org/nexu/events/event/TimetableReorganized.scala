package org.nexu.events.event

import java.util.UUID

import org.nexu.events.domain.{Calendar, Aggregate}

/**
 *
 * @param calendarImpacted
 */
class TimetableReorganized(calendarImpacted: Calendar) extends Event{
  override def getId: UUID = UUID.randomUUID()

  override def getAggregate: Aggregate = calendarImpacted
}
