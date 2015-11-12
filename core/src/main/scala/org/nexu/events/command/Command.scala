package org.nexu.events.command

import org.nexu.events.domain.Aggregate

/**
 * External command that can impact the business.
 * Use for write only.
 */
trait Command {

  /**
   * @throws IllegalArgumentException if data is missing/malformed
   */
  def isValid

  /**
   *
   * @return aggregate id in order to identify the target
   */
  def targetAggregateId: String

  /**
   *
   * @return kind of aggregate to target with the command
   */
  def targetClassAggregate: Class[_ <: Aggregate]

}
