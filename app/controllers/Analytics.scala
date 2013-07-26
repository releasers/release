package controllers

import play.api._
import play.api.mvc._

object Analytics extends Controller with Authentication {
  
  def main = AuthenticatedAction { implicit request => implicit user =>
    Ok(views.html.analytics.main())
  }
}