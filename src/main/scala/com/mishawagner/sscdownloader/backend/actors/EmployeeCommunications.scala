package com.mishawagner.sscdownloader.backend.actors

object EmployeeCommunications {
  sealed trait WorkerMessage

  // To worker
  case class Work(file: Runnable) extends WorkerMessage
  case class Finished() extends WorkerMessage

  // To master
  case class GiveWork(work: Seq[Runnable]) extends WorkerMessage
  case class StartWorkers(amountOfWorkers: Int) extends WorkerMessage
  case class WorkDone() extends WorkerMessage
}
