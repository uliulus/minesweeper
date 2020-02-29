package rest

import akka.http.scaladsl.server.Route
import repositories.{RepositoryService, TodoRepository}
import rest.resourceRouters.TodoRouter

class MineSweeperAPI(repos: RepositoryService) extends Router with TodoRouter {
  override lazy val todoRepository: TodoRepository = repos.todoRepository
  override def route: Route = todoRouter
}