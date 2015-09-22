package org.nexu.events.command

import java.util.Objects

import org.nexu.events.domain.{User, Calendar, Aggregate}

/**
 *
 * @param calendarId mandatory
 */
case class CreateCalendar(calendarId: String, user: User) extends Command {

  override def isValid: Unit = {
    Objects.requireNonNull(calendarId, "an unique is mandatory to create a calendar")
  }

  override def targetAggregateId: String = calendarId

  override def targetClassAggregate: Class[_ <: Aggregate] = classOf[Calendar]
}
