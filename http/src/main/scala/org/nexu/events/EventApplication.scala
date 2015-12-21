package org.nexu.events

import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.Deflate
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.Credentials
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
import org.json4s.native.JsonMethods._
import org.json4s.{Extraction, _}
import org.nexu.events.command.{CommandHandler, CreateCalendar, CreateMeeting}
import org.nexu.events.read.CalendarViewService
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Entry point for EventApplication
  */
object EventApplication extends App {

  implicit lazy val formats = DefaultFormats
  // Actor system
  implicit val system = ActorSystem("event-system")
  implicit val materializer = ActorMaterializer()
  implicit val serialization = native.Serialization
  val route = {
    path("meeting") {
      authenticateBasicAsync(realm = "admin area", myUserPassAuthenticator) { user =>
        post {
          decodeRequest {
            entity(as[CreateMeeting]) { createMeeting =>
              complete {
                commandHandler.sendCommand(createMeeting)
                  .map(event => compact(render(Extraction.decompose(event))))(system.dispatcher)
              }
            }
          }
        } ~
          pathPrefix("monthly") {
            get {
              encodeResponseWith(Deflate) {
                complete {
                  // marshal custom object with in-scope marshaller
                  calendarViewService.findByCriteria(LocalDateTime.now(), LocalDateTime.now().plusMonths(1))
                }
              }
            }
          }
      }


    } ~
      path("calendar") {
        authenticateBasicAsync(realm = "admin area", myUserPassAuthenticator) { user =>
          post {
            decodeRequest {
              entity(as[CreateCalendar]) { createCalendar =>
                complete {
                  commandHandler.sendCommand(createCalendar)
                    .map(event => compact(render(Extraction.decompose(event))))(system.dispatcher)
                }
              }
            }
          } ~
            get {
              encodeResponseWith(Deflate) {
                complete {
                  // marshal custom object with in-scope marshaller
                  calendarViewService.findUserCalendar(user)
                }
              }
            }
        }
      } ~
      path("test") {
        get {
          complete {
            "{ status : Test - OK}"
          }
        }
      }

    //    path("meeting" / IntNumber / endDate) {
    //
    //    }

  }
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  private val logger = LoggerFactory.getLogger(this.getClass)
  private val commandHandler = new CommandHandler
  private val calendarViewService = new CalendarViewService

  def myUserPassAuthenticator(credentials: Credentials): Future[Option[String]] =
    credentials match {
      case p@Credentials.Provided(id) =>
        Future {
          // potentially
          if (p.verify("p4ssw0rd")) Some(id)
          else None
        }
      case _ => Future.successful(None)
    }


  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  Console.readLine()



  bindingFuture onFailure {
    case ex: Exception =>
      logger.error("Unexpected exception ", ex)
  }


}
