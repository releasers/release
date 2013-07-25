package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.Play.current

import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.modules.reactivemongo.json.collection.JSONCollection
import play.autosource.reactivemongo._

import models.User

object Users extends AuthenticatedController[User] {
  val collectionName = "users"

  def list = Action {
    Ok(views.html.users.users())
  }

  def detail = Action {
    Ok(views.html.users.user())
  }


  def borrow = {
    // if remaining create a loan
    // else add to queue
    // val to: User
    // val from: Rack
    // val howMany: Int
  }

  def render = {
    // decrease/delete a loan
    // val loan: Loan OR val from: User, val to: Rack
    // val howMany: Int

    // prepare to send an alert if queue isn't empty
  }
}