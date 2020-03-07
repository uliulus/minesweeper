package rest.controllers.games

import java.util.UUID
import models.{Game, GameStatus}
import repositories.games.GamesRepository
import rest.entities.{CreateGameRequest, GameResponse}

import scala.concurrent.{ExecutionContext, Future}

class GamesController(repo: GamesRepository)
                     (implicit ec: ExecutionContext) {

  def getById(id: String): Future[GameResponse] = {
    repo.getById(id) flatMap {
      case Some (g) => Future.successful(toResponse(g))
      case _ => Future.failed(new Exception("game not found")) //todo add new exception for this
    }
  }

  def list: Future[Seq[GameResponse]] = {
    repo.getAll() map (_.map(toResponse))
  }

  def create(req: CreateGameRequest): Future[GameResponse] = {
    val g = fromCreateRequest(req)
    repo.store(g) flatMap {
      case true => Future.successful(toResponse(g))
      case _ => Future.failed(new Exception("game already stored!!!")) //todo add new exception for this
    }
  }

  private def toResponse(g: Game): GameResponse = GameResponse(g.x_dimension, g.y_dimension, g.mines, g.seconds, g.status.toString)

  private def fromCreateRequest(req: CreateGameRequest): Game = {
    Game(UUID.randomUUID().toString, req.x_dimension, req.y_dimension, req.mines, req.seconds, GameStatus.Created)
  }

}
