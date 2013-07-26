package models

import org.jboss.netty.buffer.ChannelBuffers
import play.api.libs.json._
import play.api.Play.current
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.BSONFormats._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONBinary
import reactivemongo.bson.Subtype
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.buffer.ReadableBuffer
import reactivemongo.bson.utils.Converters.{hex2Str, str2Hex}
import reactivemongo.core.commands.LastError
import reactivemongo.core.netty.ChannelBufferReadableBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

case class Book (
  _id: String,
  comments: Seq[Comment],
  title: String,
  description: String,
  picture: Option[BSONBinary],
  pictureUrl: Option[String]
) {
  @inline def isbn = _id
}

object Book {

  implicit val formater = Json.format[Book]

  val collectionName = "books"
  val collection = ReactiveMongoPlugin.db.collection[JSONCollection](collectionName)

  def findById(id: String): Future[Option[Book]] = {
    collection.find(BSONDocument("_id" -> BSONObjectID(id))).cursor[Book].headOption
  }

  def findAll(): Future[Seq[Book]] = {
    collection.find(Json.obj()).cursor[Book].toList
  }

  def findAllByIsbns(isbns: Seq[String]): Future[Seq[Book]] = {
    collection.find(BSONDocument("_id" -> BSONDocument("$in" -> isbns))).cursor[Book].toList
  }

  def create(book: JsObject) = {
    collection.insert(book ++ Json.obj(
      "_id" -> (book \ "isbn"),
      "comments" -> Json.arr()
    ))
  }

}
