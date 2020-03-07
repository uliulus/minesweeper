package rest.resourceRouters

import akka.http.scaladsl.server.{Directives, Route}
import rest.controllers.games.GamesController
import rest.entities.CreateGameRequest

trait GamesRouter extends Directives with BaseDirectives with ValidatorDirectives {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  val gamesController: GamesController

  def gamesRouter: Route = pathPrefix("games") {
    pathEndOrSingleSlash {
      get {
        handleWithGeneric(gamesController.list) { gamesResponses =>
          complete(gamesResponses)
        }
      } ~ post {
        entity(as[CreateGameRequest]) { req =>
          validateWith(CreateGameRequestValidator)(req) {
            handleWithGeneric(gamesController.create(req)) { game =>
              complete(game)
            }
          }
        }
      }
    } ~ path(Segment) { id: String =>
      get {
        handleWithGeneric(gamesController.getById(id)) { gameResponse =>
          complete(gameResponse)
        }
    }
  }
}

}
