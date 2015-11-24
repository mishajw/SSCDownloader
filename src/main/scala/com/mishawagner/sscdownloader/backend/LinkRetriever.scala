package com.mishawagner.sscdownloader.backend

import akka.actor.{ActorSystem, Props}
import com.mishawagner.sscdownloader.backend.actors.EmployeeCommunications.{GiveWork, StartWorkers}
import com.mishawagner.sscdownloader.backend.actors.MasterActor

/**
 * Created by misha on 12/11/15.
 *
 * Downloads a list of links
 */
class LinkRetriever(links: Seq[String], threadSize: Int, location: String, callback: () => Unit) {
  /**
   * Downloaders
   */
  val downloaders: Seq[FileDownload] = links.map(url => new FileDownload(url, location, callback))

  /**
   * Start the download links
   */
  def downloadLinks(): Unit = {
    val system = ActorSystem("download-system")
    val master = system.actorOf(Props[MasterActor], "master0")

    master ! GiveWork(downloaders)
    master ! StartWorkers(threadSize)
  }
}
