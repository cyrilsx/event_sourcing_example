package org.nexu.events.domain

import java.time.{Duration, LocalDateTime}
import java.util.Objects.nonNull



/**
 * Created by cyril on 10.09.15.
 */
case class TimeSlot(startDateTime: LocalDateTime, duration: Duration) {

  def isOverlappingWith(timeSlot: TimeSlot): Boolean = {
    this.contains(timeSlot.startDateTime) || this.contains(timeSlot.getEndDateTime())
  }

  def contains(dateTimeValue: LocalDateTime) = dateTimeValue.isAfter(startDateTime) && dateTimeValue.isBefore(getEndDateTime())

  def getEndDateTime(): LocalDateTime = {
    startDateTime.plus(duration)
  }

  def isValid = {
    require(nonNull(startDateTime), "startDate is mandatory")
    require(nonNull(duration), "duration is mandatory")
  }

}
