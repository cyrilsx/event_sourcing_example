package org.nexu.events.command

import org.nexu.events.domain.{Aggregate, User}

/**
  * Created by cyril on 20.12.15.
  */
case class DeleteUser(user: User) extends Command {
  /**
    * @throws IllegalArgumentException if data is missing/malformed
    */
  override def isValid: Unit = {
    user.isValid
  }

  /**
    *
    * @return aggregate id in order to identify the target
    */
  override def targetAggregateId: String = user.email

  /**
    *
    * @return kind of aggregate to target with the command
    */
  override def targetClassAggregate: Class[_ <: Aggregate] = classOf[User]
}
