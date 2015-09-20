package org.nexu.events.domain


import org.nexu.events.command.{CreateCalendar, CreateMeeting}
import org.nexu.events.event.{CalendarCreated, Event, MeetingCreated}

/**
 *
 * @param owner
 * @param timetable
 * @param aggregateId
 * @param version
 */
case class Calendar(owner: User, timetable: List[Meeting], aggregateId: String, version: Long) extends Aggregate {
  def onCommand = {
    case app: CreateMeeting => createAppointment(app)
    case newCalendar: CreateCalendar => new CalendarCreated(this)
  }

  /**
   * Add an appointment to the current timetable
   *
   * @param meetingToCreate
   * @return event with the overlapping event if any
   */
  def createAppointment(meetingToCreate: CreateMeeting): Event = {
    val overlappingMeeting: List[Meeting] = findConflitingMeeting(meetingToCreate.meeting)
    MeetingCreated(meetingToCreate.meeting, this, overlappingMeeting)
  }

  /**
   *
   * @param meeting
   * @return overlapping meeting or empty list if none
   */
  private def findConflitingMeeting(meeting: Meeting) = {
    timetable.filter(scheduledMeeting => meeting.hasConflit(scheduledMeeting))
  }


  override def getAggregateId: String = aggregateId

  override def getVersion: Long = version

  override def replay(event: Event): Aggregate = {
    event match {
      case meetingCreatedEvent: MeetingCreated =>
        new Calendar(owner, meetingCreatedEvent.meeting :: timetable, aggregateId, version + 1)
    }

  }
}


