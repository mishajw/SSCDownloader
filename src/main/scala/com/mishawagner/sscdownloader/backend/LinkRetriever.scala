package com.mishawagner.sscdownloader.backend

import java.util.concurrent._

/**
 * Created by misha on 12/11/15.
 *
 * Downloads a list of links
 */
class LinkRetriever(links: Seq[String], threadSize: Int, location: String, callback: () => Unit) {
  /**
   * Executor for downloaders
   */
  val exec = Executors.newFixedThreadPool(threadSize)
  /**
   * Downloaders
   */
  val downloaders: Seq[Downloader] = links.map(url => new Downloader(url, location, callback))

  /**
   * Start the download links
   */
  def downloadLinks(): Unit = downloaders.map(d => exec.submit(d))
}
