package org.nexu.events.event

import java.util.UUID

import org.nexu.events.domain.Aggregate
import sbt.serialization.JsonValue
import spray.json.JsValue

/**
 * Created by cyril on 10.09.15.
 */
trait Event extends Serializable {

  def getId : UUID

  def getAggregate: Aggregate

  def typeOf: Class[_ <: Event] = this.getClass

  def toJsonValue: JsValue

  def toObjValue(jsValue: JsValue): Event
}

