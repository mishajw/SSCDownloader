package com.mishawagner.sscdownloader.backend

import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

import grizzled.slf4j.Logging

import scala.xml.{Attribute, Null, Text}

/**
 * Created by misha on 13/11/15.
 *
 * Downloads a link
 */
class FileDownload(url: String, location: String, callback: () => Unit) extends Runnable with Logging {

  /**
   * Possible states for the download
   */
  abstract class DownloadState()
  case class NotStarted() extends DownloadState
  case class InProgress() extends DownloadState
  case class Finished() extends DownloadState
  case class Failed() extends DownloadState

  /**
   * Current download state
   */
  private var _state: DownloadState = NotStarted()

  /**
   * File name of the download
   */
  private val fileName = url.split("/").last

  /**
   * Getter for state
   * @return state
   */
  def state = _state

  /**
   * Setter for state, notifies Organiser of change
   * @param newState what to set the state to
   */
  def state_=(newState: DownloadState) = {
    callback()
    _state = newState
  }

  override def run(): Unit = {
    state = InProgress()
    download()

    if (state != Failed())
      state = Finished()
  }

  /**
   * Download the link
   */
  private def download() = {
    info("Downloading a file")
    try {
//      new URL(url) #> new File(location + fileName) !!
      val urlObj = new URL(url)
      val rbc = Channels.newChannel(urlObj.openStream())
      val fos = new FileOutputStream(location + fileName)
      fos.getChannel.transferFrom(rbc, 0, Long.MaxValue)
      fos.close()
      rbc.close()
    } catch {
      case e: Exception =>
        e.printStackTrace()
        state = Failed()
    }
  }

  /**
   * Cast the downloader to HTML
   * @return
   */
  def toHtml =
    <tr class="downloader-details">
      { stateToHtml }
      { nameToHtml }
    </tr>

  /**
   * Cast the name to HTML
   * @return
   */
  def nameToHtml =
    <td class="downloader-attr">
      {url}
    </td>

  /**
   * Cast the state to HTML
   * @return
   */
  def stateToHtml = {
    val (labelType, text) = this.state match {
      case NotStarted() => ("danger", "Not started")
      case InProgress() => ("warning", "In progress")
      case Finished() => ("success", "Finished")
      case Failed() => ("danger", "Download failed")
    }

    <td class="downloader-attr downloader-status">
      { <div> {text} </div> %
        Attribute(None, "class", Text("label label-" + labelType), Null) }
    </td>
  }
}