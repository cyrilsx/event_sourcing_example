package org.nexu.events.domain

import org.joda.time
import org.joda.time.DateTime
import org.nexu.events.command.CreateMeeting
import org.nexu.events.event.MeetingCreated
import org.scalatest.FunSuite

/**
 * Created by cyril on 20.09.15.
 */
class TestCalendar extends FunSuite {

  val calendar = new Calendar(new User("cyril@test.org", "sX"), List(), "ProCalendar", 0)

  test("Add an appointment to my calendar") {
    val meeting: Meeting = new Meeting(new TimeSlot(DateTime.now(), time.Duration.standardHours(1)), "It's important", List())
    val event = calendar.onCommand.applyOrElse(
      new CreateMeeting(
        meeting, "ProCalendar")
      , null)

    assert(event != null, "Command not handle in the aggregate calendar")
    assert(event.isInstanceOf[MeetingCreated], "Unexpected kind of event")
    assert(event.asInstanceOf[MeetingCreated].meeting == meeting)
  }

}
