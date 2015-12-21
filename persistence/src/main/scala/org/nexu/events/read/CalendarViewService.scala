package org.nexu.events.read

import java.time.{LocalDateTime, ZoneOffset}

import org.nexu.events.read.view.CalendarDetailView.CalendarDetailViewHandler
import org.nexu.events.read.view.Event.EventViewHandler
import org.nexu.events.read.view.{CalendarDetailView, Event}
import reactivemongo.api.ReadPreference.Primary
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Read only service
  */
class CalendarViewService {

  val connection = new ReadDbConnection

  implicit def dateTimeToEpoch(dateTime: LocalDateTime): Long = dateTime.atOffset(ZoneOffset.UTC).toEpochSecond

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
