package org.nexu.events.domain

import spray.json.DefaultJsonProtocol
import TimeSlotJsonFormats._
import UserJsonFormats._

case class Meeting(timeslot: TimeSlot, description: String, participants: List[User]) {

  /**
   * Case Example:
   * <ul>
   *   <li>m1: 13:00 -> 14:00, m2: 14:05 -> 15:00 = </li>
   *   <li>m1: 13:00 -> 14:00, m2: 13:10 -> 15:00</li>
   *   <li>m1: 13:00 -> 14:00, m2: 13:10 -> 13:50</li>
   *   <li>m1: 13:00 -> 14:00, m2: 10:10 -> 12:50</li>
   * </ul>
   *
   * Algo:
   *  if m1.startDate < m2.endDate && m1.endDate > m2.endDate
   *
   * @param otherMeeting
   * @return
   */
  def hasConflit(otherMeeting: Meeting): Boolean = {
    otherMeeting.timeslot.belongTo(timeslot.startDate) || otherMeeting.timeslot.belongTo(timeslot.getEndDateTime())
  }

  def isValid = {
    require(timeslot != null, "TimeSlot is mandatory")
    require(description != null, "Description is mandatory")
    timeslot.isValid
  }


}

case class RecurrentMeeting(override val timeslot: TimeSlot, override val description: String, override val participants: List[User], period: Period)
  extends Meeting(timeslot, description, participants)



object MeetingCreatedJsonFormats {

  object JsonImplicits extends DefaultJsonProtocol {
    implicit val meetingFormat = jsonFormat3(Meeting)
    implicit val recurrentMeetingFormat = jsonFormat4(RecurrentMeeting)

  }

}