package models

import play.api.libs.json._
import org.joda.time.DateTime

case class Comment (
  author: User,
  createdDate: DateTime,
  message: String
)

object Comment {
  implicit val formater = Json.format[Comment]
}