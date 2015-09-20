package org.nexu.events.domain

import java.util.Objects.nonNull



case class User(email: String, nickname: String) {

  def isValid = {
    require(nonNull(email))
  }

}

