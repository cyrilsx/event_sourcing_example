package org.nexu.events.command


import org.nexu.events.domain.{Aggregate, Meeting, Calendar}


case class CreateMeeting(meeting: Meeting, calendarId: Long) extends Command {
  override def isValid: Unit = meeting.isValid

  override def targetAggregateId: Long = calendarId

  override def targetClassAggregate: Class[_ <: Aggregate] = classOf[Calendar]

}
