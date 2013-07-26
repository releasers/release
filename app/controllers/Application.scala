package controllers

import play.api._
import play.api.mvc._
import openlibrary.OpenLibrary

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.Json

object Application extends Controller with Authentication {

  def main(url: String) = AuthenticatedAction { implicit request => implicit user =>
    Ok(views.html.templates.main())
  }

  def index = AuthenticatedAction { implicit request => implicit user =>
    Ok(views.html.index())
  }

  def login = Action {
    Ok("").withSession("user" -> "test")
  }

  def logout = Action {
    Ok("").withNewSession
  }

  def openLibraryInfo(isbn: String) = Action {
    Async {
      OpenLibrary.bookInfo(isbn) map {
        case Some(book) => Ok(Json.toJson(book))
        case None => NotFound
      }
    }
  }

  def searchOL(pattern: String) = Action {
    Async {
      OpenLibrary.bookSearch(pattern).map(books => Ok(Json.toJson(books.take(10))))
    }
  }

}
