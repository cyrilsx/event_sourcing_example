package org.nexu.events

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.Extraction
import org.nexu.events.command.{CommandHandler, CreateMeeting}


/**
 * Entry point for EventApplication
 */
object EventApplication extends App {
  // Actor system
  implicit val system = ActorSystem("event-system")
  implicit val materializer = ActorMaterializer()
  implicit val serialization = native.Serialization
  implicit lazy val formats = DefaultFormats


  val route = {
    path("meeting") {
      post {
        decodeRequest {
          entity(as[CreateMeeting]) { createMeeting =>
            complete {
              commandHandler.sendCommand(createMeeting)
                .map(event => compact(render(Extraction.decompose(event))))(system.dispatcher)
            }
          }
        }
      }
    }

  }
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  private val commandHandler = new CommandHandler

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  Console.readLine()


  import system.dispatcher

  // for the future transformations
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.shutdown()) // and shutdown when done


}
