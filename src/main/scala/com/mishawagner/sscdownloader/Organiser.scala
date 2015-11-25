package com.mishawagner.sscdownloader

import com.mishawagner.sscdownloader.backend.{FileDownload, LinkCrawler, LinkRetriever}
import com.mishawagner.sscdownloader.comet.CometNotifier
import grizzled.slf4j.Logging

/**
 * Created by misha on 14/11/15.
 *
 * Organises the interactions between the Comet, the Snippet, and backend functions
 */
object Organiser extends Logging {
  /**
   * What's notifying the user
   */
  private var notifier: Option[CometNotifier] = None

  /**
   * Object for getting links
   */
  private var retriever: Option[LinkRetriever] = None

  /**
   * Register the current notifier
   * @param notifier Responsible for updating client
   */
  def registerNotifier(notifier: CometNotifier): Unit = {
    info("Registered a CometNotifier")
    this.notifier = Some(notifier)
  }

  /**
   * Get the downloaders
   * @return
   */
  def getDownloaders: Seq[FileDownload] = retriever match {
    case Some(r) => r.downloaders
    case None => List()
  }

  /**
   * Start downloading
   * @param page Page to download from
   * @param fileTypes filetype to filter by
   * @param threadAmount amount of threads to use to download
   * @param location where to save the files
   */
  def start(page: String, fileTypes: List[String], threadAmount: Int, location: String): Unit = {
    info("Starting downloading process")

    val links = LinkCrawler.getFilteredLinksForPage(page, fileTypes)

    if (links.isEmpty) {
      this.reset()
      return
    }

    val retriever = new LinkRetriever(links, threadAmount, location, () => notifier match {
      case Some(n) => n.reRender()
      case None =>
    })
    this.retriever = Some(retriever)

    retriever.downloadLinks()
  }

  /**
   * Reset the downloading
   */
  def reset() = {
    info("Resetting it all")
    retriever = None
    notifier match {
      case Some(n) => n.reRender()
      case _ =>
    }
  }
}
