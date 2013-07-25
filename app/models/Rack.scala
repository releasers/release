package models

import play.api.libs.json._

case class Rack (
  book: Book,
  count: Int,
  loans: Seq[Loan],
  queue: List[User]
)

object Rack {
  implicit val formater = Json.format[Rack]

  def remaining: Int = {
    // this.count - reduce or fold loans.count
    0
  }
}