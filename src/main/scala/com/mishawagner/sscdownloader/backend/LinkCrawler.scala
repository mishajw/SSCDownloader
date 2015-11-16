package com.mishawagner.sscdownloader.backend

import java.io.IOException
import java.net.MalformedURLException

import org.htmlcleaner.HtmlCleaner

import scala.io.Source

/**
 * Created by misha on 12/11/15.
 *
 * Helps retrieve links from a webpage
 */
object LinkCrawler {

  /**
   * Get all the links from a page, filtered by suffix
   * @param url page url
   * @param suff suffix of files to return
   * @return list of links
   */
  def getFilteredLinksForPage(url: String, suff: String) =
    getLinksForPage(url).filter(link => link.toLowerCase.endsWith(suff.toLowerCase))

  /**
   * Get all the links on a page
   * @param url page url
   * @return list of links
   */
  def getLinksForPage(url: String): Seq[String] =
    this getLinksForHTML (url, this getHTMLForPage url)

  /**
   * Get the html for a page
   * @param url url to fetch HTML from
   * @return
   */
  private def getHTMLForPage(url: String): String =
    try {
      (Source fromURL url).mkString
    } catch {
      case e: MalformedURLException =>
        println("Not a valid URL.") ; ""
      case e: IOException =>
        println("Couldn't reach URL.") ; ""
    }

  /**
   * Get links from a HTML string
   * @param baseUrl The original url of the page
   * @param html the html
   * @return list of links
   */
  private def getLinksForHTML(baseUrl: String, html: String): Seq[String] = {
    val cleaner = new HtmlCleaner
    val root = cleaner clean html
    val results =
        (root getElementsHavingAttribute ("href", true)) ++
        (root getElementsHavingAttribute ("src", true))
    
    results
      .map(node => node getAttributeByName { if (node.hasAttribute("href")) "href" else "src" })
      .map(link => if (link.startsWith("http")) link else baseUrl + {
        if (baseUrl.endsWith("/") || link.startsWith("/")) link else "/" + link
      })
  }
}