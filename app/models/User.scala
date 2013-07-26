package models

import play.api.libs.json._
import play.api.Play.current
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.api.collections.default.BSONCollection
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.BSONFormats._
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import org.joda.time.DateTime

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
  locale: Option[String])

case class Loanable(
  isbn: String,
  borrower: Option[BSONObjectID],
  borrowedSince: Option[DateTime])

object Loanable {
  implicit val formater = Json.format[Loanable]
}

case class User(
    _id: BSONObjectID,
    profile: Profile,
    books: Seq[Loanable]
    ) {
  lazy val id = _id.stringify
}

object User {
  implicit val profileFormater = Json.format[Profile]
  implicit val formater = Json.format[User]

  implicit val profileHandler = Macros.handler[Profile]

  val collectionName = "users"
  val collection = ReactiveMongoPlugin.db.collection[JSONCollection](collectionName)

  def findByProfileId(id: String): Future[Option[User]] = {
    collection.find(BSONDocument("profile.id" -> id)).cursor[User].headOption
  }

  def fromSession(id: String): Future[Option[User]] = {
    collection.find(BSONDocument("_id" -> BSONObjectID(id))).cursor[User].headOption
  }

  def create(profile: Profile) = {
    User(
      _id = BSONObjectID.generate,
      profile = profile,
      books = Nil)
  }

  def createOrMerge(profile: Profile): Future[User] = {
    findByProfileId(profile.id).flatMap {
      case Some(u) =>
        val user = u.copy(profile = profile)
        collection.update(BSONDocument("_id" -> user._id), user).map(_ => user)
      case None =>
        val user = User.create(profile = profile)
        collection.insert(user).map(_ => user)
    }
  }
}
