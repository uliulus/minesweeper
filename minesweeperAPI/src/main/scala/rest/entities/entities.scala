package rest.entities

import java.sql.Timestamp

case class CreateTodo(title: String, description: String)
case class UpdateTodo(title: Option[String], description: Option[String], done: Option[Boolean])

case class CreateGameRequest(x_dimension: Int, y_dimension: Int, mines: Int, seconds: Int)
case class GameResponse(x_dimension: Int, y_dimension: Int, mines: Int, seconds: Int, status: String, started: Timestamp)

