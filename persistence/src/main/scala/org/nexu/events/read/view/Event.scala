package org.nexu.events.read.view

import java.time.{ZoneId, Instant, ZoneOffset, LocalDateTime}

import reactivemongo.bson.{BSONDateTime, BSONDocument, BSONDocumentWriter, BSONDocumentReader}

/**
  * Created by cyril on 15.11.15.
  */
case class Event(startDate: LocalDateTime, endDate: LocalDateTime, description: String, title: String) {
}

object Event {


  implicit def dateTimeToEpoch(dateTime: LocalDateTime): Long = dateTime.atOffset(ZoneOffset.UTC).toEpochSecond

  implicit object EventViewHandler extends BSONDocumentReader[Event] with BSONDocumentWriter[Event] {
    override def write(t: Event): BSONDocument = {
      BSONDocument(
        "startDate" -> BSONDateTime(t.startDate),
        "endDate" -> BSONDateTime(t.endDate),
        "description" -> t.description,
        "title" -> t.title
      )
    }

    override def read(bson: BSONDocument): Event = {
      val opt: Option[Event] = for {
        title <- bson.getAs[String]("title")
        startDate <- bson.getAs[BSONDateTime]("startDate").map(b => LocalDateTime.ofInstant(Instant.ofEpochMilli(b.value), ZoneId.of("UTC")))
        endDate <- bson.getAs[BSONDateTime]("endDate").map(b => LocalDateTime.ofInstant(Instant.ofEpochMilli(b.value), ZoneId.of("UTC")))
        description <- bson.getAs[String]("description")
      } yield new Event(startDate, endDate, description, title)
      opt.get
    }
  }

}
