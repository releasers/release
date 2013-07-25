package models

import play.api.libs.json._
import org.joda.time.DateTime

case class Loan (
  to: User,
  since: DateTime,
  count: Int
)

object Loan {
  implicit val formater = Json.format[Loan]
}