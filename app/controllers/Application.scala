package controllers

import play.api._
import play.api.mvc._

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

}
