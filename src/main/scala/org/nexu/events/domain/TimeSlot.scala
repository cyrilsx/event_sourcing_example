package org.nexu.events.domain

import java.util.Objects.nonNull

import org.joda.time.{Period, DateTime}


/**
 * Created by cyril on 10.09.15.
 */
case class TimeSlot(startDateTime: DateTime, duration: org.joda.time.Duration) {

  def contains(dateTimeValue: DateTime) = dateTimeValue.isAfter(startDateTime) && dateTimeValue.isBefore(getEndDateTime())

  def isOverlappingWith(timeSlot: TimeSlot): Boolean = {
    this.contains(timeSlot.startDateTime) || this.contains(timeSlot.getEndDateTime())
  }

  def getEndDateTime() : DateTime = {
    startDateTime.plus(duration)
  }

  def isValid = {
    require(nonNull(startDateTime), "startDate is mandatory")
    require(nonNull(duration), "duration is mandatory")
  }

}
