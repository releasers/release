package models

import play.api.libs.json._
import reactivemongo.bson.BSONBinary
import reactivemongo.core.netty.ChannelBufferReadableBuffer
import org.jboss.netty.buffer.ChannelBuffers
import reactivemongo.bson.buffer.ReadableBuffer
import reactivemongo.bson.Subtype
import reactivemongo.bson.utils.Converters.{hex2Str, str2Hex}

case class Book (
  isbn: String,
  comments: Seq[Comment],
  title: String,
  description: String,
  picture: Option[BSONBinary]
  
)

object Book { 
  
  implicit object binaryFormater extends Format[BSONBinary] {
    def reads(json: JsValue): JsResult[BSONBinary] =
      json match {
        case JsString(str) => JsSuccess(BSONBinary(
          str2Hex(str),
          Subtype.GenericBinarySubtype
        ))
        case _ => JsError("expected string for binary")
      }

    def writes(bin: BSONBinary): JsValue =
      JsString(hex2Str(bin.value.readArray(bin.value.readable)))
  }

  implicit val formater = Json.format[Book]
}