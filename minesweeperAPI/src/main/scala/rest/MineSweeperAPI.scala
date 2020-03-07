package rest

import akka.http.scaladsl.server.Route
import repositories.RepositoryService
import rest.controllers.games.GamesController
import rest.resourceRouters.GamesRouter

import scala.concurrent.ExecutionContext

class MineSweeperAPI(repos: RepositoryService)
                    (implicit ec: ExecutionContext)
  extends Router with GamesRouter {
  override lazy val gamesController: GamesController = new GamesController(repos.gamesRepository)
  override def route: Route = gamesRouter
}