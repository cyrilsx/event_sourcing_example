package org.nexu.events.event

import java.util.UUID

import org.nexu.events.domain.{Aggregate, Meeting}
import spray.json.{JsValue, pimpAny, jsonWriter, jsonReader}

/**
 * Created by cyril on 10.09.15.
 */
case class MeetingCreated(meeting: Meeting, aggregate: Aggregate, overlappingMeeting: List[Meeting]) extends Event {
  override def getId: UUID = UUID.randomUUID()

  override def getAggregate: Aggregate = aggregate

  override def toJsonValue: JsValue = jsonWriter.toJson

  override def toObjValue(jsValue: JsValue): Event = jsonReader.read(jsValue).asInstanceOf[MeetingCreated]
}

/**
 * Companion class with static
 */
object MeetingCreated {

  def apply(aggregate: Aggregate, meeting: Meeting, overlappingMeeting: List[Meeting]): MeetingCreated = {
    new MeetingCreated(meeting, aggregate, overlappingMeeting)
  }

}
