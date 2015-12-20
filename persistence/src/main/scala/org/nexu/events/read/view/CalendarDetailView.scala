package org.nexu.events.read.view

import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}

/**
  * Created by cyril on 15.11.15.
  */
case class CalendarDetailView(name: String, creationDateTime: LocalDateTime) {


}

object CalendarDetailView {

  import reactivemongo.bson._

  implicit def dateTimeToEpoch(dateTime: LocalDateTime): Long = dateTime.atOffset(ZoneOffset.UTC).toEpochSecond

  implicit object CalendarDetailViewHandler extends BSONDocumentReader[CalendarDetailView] with BSONDocumentWriter[CalendarDetailView] {
    override def read(bson: BSONDocument): CalendarDetailView = {
      val opt: Option[CalendarDetailView] = for {
        name <- bson.getAs[String]("name")
        creationDateTime <- bson.getAs[BSONDateTime]("creationDateTime").map(b => LocalDateTime.ofInstant(Instant.ofEpochMilli(b.value), ZoneId.of("UTC")))
      } yield new CalendarDetailView(name, creationDateTime)
      opt.get
    }

    override def write(t: CalendarDetailView): BSONDocument = {
      BSONDocument(
        "name" -> t.name,
        "creationDateTime" -> new BSONDateTime(t.creationDateTime)
      )
    }
  }

}
