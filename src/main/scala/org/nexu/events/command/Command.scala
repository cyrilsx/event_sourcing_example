package org.nexu.events.command

import org.nexu.events.domain.Aggregate

trait Command {

  def isValid

  def targetAggregateId: String

  def targetClassAggregate: Class[_ <: Aggregate]

}
