package openlibrary

import play.api.libs.ws.WS
import play.api.libs.json._

import scala.concurrent.{Future, ExecutionContext}

case class OLBook(
  title: String,
  url: String,
  cover: Cover,
  authors: Seq[Author]
)

case class Cover(
  small: String,
  medium: String,
  large: String
)

case class Author(
  name: String,
  url: String
)

object Author {
  implicit val olAuthorFormat = Json.format[Author]
}

object Cover {
  implicit val olCoverFormat = Json.format[Cover]
}

object OLBook {
  implicit val olBookFormat = Json.format[OLBook]
}

// See the response: http://openlibrary.org/api/books?bibkeys=ISBN:0451526538,ISBN:9780262513593&format=json&jscmd=data
object OpenLibrary {
  import OLBook._

  val URL = "http://openlibrary.org/api/books"

  def bookInfo(isbn: String)(implicit ec: ExecutionContext): Future[Option[OLBook]] = booksInfo(Seq(isbn)).map(_.get(isbn))

  def booksInfo(isbns: Seq[String])(implicit ec: ExecutionContext): Future[Map[String, OLBook]] =
    WS.url(URL).withQueryString(
      "bibkeys" -> isbns.mkString(","),
      "format" -> "json",
      "jscmd" -> "data"
    ).get().map(_.json.as[Map[String, OLBook]])

}


