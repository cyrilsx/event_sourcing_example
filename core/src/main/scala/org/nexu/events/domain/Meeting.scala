package org.nexu.events.domain

case class Meeting(timeslot: TimeSlot, description: String, participants: List[User]) {

  /**
   * Case Example:
   * <ul>
   * <li>m1: 13:00 -> 14:00, m2: 14:05 -> 15:00 = </li>
   * <li>m1: 13:00 -> 14:00, m2: 13:10 -> 15:00</li>
   * <li>m1: 13:00 -> 14:00, m2: 13:10 -> 13:50</li>
   * <li>m1: 13:00 -> 14:00, m2: 10:10 -> 12:50</li>
   * </ul>
   *
   *
   * @param otherMeeting
   * @return
   */
  def hasConflit(otherMeeting: Meeting): Boolean = {
    otherMeeting.timeslot.contains(timeslot.startDateTime) || otherMeeting.timeslot.contains(timeslot.getEndDateTime())
  }

  def isValid = {
    require(timeslot != null, "TimeSlot is mandatory")
    require(description != null, "Description is mandatory")
    timeslot.isValid
  }


}

class RecurrentMeeting(override val timeslot: TimeSlot, override val description: String, override val participants: List[User], period: MeetingFrequency)
  extends Meeting(timeslot, description, participants)

