package org.nexu.events.write.sync

import akka.actor.Actor
import org.nexu.events.command.DeleteUser
import org.nexu.events.domain.User
import org.nexu.events.event.{CalendarCreated, MeetingCreated, TimetableReorganized, UserCreated}
import org.nexu.events.read.ReadDbConnection
import org.nexu.events.write.sync.CalendarMapper.toReadCalendarView
import reactivemongo.bson.BSONDocument

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
    case newUser: UserCreated => createOrUpdate(newUser.getAggregate)
    case deleted: DeleteUser => dbConnection.connect("user").remove(BSONDocument("email" -> deleted.user.email))
  }

  def createOrUpdate(user: User): Unit = {
    dbConnection.connect("user").findAndUpdate(BSONDocument("email" -> user.email), BSONDocument("$set" -> BSONDocument("nickname" -> user.nickname)))
  }
}
