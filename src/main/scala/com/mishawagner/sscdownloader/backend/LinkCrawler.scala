package com.mishawagner.sscdownloader.backend

import grizzled.slf4j.Logging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by misha on 12/11/15.
 *
 * Helps retrieve links from a webpage
 */
object LinkCrawler extends Logging {

  private val LINK_ATTR = Array("href", "src")


  /**
   * Get all the links from a page, filtered by suffix
   * @param url page url
   * @param suffs list of suffixes of files to return
   * @return list of links
   */
  def getFilteredLinksForPage(url: String, suffs: List[String]) = {
    val links = getLinksForPage(url)
    suffs.contains("All") match {
      case true => links
      case false => links.filter(link =>
        suffs.foldLeft(false)((p, suff) =>
          p || (link.toLowerCase endsWith suff.toLowerCase)))
    }
  }

  def getLinksForPage(url: String): List[String] = {
    try {
      val doc = Jsoup.connect(url).get()

      LINK_ATTR
        .flatMap(doc.getElementsByAttribute(_).toArray)
        .map(_.asInstanceOf[Element])
        .map(e => e.attr("abs:" + LINK_ATTR.filter(e.hasAttr).head))
        .toList
    } catch {
      case e: Throwable => List()
    }
  }
}
