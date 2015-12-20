package org.nexu.events.write.sync

import akka.actor.Actor
import org.nexu.events.event.{TimetableReorganized, MeetingCreated, CalendarCreated}
import org.nexu.events.read.ReadDbConnection
import org.nexu.events.write.sync.CalendarMapper.toReadCalendarView
import scala.concurrent.ExecutionContext.Implicits.global


/**
  *
  */
class EventNotifier extends Actor {

  val dbConnection = new ReadDbConnection

  override def receive: Receive = {
    case calendarCreated: CalendarCreated => dbConnection.connect("calendar").insert(toReadCalendarView(calendarCreated.getAggregate))
    case meetingCreated: MeetingCreated => dbConnection.connect("events").insert(EventMapper.toReadEventView(meetingCreated.meeting))
    case newTimetable: TimetableReorganized => ???
  }
}
