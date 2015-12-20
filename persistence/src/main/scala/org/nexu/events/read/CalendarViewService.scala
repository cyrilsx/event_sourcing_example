package org.nexu.events.read

import java.time.{ZoneOffset, LocalDateTime}

import org.nexu.events.read.view.CalendarDetailView.CalendarDetailViewHandler
import org.nexu.events.read.view.Event.EventViewHandler
import org.nexu.events.read.view.CalendarView.calendarViewHandler
import org.nexu.events.read.view.{CalendarDetailView, CalendarView, Event}
import reactivemongo.api.ReadPreference.Primary
import reactivemongo.bson.{BSONDateTime, BSONArray, BSONDocument}
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Read only service
  */
class CalendarViewService {

  implicit def dateTimeToEpoch(dateTime: LocalDateTime): Long = dateTime.atOffset(ZoneOffset.UTC).toEpochSecond

  val connection = new ReadDbConnection

  def findByCriteria(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Future[List[Event]] = {
    val calendarCollection = connection.connect("calendar")

    val query = BSONDocument("$and" -> BSONArray(Seq(
      BSONDocument("startDate" -> BSONDocument("$gt" -> BSONDateTime(startDateTime))),
      BSONDocument("endDate" -> BSONDocument("$le" -> BSONDateTime(endDateTime)))
    )))

    calendarCollection.find(query)
      .cursor[Event](Primary)
      .collect[List]()
  }

  def findUserCalendar(email: String): Future[List[CalendarDetailView]] = {
    val calendarCollection = connection.connect("calendar")

    calendarCollection.find(BSONDocument("$eq" -> email))
      .cursor[CalendarDetailView](Primary)
      .collect[List]()
  }

}
