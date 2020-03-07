package models

import java.sql.Timestamp

case class Todo(id: String, title: String, description: String, done: Boolean)

case class Game(gameId: String, x_dimension: Int, y_dimension: Int, mines: Int, seconds: Int, status: String, started: Timestamp)

//case class Cell(gameId: String, x: Int, y: Int, kind: String, hasMine: Boolean, flag: String)
