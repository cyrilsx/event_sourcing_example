package org.nexu.events.domain

import java.util.Objects.nonNull

import org.joda.time.DateTime


/**
 * Created by cyril on 10.09.15.
 */
case class TimeSlot(startDate: DateTime, duration: org.joda.time.Period) {

  def belongTo(localDateTime: DateTime) = localDateTime.isAfter(startDate) && localDateTime.isBefore(getEndDateTime())

  def getEndDateTime() : DateTime = {
    startDate.plus(duration)
  }

  def isValid = {
    require(nonNull(startDate), "startDate is mandatory")
    require(nonNull(duration), "duration is mandatory")
  }

}
