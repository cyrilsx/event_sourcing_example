package org.nexu.events.event

import java.util.UUID

import org.nexu.events.domain.User

/**
  * Created by cyril on 20.09.15.
  */
class UserDeleted(user: User) extends Event {
  override def getId: UUID = UUID.randomUUID()

  override def getAggregate = user
}
