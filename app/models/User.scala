package models

import play.api.libs.json._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

case class Profile(
  id: String,
  email: String,
  verifiedEmail: Boolean,
  name: String,
  givenName: String,
  familyName: String,
  link: String,
  picture: Option[String],
  gender: String,
  birthday: Option[String],
  locale: Option[String]
)

case class User(
  profile: Profile
)

object User {
  implicit val profileFormater = Json.format[Profile]
  implicit val formater = Json.format[User]

  def findByProfileId(id: String): Future[Option[User]] = {
    Future(None)
  }

  def fromSession(id: String): Future[Option[User]] = Future {
    Some(User(Profile(
      id = "1",
      email = "foo@bar.com",
      verifiedEmail = true,
      name = "Foo Bar",
      givenName = "Foo",
      familyName = "Bar",
      link = "",
      picture = None,
      gender = "male",
      birthday = None,
      locale = None
    )))
  }

  def create(profile: Profile) = {
    User(profile)
  }

  def createOrMerge(profile: Profile): Future[User] = {
    findByProfileId(profile.id).map {
      case Some(user) => user.copy(profile = profile)
      case None => User.create(profile = profile)
    }
    // TODO save user
  }
}
