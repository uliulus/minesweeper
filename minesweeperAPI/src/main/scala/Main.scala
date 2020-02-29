import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import repositories.RepositoryService
import rest.MineSweeperAPI

import scala.concurrent.Await
import scala.util.{Failure, Success, Try}

object Main extends App {

  val host = "0.0.0.0"
  val port = Try(System.getenv("PORT")).map(_.toInt).getOrElse(9000)

  implicit val system: ActorSystem = ActorSystem(name = "todoapi")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import system.dispatcher

  val router = new MineSweeperAPI(RepositoryService())
  val server = new Server(router, host, port)

  val binding = server.bind()
  binding.onComplete {
    case Success(_) => println("Success!")
    case Failure(error) => println(s"Failed: ${error.getMessage}")
  }

  import scala.concurrent.duration._
  Await.result(binding, 3.seconds)
}
