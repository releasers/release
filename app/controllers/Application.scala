package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def main(url: String) = Action {
    Ok(views.html.templates.main())
  }

  def index = Action {
    Ok(views.html.index())
  }

  def login = Action {
    Ok("").withSession("user" -> "test")
  }

  def logout = Action {
    Ok("").withNewSession
  }
  
}