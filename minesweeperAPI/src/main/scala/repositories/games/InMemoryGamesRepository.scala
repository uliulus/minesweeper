package repositories.games

import models.Game

import scala.concurrent.Future

class InMemoryGamesRepository extends GamesRepository {
  private var games: Seq[Game] = Seq.empty
  // this representation is not efficient, because Seq  is immutable and has to be copied.
  // But I don't care since it is temporary, the final representation will persist in db

  override def store(g: Game): Future[Boolean] = {
    if (games.exists(_.gameId==g.gameId)) {
      Future.successful(false)
    }
    else {
      games = games :+ g
      Future.successful(true)
    }
  }

  override def getById(id: String): Future[Option[Game]] = Future.successful(games.find(_.gameId==id))

  override def getAll(): Future[Seq[Game]] = Future.successful(games)
}
