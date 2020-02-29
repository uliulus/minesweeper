package rest

import rest.entities.{CreateTodo, UpdateTodo}

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
