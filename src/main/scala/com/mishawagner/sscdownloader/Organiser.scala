package com.mishawagner.sscdownloader

import com.mishawagner.sscdownloader.backend.{Downloader, LinkCrawler, LinkRetriever}
import com.mishawagner.sscdownloader.comet.CometNotifier

/**
 * Created by misha on 14/11/15.
 *
 * Organises the interactions between the Comet, the Snippet, and backend functions
 */
object Organiser {
  /**
   * What's notifying the user
   */
  private var notifier: Option[CometNotifier] = None

  /**
   * Object for getting linsk
   */
  private var retriever: Option[LinkRetriever] = None

  /**
   * Register the current notifier
   * @param notifier
   */
  def registerNotifier(notifier: CometNotifier): Unit = {
    this.notifier = Some(notifier)
  }

  /**
   * Get the downloaders
   * @return
   */
  def getDownloaders: Seq[Downloader] = retriever match {
    case Some(r) => r.downloaders
    case None => List()
  }

  /**
   * Start downloading
   * @param page Page to download from
   * @param fileType filetype to filter by
   * @param threadAmount amount of threads to use to download
   * @param location where to save the files
   */
  def start(page: String, fileType: String, threadAmount: Int, location: String): Unit = {
    val links = LinkCrawler.getFilteredLinksForPage(page, fileType)

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
    retriever = None
  }
}
