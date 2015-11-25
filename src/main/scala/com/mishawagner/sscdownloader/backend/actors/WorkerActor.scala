package com.mishawagner.sscdownloader.backend.actors

import akka.actor.Actor
import com.mishawagner.sscdownloader.backend.actors.EmployeeCommunications._
import grizzled.slf4j.Logging

class WorkerActor extends Actor with Logging {
  override def receive = {
    case Work(file) =>
      file.run()
      sender() ! WorkDone()
    case Finished() =>
      info("Download worker finished.")
    case n =>
      info(s"Unrecognised message: $n")
  }
}
