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

object Books extends AuthenticatedController[Book] {
  val collectionName = "books"

  def list = Action {
    Ok(views.html.books.books())
  }

  def detail = Action {
    Ok(views.html.books.book())
  }

  def comment = {
    // Add a comment to the book
  }

  def rate = {
    // rate a book from 0 to 5
  }
}