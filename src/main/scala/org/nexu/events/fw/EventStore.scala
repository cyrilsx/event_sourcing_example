package org.nexu.events.fw


import java.util.UUID

import com.datastax.driver.core.Row
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._
import org.joda.time.DateTime
import org.json4s.{Extraction, DefaultFormats}
import org.nexu.events.event.Event

import CassandraConfig.MyDatabase._

import org.json4s.native.JsonMethods._
import scala.concurrent.Future


class EventDbObjects extends CassandraTable[EventStore, EventDbObject] {

  object id extends UUIDColumn(this) with PartitionKey[UUID]

  object calendarId extends StringColumn(this) with PartitionKey[String]

  object jsonEventObject extends StringColumn(this)

  object creationDate extends DateTimeColumn(this)

  object eventClass extends StringColumn(this)

  override def fromRow(r: Row): EventDbObject = {
    new EventDbObject(
      id(r),
      calendarId(r),
      creationDate(r),
      jsonEventObject(r),
      eventClass(r)
    )
  }
}

case class EventDbObject(id: UUID, calendarId: String, creationDate: DateTime, jsonEventObject: String, eventClass: String)


class EventStore extends EventDbObjects {

  def findAll(pCalendarId: String): Future[List[Event]] = {
    select.where(_ => calendarId eqs pCalendarId)
      .fetch()
      .map(eventDbObjects => {
        eventDbObjects.map(e => {
          val anyClass: Class[_] = Class.forName(e.eventClass)
          val eventClass: Class[_ <: Event] = anyClass.asInstanceOf[Class[_ <: Event]]
          toObj(e, eventClass)
        })
    })
  }

  private def toObj[T <: Event](eventDbObject: EventDbObject, clazz: Class[T])(implicit m: Manifest[T]): T = {
    implicit lazy val formats = DefaultFormats
    parse(eventDbObject.jsonEventObject, true).extract[T]
  }


  def store(event: Event): Future[Event] = {
    implicit lazy val formats = DefaultFormats
    select.count().where(_ => calendarId eqs event.getAggregate.getAggregateId).one()
      .map(count => {
      // find a alternative to this
      synchronized {
        if (count.getOrElse(0).asInstanceOf[Long] < event.getAggregate.getVersion) {
          throw new IllegalStateException("Out of date version")
        }

        insert.value(_.id, event.getId)
          .value(_.calendarId, event.getAggregate.getAggregateId)
          .value(_.creationDate, DateTime.now())
          .value(_.jsonEventObject, compact(render(Extraction.decompose(event))))
          .value(_.eventClass, event.getClass.getName)
          .execute()
        event
      }
    })

  }

}
