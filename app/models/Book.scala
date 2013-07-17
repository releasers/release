package models

import play.api.libs.json._

case class Book (isbn: String)

object Book {
  implicit val formater = Json.format[Book]
}