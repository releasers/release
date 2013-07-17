package models

import play.api.libs.json._

case class User (email: String)

object User {
  implicit val formater = Json.format[User]

  def fromSession(id: String) = {
    User("test@test.com")
  }
}