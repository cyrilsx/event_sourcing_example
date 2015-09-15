package org.nexu.events.domain

import java.time.temporal.{TemporalUnit}


class Period(temporalUnit: TemporalUnit, delayUnit: Int) {

  def nextEvent(meeting: Meeting): TimeSlot = {
    new TimeSlot(meeting.timeslot.startDate.plus(delayUnit, temporalUnit), meeting.timeslot.duration)
  }

  def getFuturesEvent(meeting: Meeting): Stream[TimeSlot] = {
    lazy val nextEvent: TimeSlot = this.nextEvent(meeting)
    nextEvent #:: getFuturesEvent(new Meeting(nextEvent, meeting.description, meeting.participants))
  }

}
