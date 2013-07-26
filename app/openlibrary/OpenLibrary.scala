package openlibrary

import play.api.libs.ws.WS
import play.api.libs.json._
import play.api.Logger

import scala.concurrent.{Future, ExecutionContext}

import models.Book

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
  val LOGGER = Logger("OpenLibrary")

  import OLBook._

  val URL = "http://openlibrary.org/api/books"

  def bookInfo(isbn: String)(implicit ec: ExecutionContext): Future[Option[OLBook]] = booksInfo(Seq(isbn)).map(_.get(isbn))

  def booksInfo(isbns: Seq[String])(implicit ec: ExecutionContext): Future[Map[String, OLBook]] =
    WS.url(URL).withQueryString(
      "bibkeys" -> isbns.mkString(","),
      "format" -> "json",
      "jscmd" -> "data"
    ).get().map(_.json.as[Map[String, OLBook]])

  def bookSearch(pattern: String)(implicit ec: ExecutionContext): Future[List[JsObject]] =
    WS.url("http://openlibrary.org/search.json").withQueryString("q" -> pattern).get.map(_.json).map { json =>
      val docs = (json \ "docs").as[JsArray]

      docs.value.flatMap { jsDoc =>
        val maybeIsbn = (jsDoc \ "isbn").asOpt[List[String]].map(_(0))
        val maybeAuthor = (jsDoc \ "author_name").asOpt[List[String]].map(_(0))
        val maybeTitle = (jsDoc \ "title").asOpt[String]
        (maybeIsbn, maybeTitle, maybeAuthor) match {
          case (Some(isbn), Some(title), Some(author)) => Some(Json.obj(
            "isbn" -> isbn,
            "title" -> title,
            "description" -> author,
            "pictureUrl" -> s"http://covers.openlibrary.org/b/isbn/$isbn-M.jpg"
          ))
          case _ => None
        }
      }.toList
    }
}


