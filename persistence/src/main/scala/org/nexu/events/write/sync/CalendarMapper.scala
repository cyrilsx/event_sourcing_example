package org.nexu.events.write.sync

import java.time.LocalDateTime

import org.nexu.events.domain.Calendar
import org.nexu.events.read.view.{CalendarDetailView, CalendarView}

/**
  * Created by cyril on 19.11.15.
  */
object CalendarMapper {

  def toReadCalendarView(calendar: Calendar): CalendarDetailView = {
    new CalendarDetailView(calendar.getAggregateId, LocalDateTime.now())
  }

}
