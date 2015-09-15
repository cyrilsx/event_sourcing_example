package org.nexu.events.domain

import java.util.Objects.nonNull


class User(val email: String, val nickname: String) {

  def isValid = {
    require(nonNull(email))
  }

}
