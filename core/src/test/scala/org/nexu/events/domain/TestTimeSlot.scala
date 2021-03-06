package org.nexu.events.domain

import java.time
import java.time.LocalDateTime

import org.scalatest.FunSuite

/**
 * Created by cyril on 22.09.15.
 */
class TestTimeSlot extends FunSuite {

  test("Time is not overlapping as it's in the past") {
    val referenceTimeSlot: TimeSlot = new TimeSlot(LocalDateTime.parse("2010-06-30T01:20"), time.Duration.ofHours(1))
    val isOverlapping: Boolean = referenceTimeSlot
      .isOverlappingWith(new TimeSlot(LocalDateTime.parse("2010-06-30T00:20"), time.Duration.ofHours(1)))

    assert(!isOverlapping, "00h20 -> 01h20 is not overlapping with 01h20 -> 02h20")
  }

  test("Time is not overlapping as it's in the future") {
    val referenceTimeSlot: TimeSlot = new TimeSlot(LocalDateTime.parse("2010-06-30T01:20"), time.Duration.ofHours(1))
    val isOverlapping: Boolean = referenceTimeSlot
      .isOverlappingWith(new TimeSlot(LocalDateTime.parse("2010-06-30T02:20"), time.Duration.ofHours(1)))

    assert(!isOverlapping, "02h20 -> 03h20 is not overlapping with 01h20 -> 02h20")
  }

  test("Start Time outside of the lower bound of the timeslot. Ending time inside") {
    val referenceTimeSlot: TimeSlot = new TimeSlot(LocalDateTime.parse("2010-06-30T01:20"), time.Duration.ofHours(1))
    val isOverlapping: Boolean = referenceTimeSlot
      .isOverlappingWith(new TimeSlot(LocalDateTime.parse("2010-06-30T01:00"), time.Duration.ofHours(1)))

    assert(isOverlapping, "01:20 until 02:00 is overlapping")
  }

  test("End Time outside of the higher bound of the timeslot. Start time inside") {
    val referenceTimeSlot: TimeSlot = new TimeSlot(LocalDateTime.parse("2010-06-30T01:20"), time.Duration.ofHours(1))
    val isOverlapping: Boolean = referenceTimeSlot
      .isOverlappingWith(new TimeSlot(LocalDateTime.parse("2010-06-30T02:00"), time.Duration.ofHours(1)))

    assert(isOverlapping, "02:00 until 02:20 is overlapping")
  }

  test("One timeslot inside another one") {
    val referenceTimeSlot: TimeSlot = new TimeSlot(LocalDateTime.parse("2010-06-30T01:20"), time.Duration.ofHours(2))
    val isOverlapping: Boolean = referenceTimeSlot
      .isOverlappingWith(new TimeSlot(LocalDateTime.parse("2010-06-30T01:30"), time.Duration.ofHours(1)))

    assert(isOverlapping, "The second timeslot is fully overlapping")
  }
}
