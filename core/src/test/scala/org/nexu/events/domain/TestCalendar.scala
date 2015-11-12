package org.nexu.events.domain

import org.joda.time
import org.joda.time.DateTime
import org.nexu.events.command.{OptimizeTimetable, CreateMeeting}
import org.nexu.events.event.{TimetableReorganized, MeetingCreated}
import org.scalatest.FunSuite

/**
 * Created by cyril on 20.09.15.
 */
class TestCalendar extends FunSuite {



  test("Add an appointment to my calendar") {
    // Given an empty calendar
    val calendar = new Calendar(new User("cyril@test.org", "sX"), List(), "ProCalendar", 0)
    val meeting: Meeting = new Meeting(new TimeSlot(DateTime.now(), time.Duration.standardHours(1)), "It's important", List())

    // When meeting is added to calendar
    val event = calendar.onCommand.applyOrElse(
      new CreateMeeting(
        meeting, "ProCalendar")
      , null)

    // Then
    assert(event != null, "Command not handle in the aggregate calendar")
    assert(event.isInstanceOf[MeetingCreated], "Unexpected kind of event")
    assert(event.asInstanceOf[MeetingCreated].meeting == meeting)
  }


  test("Test re-organized meeting nominal case") {
    // Given the following meetings
    val meetings = List(
      new Meeting(new TimeSlot(DateTime.parse("2014-01-01T02:00"), time.Duration.standardHours(2)), "It's important", List()),
      new Meeting(new TimeSlot(DateTime.parse("2014-01-01T02:00"), time.Duration.standardHours(1)), "It's important", List()),
      new Meeting(new TimeSlot(DateTime.parse("2014-01-01T03:00"), time.Duration.standardHours(1)), "It's important", List()),
      new Meeting(new TimeSlot(DateTime.parse("2014-01-01T04:00"), time.Duration.standardHours(3)), "It's important", List()),
      new Meeting(new TimeSlot(DateTime.parse("2014-01-01T04:00"), time.Duration.standardHours(2)), "It's important", List()))


    val calendar = new Calendar(new User("cyril@test.org", "sX"), meetings, "ProCalendar", 0)

    // When reorganized calendar for the 01-01-2014
    val event = calendar.onCommand.applyOrElse(
      new OptimizeTimetable("ProCalendar", DateTime.parse("2014-01-01T02:00"), DateTime.parse("2014-01-02T02:00"))
      , null)

    // Then
    assert(event != null, "Command not handle in the aggregate calendar")
    assert(event.isInstanceOf[TimetableReorganized], "Unexpected kind of event")
    assert(event.getAggregate.asInstanceOf[Calendar].timetable.size === 6, "Same amount of events are returned")
  }

}
