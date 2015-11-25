package com.mishawagner.sscdownloader.snippet

import com.mishawagner.sscdownloader.Organiser
import grizzled.slf4j.Logging
import net.liftweb.http.S

import scala.util.Try
import scala.xml.{Attribute, NodeSeq, Null, Text}

/**
 * Created by misha on 14/11/15.
 *
 * Handles input from user
 */
class DownloaderInput extends Logging {
  /**
   * For the body of the HTML
   * @param in nodes
   * @return
   */
  def body(in: NodeSeq): NodeSeq = {
    Organiser.reset()
    in
  }

  /**
   * When the submit button is pressed
   * @param in nodes
   * @return
   */
  def buttonPressed(in: NodeSeq): NodeSeq = {
    for {
      page <- S.param("page")
//      fileTypes <- S.params("filetype")
      otherFiletypes <- S.param("other-filetypes")
      threadAmount <- S.param("thread-amount")
      location <- S.param("location")
    }
    {
      val fileTypes = S.params("filetype")
      val allFileTypes: List[String] = fileTypes ++
        otherFiletypes.split(",")
          .map(_.replace("""(?m)\s+$""", ""))
          .toList
          .filter(_.nonEmpty)

      info(s"Form submitted with details: $page, $allFileTypes, $threadAmount")

      Organiser.start(
        page,
        allFileTypes,
        Try(threadAmount.toInt).getOrElse(2),
        location)

      S redirectTo "/results.html"
    }


    in
  }

  def filetypeInput = List("All", ".png", ".gif", ".jpeg", ".pdf", ".zip").map(s =>
      <div class="filetype-container">
        { <input type="checkbox" name="filetype" /> % Attribute(None, "value", Text(s), Null) }
        <div>{s}</div>
      </div>
    )
}
