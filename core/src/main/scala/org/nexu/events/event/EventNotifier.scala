package org.nexu.events.event

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
 *
 */
class EventNotifier extends Actor {
  override def receive: Receive = {
    case calendarCreated: CalendarCreated => ???
    case meetingCreated: MeetingCreated => ???
    case newTimetable: TimetableReorganized => ???
  }
}
