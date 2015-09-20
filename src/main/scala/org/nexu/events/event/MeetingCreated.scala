package org.nexu.events.event


import java.util.UUID

import org.nexu.events.domain.{Aggregate, Calendar, Meeting}

/**
 * Created by cyril on 10.09.15.
 */
case class MeetingCreated(meeting: Meeting, aggregate: Calendar, overlappingMeeting: List[Meeting]) extends Event {
  override def getId: UUID = UUID.randomUUID()

  override def getAggregate: Aggregate = aggregate

}

