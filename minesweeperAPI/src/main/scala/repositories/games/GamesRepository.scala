package repositories.games

import models.Game

import scala.concurrent.Future

trait GamesRepository {
  def store(g: Game): Future[Boolean]

  def getById(id: String): Future[Option[Game]]

  def getAll(): Future[Seq[Game]]
}
