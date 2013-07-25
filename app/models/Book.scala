package models

import play.api.libs.json._

case class Book (
  isbn: String,
  comments: Seq[Comment]
)

object Book {
  implicit val formater = Json.format[Book]
}