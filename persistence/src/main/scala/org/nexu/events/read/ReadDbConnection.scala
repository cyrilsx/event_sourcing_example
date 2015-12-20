package org.nexu.events.read

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{Collection, DB, MongoDriver}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by cyril on 13.10.15.
 */
class ReadDbConnection {

  def connect(collection: String): BSONCollection = {
    // gets an instance of the driver
    // (creates an actor system)
    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))

    // Gets a reference to the database "plugin"
    //    val db = connection("calendar")

    // Gets a reference to the collection "calendar"
    // By default, you get a BSONCollection.
    val database: DB = connection.db("calendar")
    database.collection[BSONCollection](collection)
  }


}
