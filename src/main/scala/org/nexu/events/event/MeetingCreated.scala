package org.nexu.events.event

import java.util.UUID

import org.nexu.events.domain.{Aggregate, Meeting}
import spray.json._

/**
 * Created by cyril on 10.09.15.
 */
case class MeetingCreated(meeting: Meeting, aggregate: Aggregate, overlappingMeeting: List[Meeting]) extends Event {
  override def getId: UUID = UUID.randomUUID()

  override def getAggregate: Aggregate = aggregate

  override def toJsonValue: JsValue = jsonWriter.toJson

  override def toObjValue(jsValue: JsValue): Event = jsonReader.read(jsValue).asInstanceOf[MeetingCreated]
}


object MeetingCreatedJsonFormats {

  object JsonImplicits extends DefaultJsonProtocol {
    implicit val meetingCreatedFormat = jsonFormat3(MeetingCreated)
  }

}