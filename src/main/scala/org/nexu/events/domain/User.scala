package org.nexu.events.domain

import java.util.Objects.nonNull

import spray.json.DefaultJsonProtocol


case class User(email: String, nickname: String) {

  def isValid = {
    require(nonNull(email))
  }

}

object UserJsonFormats {

  object JsonImplicits extends DefaultJsonProtocol {
    implicit val userFormat = jsonFormat2(User)

  }

}
