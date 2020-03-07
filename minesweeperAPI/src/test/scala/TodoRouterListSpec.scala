import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import models.Todo
import org.scalatest.{Matchers, WordSpec}
import repositories.{InMemoryTodoRepository, TodoRepository}
import rest.resourceRouters.{ApiError, TodoRouter}

class TodoRouterListSpec extends WordSpec with Matchers with ScalatestRouteTest with TodoMocks {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  private val doneTodo =
    Todo("2", "Buy milk", "The cat is thirsty!", done=true)
  private val pendingTodo =
    Todo("1", "Buy eggs", "Ran out of eggs, buy a dozen", done=false)

  private val todos = Seq(doneTodo, pendingTodo)

  class TestTodoRouterClass(repo: TodoRepository) extends TodoRouter { override val todoRepository = repo}

  "A TodoRouter" should {

    "return all the todos" in {
      val repository = new InMemoryTodoRepository(todos)
      val router = new TestTodoRouterClass(repository)

      Get("/todos") ~> router.todoRouter ~> check {
        status shouldBe StatusCodes.OK
        val respTodos = responseAs[Seq[Todo]]
        respTodos shouldBe todos
      }
    }

    "return all the done todos" in {
      val repository = new InMemoryTodoRepository(todos)
      val router = new TestTodoRouterClass(repository)

      Get("/todos/done") ~> router.todoRouter ~> check {
        status shouldBe StatusCodes.OK
        val respTodos = responseAs[Seq[Todo]]
        respTodos shouldBe Seq(doneTodo)
      }
    }

    "return all the pending todos" in {
      val repository = new InMemoryTodoRepository(todos)
      val router = new TestTodoRouterClass(repository)

      Get("/todos/pending") ~> router.todoRouter ~> check {
        status shouldBe StatusCodes.OK
        val respTodos = responseAs[Seq[Todo]]
        respTodos shouldBe Seq(pendingTodo)
      }
    }

    "handle repository failure in the todos route" in {
      val repository = new FailingRepository
      val router = new TestTodoRouterClass(repository)

      Get("/todos") ~> router.todoRouter ~> check {
        status shouldBe ApiError.generic.statusCode
        val resp = responseAs[String]
        resp shouldBe ApiError.generic.message
      }
    }

    "handle repository failure in the done todos route" in {
      val repository = new FailingRepository
      val router = new TestTodoRouterClass(repository)

      Get("/todos/done") ~> router.todoRouter ~> check {
        status shouldBe ApiError.generic.statusCode
        val resp = responseAs[String]
        resp shouldBe ApiError.generic.message
      }
    }

    "handle repository failure in the pending todos route" in {
      val repository = new FailingRepository
      val router = new TestTodoRouterClass(repository)

      Get("/todos/pending") ~> router.todoRouter ~> check {
        status shouldBe ApiError.generic.statusCode
        val resp = responseAs[String]
        resp shouldBe ApiError.generic.message
      }
    }

  }
}
