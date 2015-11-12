package controllers

import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getLinks = Action {
    Ok("getLinks called:\n" +
      LinkCrawler.getFilteredLinksForPage("http://google.com", ".png").mkString("\n") + "\n\nNot filtered:\n" +
      LinkCrawler.getFilteredLinksForPage("http://google.com", "").mkString("\n"))
  }
}
