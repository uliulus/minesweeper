package models

import models.GameStatus.GameStatus

case class Todo(id: String, title: String, description: String, done: Boolean)

object GameStatus extends Enumeration {

  type GameStatus = Value

  val Created = Value("created")
  val InProgress = Value("in-progress")
  val Exploded = Value("exploded")
  val Cleared = Value("cleared")
}

case class Game(gameId: String, x_dimension: Int, y_dimension: Int, mines: Int, seconds: Int, status: GameStatus)

//case class Cell(gameId: String, x: Int, y: Int, kind: String, hasMine: Boolean, flag: String)
