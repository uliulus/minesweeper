package repositories

import models.Todo

class RepositoryService(var todoRepository:TodoRepository)

object RepositoryService {
  import scala.concurrent.ExecutionContext.Implicits.global

  def apply(): RepositoryService = {
    val todoRepository = new InMemoryTodoRepository(Seq(
      Todo("1", "Buy eggs", "Ran out of eggs, buy a dozen", false),
      Todo("2", "Buy milk", "The cat is thirsty!", true),
      Todo("3", "Buy rice", "I'm starving!", true),
    ))

    new RepositoryService(todoRepository)
  }

}
