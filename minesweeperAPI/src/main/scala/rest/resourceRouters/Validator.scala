package rest.resourceRouters

import rest.entities.{CreateGameRequest, CreateTodo, UpdateTodo}

trait Validator[T] {
  def validate(t: T): Option[ApiError]
}

object CreateTodoValidator extends Validator[CreateTodo] {

  def validate(createTodo: CreateTodo): Option[ApiError] =
    if (createTodo.title.isEmpty)
      Some(ApiError.emptyTitleField)
    else
      None
}

object UpdateTodoValidator extends Validator[UpdateTodo] {

  def validate(updateTodo: UpdateTodo): Option[ApiError] =
    if (updateTodo.title.exists(_.isEmpty))
      Some(ApiError.emptyTitleField)
    else
      None
}

object CreateGameRequestValidator extends Validator[CreateGameRequest] {

  def validate(req: CreateGameRequest): Option[ApiError] =
    if (req.mines < 1)
      Some(ApiError.emptyTitleField)
    else
      None
}