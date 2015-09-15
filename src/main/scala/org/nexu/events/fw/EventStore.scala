package org.nexu.events.fw


import java.util.UUID

import com.datastax.driver.core.Row
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._
import org.joda.time.DateTime
import org.nexu.events.event.Event

import CassandraConfig.MyDatabase._


import scala.concurrent.Future


class EventDbObjects extends CassandraTable[EventStore, EventDbObject] {

  object id extends UUIDColumn(this) with PartitionKey[UUID]

  object calendarId extends LongColumn(this) with PartitionKey[Long]

  object jsonEventObject extends StringColumn(this)

  object creationDate extends DateTimeColumn(this)

  override def fromRow(r: Row): EventDbObject = {
    new EventDbObject(
      id(r),
      calendarId(r),
      creationDate(r),
      jsonEventObject(r)
    )
  }
}

case class EventDbObject(id: UUID, calendarId: Long, creationDate: DateTime, jsonEventObject: String)


class EventStore extends EventDbObjects {

  def findAll(pCalendarId: Long) = {
    select.where(_ => calendarId eqs pCalendarId)
  }


  def store(event: Event): Future[Event] = {
    select.count().where(_ => calendarId eqs event.getAggregate.getAggregateId).one()
      .map(count => {
      // find a alternative to this
      synchronized {
        if (count.getOrElse(0) < event.getAggregate.getVersion) {
          throw new IllegalStateException("Out of date version")
        }

        insert.value(_.id, event.getId)
          .value(_.calendarId, event.getAggregate.getAggregateId)
          .value(_.creationDate, DateTime.now())
          .value(_.jsonEventObject, event.toJson)
          .execute()
        event
      }
    })

  }

}
