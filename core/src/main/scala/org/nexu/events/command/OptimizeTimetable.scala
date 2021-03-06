package org.nexu.events.command

import java.time.LocalDateTime
import java.util.Objects.requireNonNull

import org.nexu.events.domain.{Aggregate, Calendar}


case class OptimizeTimetable(calendarId: String, from: LocalDateTime, to: LocalDateTime) extends Command {

  override def isValid: Unit = {
    requireNonNull(from, "start date must be fill")
    requireNonNull(from, "end date must be fill")
    if (from.isAfter(to)) {
      throw new IllegalArgumentException("end date must be after start date. dates given from " + from + " to " + to)
    }
  }

  override def targetAggregateId: String = calendarId

  override def targetClassAggregate: Class[_ <: Aggregate] = classOf[Calendar]
}
