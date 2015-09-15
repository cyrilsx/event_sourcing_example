package org.nexu.events.domain


import org.nexu.events.command.CreateMeeting
import org.nexu.events.event.{Event, MeetingCreated}

class Calendar(owner: User, timetable: List[Meeting]) extends Aggregate {
  def onCommand = {
    case app: CreateMeeting => createAppointment(app)
  }


  def createAppointment(meetingToCreate: CreateMeeting): Event = {
    val overlappingMeeting: List[Meeting] = findConflitingMeeting(meetingToCreate.meeting)
    MeetingCreated(this, meetingToCreate.meeting, overlappingMeeting)
  }



  def findConflitingMeeting(meeting: Meeting) = {
    timetable.filter(scheduledMeeting => meeting.hasConflit(scheduledMeeting))
  }



  override def getAggregateId: String = ???

  override def getVersion: Long = ???

  override def replay(event: Event): Aggregate = ???
}
