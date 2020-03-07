package rest.entities

case class CreateTodo(title: String, description: String)
case class UpdateTodo(title: Option[String], description: Option[String], done: Option[Boolean])
