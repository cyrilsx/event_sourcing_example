package org.nexu.events.fw

import com.websudos.phantom.connectors.{KeySpaceDef, ContactPoints}
/**
 * Created by cyril on 11.09.15.
 */
object CassandraConfig {

  object Defaults {
    val hosts = Seq("127.0.0.1")

    val connector: KeySpaceDef = ContactPoints(hosts).keySpace("event_store")
  }

  class MyDatabase(val keyspace: KeySpaceDef) extends com.websudos.phantom.db.DatabaseImpl(keyspace) {
    object events extends EventDbObjects with keyspace.Connector
  }

  object MyDatabase extends MyDatabase(Defaults.connector)
}
