package org.nexu.events.command

import org.nexu.events.domain.{Calendar, Aggregate, Meeting, User}

class InvitePerson(user: User, meeting: Meeting, calendarId: String) extends Command {
  override def isValid = {
    meeting.isValid
    user.isValid
  }

  override def targetAggregateId: String = calendarId

  override def targetClassAggregate: Class[_ <: Aggregate] = classOf[Calendar]
}
