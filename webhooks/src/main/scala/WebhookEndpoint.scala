package webhooks

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Graph, SourceShape}
import akka.stream.scaladsl.{FlowOpsMat, Sink, Source}
import cats.effect.IO

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}
//import spray.json._

import scala.concurrent.Future

object WebhookEndpoint {
  private def requestRoute(sendEvent: WebhookEvent => Unit): Route = {
    path("webhook" / RemainingPath) { path =>
      post {
        headerValueByName("X-GitHub-Event") { event =>
          println(s"Received event: $event")
          entity(as[String]) { payload =>
            // Process the GitHub webhook payload
            println(s"Received payload: $payload")

            WebhookEvent.parse(event, payload) match {
              case Some(event) => sendEvent(event)
              case None => println("Failed to parse event")
            }

            complete(HttpResponse(StatusCodes.OK))
          }
        }
      }
    }
  }

  def source(interface: String = "localhost", port: Int = 8080, bufferSize: Int = 256)(implicit
    as: ActorSystem
  ): Source[WebhookEvent, Future[Done]] = {
    implicit val ec: ExecutionContext = as.dispatcher

    Source
      .queue[WebhookEvent](bufferSize)
      .mapMaterializedValue { queue =>
        Http()
          .newServerAt(interface, port)
          .bind(requestRoute(queue.offer))
      }
      .watchTermination() { (serverBinding, future) =>
        future.onComplete { result =>
          serverBinding.onComplete {
            case Success(binding) => binding.terminate(hardDeadline = 3.seconds)
            case Failure(ex) => println(s"Server binding failure: ${ex.getMessage}")
          }

          result match {
              case Success(_) => println("Server stopped.")
              case Failure(ex) => println(s"Server failed with: $ex")
          }
        }

        future
      }
  }
}

//class WebhookEndpoint {
//  implicit val system = ActorSystem("webhook-system")
//  implicit val materializer = ActorMaterializer()
//  implicit val executionContext = system.dispatcher
//
//  val x = Http().newServerAt("localhost", 8080).bindFlow(route)

//  def run(sink: Sink[WebhookEvent, _], interface: String, port: Int): IO[Unit] = {
//    Sink.
//
//    val bindingFuture = Http().newServerAt(interface, port).bindFlow()
//
//    IO {
//
//    }.onCancel(IO.unit)
//  }
//}
