package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.Play.current

import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.modules.reactivemongo.json.collection.JSONCollection
import play.autosource.reactivemongo._

import models._

object Users extends AuthenticatedController[User] {
  val collectionName = "users"

  def list = Action {
    Ok(views.html.users.users())
  }

  def detail = Action {
    Ok(views.html.users.user())
  }

  val borrowReader: Reads[String] = (__ \ 'isbn).read[String]
  def borrow(targetUserId: String) = AuthenticatedAction { implicit request => implicit user =>
    request.body.asJson match {
      case Some(json) =>
        borrowReader.reads(json) match {
          case JsSuccess(isbn, _) =>
            Async {
              user.borrowToUser(isbn, targetUserId).map { _ =>
                Ok("")
              }.recover {
                case BorrowException(msg) => BadRequest(msg)
              }
            }
          case JsError(errors) => BadRequest(s"Bad content: $errors")
        }
      case None => BadRequest("Missing content")
    }
  }

  val loanReader: Reads[String] = (__ \ 'isbn).read[String]
  def loan(targetUserId: String) = AuthenticatedAction { implicit request => implicit user =>
    request.body.asJson match {
      case Some(json) =>
        loanReader.reads(json) match {
          case JsSuccess(isbn, _) =>
            Async {
              user.borrowFromUser(isbn, targetUserId).map { _ =>
                Ok("")
              }.recover {
                case BorrowException(msg) => BadRequest(msg)
              }
            }
          case JsError(errors) => BadRequest(s"Bad content: $errors")
        }
      case None => BadRequest("Missing content")
    }
  }

  val renderReader: Reads[String] = (__ \ 'isbn).read[String]
  def render(targetUserId: String) = AuthenticatedAction { implicit request => implicit user =>
    request.body.asJson match {
      case Some(json) =>
        renderReader.reads(json) match {
          case JsSuccess(isbn, _) =>
            Async {
              user.renderToUser(isbn, targetUserId).map { _ =>
                Ok("")
              }.recover {
                case RenderException(msg) => BadRequest(msg)
              }
            }
          case JsError(errors) => BadRequest(s"Bad content: $errors")
        }
      case None => BadRequest("Missing content")
    }
  }

  def listBooks(targetUserId: String) = AuthenticatedAction { implicit request => implicit user =>
    Async {
      User.listBooks(targetUserId).map { books =>
        Ok(books)
      }
    }
  }

  val addBookReader: Reads[(String,Int)] = (
    (__ \ 'isbn).read[String] and
    (__ \ 'number).read[Int]
  ).tupled
  def addBook() = AuthenticatedAction { implicit request => implicit user =>
     request.body.asJson match {
      case Some(json) =>
        addBookReader.reads(json) match {
          case JsSuccess((isbn, number), _) =>
            Async {
              user.addBook(isbn, number).map { _ =>
                Ok("")
              }.recover {
                case AddBookException(msg) => BadRequest(msg)
              }
            }
          case JsError(errors) => BadRequest(s"Bad content: $errors")
        }
      case None => BadRequest("Missing content")
    }
  }

}
