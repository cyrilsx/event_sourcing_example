package org.nexu.events.domain

import java.time.{Duration, LocalDateTime}

import org.nexu.events.command.{CreateMeeting, OptimizeTimetable}
import org.nexu.events.event.{MeetingCreated, TimetableReorganized}
import org.scalatest.FunSuite

/**
 * Created by cyril on 20.09.15.
 */
class TestCalendar extends FunSuite {



  test("Add an appointment to my calendar") {
    // Given an empty calendar
    val calendar = new Calendar(new User("cyril@test.org", "sX"), List(), "ProCalendar", 0)
    val meeting: Meeting = new Meeting(new TimeSlot(LocalDateTime.now(), Duration.ofHours(1)), "It's important", List())

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
      new Meeting(new TimeSlot(LocalDateTime.parse("2014-01-01T02:00"), Duration.ofHours(2)), "It's important", List()),
      new Meeting(new TimeSlot(LocalDateTime.parse("2014-01-01T02:00"), Duration.ofHours(1)), "It's important", List()),
      new Meeting(new TimeSlot(LocalDateTime.parse("2014-01-01T03:00"), Duration.ofHours(1)), "It's important", List()),
      new Meeting(new TimeSlot(LocalDateTime.parse("2014-01-01T04:00"), Duration.ofHours(3)), "It's important", List()),
      new Meeting(new TimeSlot(LocalDateTime.parse("2014-01-01T04:00"), Duration.ofHours(2)), "It's important", List()))


    val calendar = new Calendar(new User("cyril@test.org", "sX"), meetings, "ProCalendar", 0)

    // When reorganized calendar for the 01-01-2014
    val event = calendar.onCommand.applyOrElse(
      new OptimizeTimetable("ProCalendar", LocalDateTime.parse("2014-01-01T02:00"), LocalDateTime.parse("2014-01-02T02:00"))
      , null)

    // Then
    assert(event != null, "Command not handle in the aggregate calendar")
    assert(event.isInstanceOf[TimetableReorganized], "Unexpected kind of event")
    assert(event.getAggregate.asInstanceOf[Calendar].timetable.size === 6, "Same amount of events are returned")
  }

}
