package controllers

import org.htmlcleaner.HtmlCleaner

import scala.io.Source

/**
 * Created by misha on 12/11/15.
 *
 * Helps retrieve links from a webpage
 */
object LinkCrawler {

  def getFilteredLinksForPage(url: String, suff: String) =
    getLinksForPage(url).filter(link => link.endsWith(suff))

  def getLinksForPage(url: String): Seq[String] =
    this getLinksForHTML (url, this getHTMLForPage url)

  private def getHTMLForPage(url: String): String =
    (Source fromURL url).mkString

  private def getLinksForHTML(baseUrl: String, html: String): Seq[String] = {
    val cleaner = new HtmlCleaner
    val root = cleaner clean html
    val results =
        (root getElementsHavingAttribute ("href", true)) ++
        (root getElementsHavingAttribute ("src", true))
    
    results
      .map(node => node getAttributeByName (node.hasAttribute("href") match {
        case true  => "href"
        case false => "src"
      }))
      .map(link => if (link.startsWith("http")) link else baseUrl + link)
  }
}
