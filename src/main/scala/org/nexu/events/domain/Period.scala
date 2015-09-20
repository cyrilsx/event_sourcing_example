package org.nexu.events.domain

import org.joda.time.Duration


case class Period(duration: Duration) {

  def nextEvent(meeting: Meeting): TimeSlot = {
    new TimeSlot(meeting.timeslot.startDate.plus(duration), meeting.timeslot.duration)
  }

  def getFuturesEvent(meeting: Meeting): Stream[TimeSlot] = {
    lazy val nextEvent: TimeSlot = this.nextEvent(meeting)
    nextEvent #:: getFuturesEvent(new Meeting(nextEvent, meeting.description, meeting.participants))
  }

}
