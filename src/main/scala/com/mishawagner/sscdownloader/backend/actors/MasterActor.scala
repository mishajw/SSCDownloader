package com.mishawagner.sscdownloader.backend.actors

import java.util.concurrent.ConcurrentLinkedQueue

import akka.actor.{Actor, ActorRef, Props}
import com.mishawagner.sscdownloader.backend.actors.EmployeeCommunications._
import grizzled.slf4j.Logging

class MasterActor extends Actor with Logging {
  private val workQueue = new ConcurrentLinkedQueue[Runnable]()

  override def receive = {
    case GiveWork(work) =>
      work foreach workQueue.add
    case StartWorkers(amountOfWorkers) =>
      startWithWorkers(amountOfWorkers)
    case WorkDone() =>
      giveWork(sender())
    case n => info(s"Unrecognised message: $n")
  }

  private def startWithWorkers(amountOfWorkers: Int): Unit = {
    for (i <- 0 until amountOfWorkers) {
      val worker = context.actorOf(Props[WorkerActor], "worker" + i)
      giveWork(worker)
    }
  }

  private def giveWork(ref: ActorRef) = workQueue.poll() match {
    case null => ref ! Finished()
    case work => ref ! Work(work)
  }
}
