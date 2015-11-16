package com.mishawagner.sscdownloader.snippet

import com.mishawagner.sscdownloader.Organiser
import net.liftweb.common.{Empty, Full}
import net.liftweb.http.S

import scala.xml.NodeSeq

/**
 * Created by misha on 14/11/15.
 *
 * Handles input from user
 */
class DownloaderInput {
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
    val page = S param "page" match {
      case Full(s) => s
      case Empty => {
        println("Couldn't get page")
        return in
      }
    }

    val fileType = S param "filetype" getOrElse ""
    val threadAmount =
      try {
        S param "thread-amount" match {
          case Full(s) => s.toInt
          case Empty => 5
        }
      } catch {
        case e: NumberFormatException => 5
      }
    val location = S param "location" getOrElse "/home"

    println(s"$page, $fileType, $threadAmount")

    Organiser.start(page, fileType, threadAmount, location)

    in
  }
}
