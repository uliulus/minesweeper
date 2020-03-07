package repositories

import models.Todo
import repositories.games.{GamesRepository, InMemoryGamesRepository}

class RepositoryService(var todoRepository:TodoRepository, var gamesRepository:GamesRepository )

object RepositoryService {
  import scala.concurrent.ExecutionContext.Implicits.global

  def apply(): RepositoryService = {
    val todoRepository = new InMemoryTodoRepository(Seq(
      Todo("1", "Buy eggs", "Ran out of eggs, buy a dozen", false),
      Todo("2", "Buy milk", "The cat is thirsty!", true),
      Todo("3", "Buy rice", "I'm starving!", true),
    ))

    val gamesRepository = new InMemoryGamesRepository()

    new RepositoryService(todoRepository, gamesRepository)
  }

}
