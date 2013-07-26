package models

import play.api.libs.json._
import play.api.Play.current
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.core.commands.LastError
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.BSONFormats._
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import org.joda.time.DateTime

case class BorrowException(msg: String) extends Throwable(msg)
case class RenderException(msg: String) extends Throwable(msg)
case class AddBookException(msg: String) extends Throwable(msg)
case class RemoveBookException(msg: String) extends Throwable(msg)

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
  addedDate: DateTime,
  borrowerId: Option[BSONObjectID],
  borrowedSince: Option[DateTime])

case class SuggestionVote(
  who: BSONObjectID,
  when: DateTime)

object SuggestionVote {
  implicit val formater = Json.format[SuggestionVote]
}

case class Suggestion(
  isbn: String,
  addedDate: DateTime,
  origin: BSONObjectID,
  approved: Seq[SuggestionVote],
  disapproved: Seq[SuggestionVote])

object Suggestion {
  implicit val formater = Json.format[Suggestion]
}

object Loanable {
  implicit val formater = Json.format[Loanable]
}

case class User(
    _id: BSONObjectID,
    profile: Profile,
    suggestions: Seq[Suggestion],
    books: Seq[Loanable],
    queue: Seq[Loanable]) {
  lazy val id = _id.stringify

  def borrowFromUser(isbn: String, byUserId: String): Future[LastError] = {
    borrowFromUser(isbn, BSONObjectID(byUserId))
  }
  def borrowFromUser(isbn: String, byUserId: BSONObjectID): Future[LastError] = {
    var success = false  // local var to make code readable
    var seen = false
    val updated = books.map { book =>
      if (success) book
      else if (book.isbn != isbn) book
      else {
        seen = true
        if (book.borrowerId.isEmpty) {
          success = true
          book.copy(borrowerId = Some(byUserId), borrowedSince = Some(DateTime.now))
        }
        else book
      }
    }
    if (success) {
      import Loanable.formater
      User.collection.update(
        Json.obj("_id" -> _id),
        Json.obj("$set" -> Json.obj("books" -> updated))
      )
    }
    else {
      val msg = if (seen) s"Book '$isbn' is not available"
                else s"Requested user doesn have the book '$isbn'"
      Future.failed(BorrowException(msg))
    }
  }

  def borrowToUser(isbn: String, targetUserId: String): Future[LastError] = {
    User.findById(targetUserId).flatMap {
      case Some(targetUser) => targetUser.borrowFromUser(isbn, _id)
      case None =>
        Future.failed(BorrowException(s"Can't borrow to not-existing user: $targetUserId"))
    }
  }

def renderFromUser(isbn: String, byUserId: BSONObjectID): Future[LastError] = {
    var success = false  // local var to make code readable
    var seen = false
    val updated = books.map { book =>
      if (success) book
      else if (book.isbn != isbn) book
      else if (book.borrowerId != Some(byUserId)) book
      else {
        success = true
        book.copy(borrowerId = None, borrowedSince = None)
      }
    }
    if (success) {
      import Loanable.formater
      // prepare to send an alert if queue isn't empty
      User.collection.update(
        Json.obj("_id" -> _id),
        Json.obj("$set" -> Json.obj("books" -> updated))
      )
    }
    else {
      val msg = s"Can't render the book '$isbn'"
      Future.failed(RenderException(msg))
    }
  }


  def renderToUser(isbn: String, targetUserId: String): Future[LastError] = {
    User.findById(targetUserId).flatMap {
      case Some(targetUser) => targetUser.renderFromUser(isbn, _id)
      case None =>
        Future.failed(RenderException(s"Can't render to not-existing user: $targetUserId"))
    }
  }

  def addBook(isbn: String, number: Int) : Future[LastError] = {  
    if(number < 1) Future.failed(AddBookException(s"Number ':number' must be >1"))
    else {
      val updated = books ++ {
        for(i <- 1 to number) yield {
          Loanable(isbn, DateTime.now, None, None)
        }
      }
      User.collection.update(
          Json.obj("_id" -> _id),
          Json.obj("$set" -> Json.obj("books" -> updated))
        ) 
    }
  }

  def removeBook(isbn:String) : Future[LastError] = {
    val (head,tail) = books.span(_.isbn != isbn)
    if(tail.isEmpty) Future.failed(RemoveBookException(s"Can't remove not-existing book: $isbn"))
    else {
      val updated = head ++ tail.drop(1)

      User.collection.update(
        Json.obj("_id" -> _id),
        Json.obj("$set" -> Json.obj("books" -> updated))
      )
    }
  }
}

object User {
  implicit val profileFormater = Json.format[Profile]
  implicit val formater = Json.format[User]

  implicit val profileHandler = Macros.handler[Profile]

  val collectionName = "users"
  val collection = ReactiveMongoPlugin.db.collection[JSONCollection](collectionName)

  def findById(id: String): Future[Option[User]] = {
    collection.find(BSONDocument("_id" -> BSONObjectID(id))).cursor[User].headOption
  }

  def findAll(): Future[Seq[User]] = {
    collection.find(Json.obj()).cursor[User].toList
  }

  def findByProfileId(id: String): Future[Option[User]] = {
    collection.find(BSONDocument("profile.id" -> id)).cursor[User].headOption
  }

  def fromSession(id: String): Future[Option[User]] = findById(id)

  def create(profile: Profile) = {
    User(
      _id = BSONObjectID.generate,
      profile = profile,
      suggestions = Nil,
      books = Nil,
      queue = Nil)
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

  def listBooks(userId: String): Future[JsArray] = {
    findById(userId).flatMap {
      case Some(user) => listBooks(user)
      case None => Future { Json.arr() }
    }
  }
  def listBooks(user: User): Future[JsArray] = {
    val isbns = user.books.map(_.isbn).toSet.toSeq
    Book.findAllByIsbns(isbns).map { realBooks =>
      val booksMap = realBooks.foldLeft(Map.empty[String, Book]){ case (books, book) =>
        books + (book.isbn -> book)
      }
      val books = user.books.map{ book =>
        Json.toJson(book) match {
          case jso: JsObject =>
            jso ++ Json.obj(
              "book" -> booksMap.get(book.isbn)
            )
        }
      }
      JsArray(books)
    }
  }

  def listLoans(userId: String): Future[JsArray] = {
    findById(userId).flatMap {
      case Some(user) => listLoans(user)
      case None => Future { Json.arr() }
    }
  }
  def listLoans(user: User): Future[JsArray] = {
    val isbns = user.books.map(_.isbn).toSet.toSeq
    collection.find(Json.obj("books.borrowerId" -> user._id)).cursor[User].toList.map{ users =>
      JsArray(users.flatMap { u =>
        u.books
         .filter(_.borrowerId == Some(user._id))
         .map(Json.toJson(_) match {
            case jso: JsObject => jso ++ Json.obj("userId" -> u._id)
          })
      })
    }
  }

}
