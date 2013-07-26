package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

case class Comment (
  author: BSONObjectID,
  createdDate: DateTime,
  message: String,
  rate: Int
)

object Comment {
  implicit val formater = Json.format[Comment]
}