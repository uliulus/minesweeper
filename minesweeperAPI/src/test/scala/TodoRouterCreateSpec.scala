import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import models.Todo
import org.scalatest.{Matchers, WordSpec}
import repositories.{InMemoryTodoRepository, TodoRepository}
import rest.entities.CreateTodo
import rest.resourceRouters.{ApiError, TodoRouter}

class TodoRouterCreateSpec extends WordSpec with Matchers with ScalatestRouteTest with TodoMocks {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  val testCreateTodo = CreateTodo(
    "Test todo",
    "Test description"
  )

  class TestTodoRouterClass(repo: TodoRepository) extends TodoRouter { override val todoRepository = repo}

  "A TodoRouter" should {

    "create a todo with valid data" in {
      val repository = new InMemoryTodoRepository()
      val router = new TestTodoRouterClass(repository)

      Post("/todos", testCreateTodo) ~> router.todoRouter ~> check {
        status shouldBe StatusCodes.OK
        val resp = responseAs[Todo]
        resp.title shouldBe testCreateTodo.title
        resp.description shouldBe testCreateTodo.description
      }
    }

    "not create a todo with invalid data" in {
      val repository = new FailingRepository
      val router = new TestTodoRouterClass(repository)

      Post("/todos", testCreateTodo.copy(title = "")) ~> router.todoRouter ~> check {
        status shouldBe ApiError.emptyTitleField.statusCode
        val resp = responseAs[String]
        resp shouldBe ApiError.emptyTitleField.message
      }
    }

    "handle repository failure when creating todos" in {
      val repository = new FailingRepository
      val router = new TestTodoRouterClass(repository)

      Post("/todos", testCreateTodo) ~> router.todoRouter ~> check {
        status shouldBe ApiError.generic.statusCode
        val resp = responseAs[String]
        resp shouldBe ApiError.generic.message
      }
    }
  }

}
