package org.nexu.events

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import org.nexu.events.command.{CommandHandler, CreateMeeting}
import org.nexu.events.event.MeetingCreatedJsonFormats.JsonImplicits


/**
 * Entry point for EventApplication
 */
object EventApplication extends App {
  // Actor system
  implicit val system = ActorSystem("event-system")
  implicit val materializer = ActorMaterializer()

  import JsonImplicits._
  val route = {
    path("meeting") {
      post {
        decodeRequest {
          entity(as[CreateMeeting]) { createMeeting =>
            complete {
              commandHandler.sendCommand(createMeeting)
                .map(event => event.toJsonValue.toString())
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
