package org.nexu.events.domain

import java.time.Duration


case class MeetingFrequency(duration: Duration) {

  def nextEvent(meeting: Meeting): TimeSlot = {
    new TimeSlot(meeting.timeslot.startDateTime.plus(duration), meeting.timeslot.duration)
  }

  def getFuturesEvent(meeting: Meeting): Stream[TimeSlot] = {
    lazy val nextEvent: TimeSlot = this.nextEvent(meeting)
    nextEvent #:: getFuturesEvent(new Meeting(nextEvent, meeting.description, meeting.participants))
  }

}
