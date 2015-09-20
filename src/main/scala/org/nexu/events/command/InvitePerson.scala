package org.nexu.events.command

import org.nexu.events.domain.{Aggregate, Meeting, User}

class InvitePerson(user: User, meeting: Meeting, calendarId: Long) extends Command {
  override def isValid: Unit = {
    meeting.isValid
    user.isValid
  }

  override def targetAggregateId: Long = calendarId

  override def targetClassAggregate: Class[_ <: Aggregate] = ???
}
