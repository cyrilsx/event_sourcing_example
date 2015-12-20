package org.nexu.events.read.view

import reactivemongo.bson.{Macros, BSONDocument, BSONHandler}

/**
  * Created by cyril on 15.11.15.
  */
case class CalendarView(name: String)

object CalendarView {
  implicit val calendarViewHandler: BSONHandler[BSONDocument, CalendarView] =
    Macros.handler[CalendarView]
}