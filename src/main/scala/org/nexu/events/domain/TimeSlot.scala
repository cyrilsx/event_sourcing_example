package org.nexu.events.domain

import java.time.LocalDateTime
import java.time.temporal.TemporalAmount
import java.util.Objects.nonNull

/**
 * Created by cyril on 10.09.15.
 */
class TimeSlot(val startDate: LocalDateTime, val duration: TemporalAmount) {

  def belongTo(localDateTime: LocalDateTime) = localDateTime.isAfter(startDate) && localDateTime.isBefore(getEndDateTime())

  def getEndDateTime() : LocalDateTime = {
    startDate.plus(duration)
  }

  def isValid = {
    require(nonNull(startDate), "startDate is mandatory")
    require(nonNull(duration), "duration is mandatory")
  }

}
