package org.nexu.events.domain


import org.joda.time.{Period, DateTime}
import org.nexu.events.command.{CreateCalendar, OptimizeTimetable, CreateMeeting}
import org.nexu.events.event.{TimetableReorganized, CalendarCreated, Event, MeetingCreated}

import scala.annotation.tailrec

/**
 *
 * @param owner
 * @param timetable
 * @param aggregateId
 * @param version
 */
case class Calendar(owner: User, timetable: List[Meeting], aggregateId: String, version: Long) extends Aggregate {

  private val EMPTY_CALENDAR = new Calendar(null, List(), null, 0)

  def onCommand = {
    case app: CreateMeeting => createAppointment(app)
    case newCalendar: CreateCalendar => createCalendar(newCalendar)
    case optimizationCalendar: OptimizeTimetable => optimizeTimeTable(optimizationCalendar.from, optimizationCalendar.to)
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

  /**
   *
   * @param createCommand
   * @return
   */
  def createCalendar(createCommand: CreateCalendar) = {
    if (this.eq(EMPTY_CALENDAR))
      new CalendarCreated(new Calendar(createCommand.user, List(), createCommand.calendarId, 0))
    else
      throw new IllegalArgumentException("calendar id already exist " + createCommand.calendarId)
  }


  def optimizeTimeTable(from: DateTime, to: DateTime) = {
    val selectedMeeting = this.timetable
      .filter(meeting => meeting.timeslot.startDateTime.isAfter(from)
        && meeting.timeslot.getEndDateTime().isBefore(to))
      .sortBy(meeting => meeting.timeslot.getEndDateTime())

    val temporaryTimetable =this.timetable
      .filter(meeting => meeting.timeslot.startDateTime.isAfter(from)
      && meeting.timeslot.getEndDateTime().isBefore(to))


    val meetingChanged = organiseMeeting(selectedMeeting, List(), List())
    new TimetableReorganized(new Calendar(owner, temporaryTimetable ::: meetingChanged._1 ::: meetingChanged._2, aggregateId, version + 1))



    @tailrec
    def organiseMeeting(meetingToSchedule: List[Meeting], plannedMeeting: List[Meeting], unresolvedMeeting: List[Meeting]): (List[Meeting], List[Meeting]) = {
      if(meetingToSchedule.isEmpty) {
        (plannedMeeting, unresolvedMeeting)
      } else if(plannedMeeting.exists(meeting => meeting.hasConflit(meetingToSchedule.head))) {
        organiseMeeting(meetingToSchedule.tail, plannedMeeting, meetingToSchedule.head ::unresolvedMeeting)
      } else {
        organiseMeeting(meetingToSchedule.tail, meetingToSchedule.head :: plannedMeeting, unresolvedMeeting)
      }
    }

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


