package org.nexu.events.domain

import java.time.LocalDateTime
import java.time.temporal.TemporalAmount
import java.util.Objects.nonNull

import spray.json.{JsValue, RootJsonFormat, DefaultJsonProtocol}

/**
 * Created by cyril on 10.09.15.
 */
case class TimeSlot(startDate: LocalDateTime, duration: TemporalAmount) {

  def belongTo(localDateTime: LocalDateTime) = localDateTime.isAfter(startDate) && localDateTime.isBefore(getEndDateTime())

  def getEndDateTime() : LocalDateTime = {
    startDate.plus(duration)
  }

  def isValid = {
    require(nonNull(startDate), "startDate is mandatory")
    require(nonNull(duration), "duration is mandatory")
  }

}

object TimeSlotJsonFormats {

  object JsonImplicits extends DefaultJsonProtocol {
    //implicit val timeSlotFormat = jsonFormat2(TimeSlot)


    object MyProtocol extends RootJsonFormat[TimeSlot] {
      override def read(json: JsValue): TimeSlot = ???

      override def write(obj: TimeSlot): JsValue = ???
    }
  }


}
