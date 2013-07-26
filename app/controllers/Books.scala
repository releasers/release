package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.autosource.reactivemongo._
import models.Book
import models.Comment
import org.joda.time.DateTime

object Books extends AuthenticatedController[Book] {
  val collectionName = "books"

  override def find = AuthenticatedAction { _ => _ =>
    Async {
      Book.findAll.map { list =>
        import Book.formater
        Ok(Json.toJson(list))
      }
    }
  }

  def list = Action {
    Ok(views.html.books.books())
  }

  def detail = Action {
    Ok(views.html.books.book())
  }

  private val commentTransform = (
    (__ \ 'message).read[String] and
    ((__ \ 'rate).read[Int](Reads.max(5) keepAnd Reads.min(0)))).tupled

  // Get comments for this book
  def getComments(id: String) = Action {
    Async {
      coll.find(Json.obj("_id" -> id), Json.obj("comments" -> 1, "_id" -> 0)).cursor[JsObject].toList.map { comments =>
        Ok(Json.arr(comments))
      }.recover {
        case e: Throwable => InternalServerError(e.getMessage())
      }
    }
  }

  // Add a comment to the book
  def addComment(id: String) = Authenticated { user =>
    Action { request =>
      request.body.asJson.map { json =>
        println(json.as[JsObject].values)
        val modifier = commentTransform.reads(json).map(tuple => Json.obj(
          "$push" -> Json.obj(
            "comments" -> Comment(user._id, new DateTime(), tuple._1, tuple._2))))
        modifier.map { modifier =>
          Async(coll.update(Json.obj("_id" -> id), modifier).filter(_.ok).map(_ => Ok).recover {
            case e: Throwable => InternalServerError(e.toString)
          })
        }.recover {
          case JsError(e) =>
            val errors = e.flatMap(_._2)
            BadRequest(errors.toString)
        }.get
      }.getOrElse {
        BadRequest("JSON expected")
      }
    }
  }
}
