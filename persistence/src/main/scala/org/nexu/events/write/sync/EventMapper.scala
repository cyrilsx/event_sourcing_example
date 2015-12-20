package org.nexu.events.write.sync

import org.nexu.events.domain.Meeting
import org.nexu.events.read.view.Event

/**
  * Created by cyril on 19.11.15.
  */
object EventMapper {

  def toReadEventView(meeting: Meeting): Event = {
    new Event(meeting.timeslot.startDateTime, meeting.timeslot.getEndDateTime(), meeting.description, null)
  }

}
