package models

import play.api.libs.json._
import reactivemongo.bson.BSONBinary
import reactivemongo.core.netty.ChannelBufferReadableBuffer
import org.jboss.netty.buffer.ChannelBuffers
import reactivemongo.bson.buffer.ReadableBuffer
import reactivemongo.bson.Subtype
import reactivemongo.bson.utils.Converters.{hex2Str, str2Hex}

case class Book (
  _id: String,
  comments: Seq[Comment],
  title: String,
  description: String,
  picture: Option[BSONBinary]
) {
  @inline def isbn = _id
}

object Book {

  implicit val formater = Json.format[Book]

}
