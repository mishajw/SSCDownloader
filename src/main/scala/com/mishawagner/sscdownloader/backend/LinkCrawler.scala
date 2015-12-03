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

  /**
    * Get all the links for a URL
    * @param url the url to get
    * @return list of links
    */
  def getLinksForPage(url: String): List[String] = try {
    LINK_ATTR
      .flatMap(attr =>
        Jsoup
          // Get the HTML
          .connect(url)
          .get()
          // Get all elements with certain attr
          .getElementsByAttribute(attr)
          // Cast to Array[Element]
          .toArray.map(_.asInstanceOf[Element])
          // Map to attr values
          .map(_.attr(s"abs:$attr"))
      ).toList
  } catch {
    case e: Throwable => List()
  }
}
