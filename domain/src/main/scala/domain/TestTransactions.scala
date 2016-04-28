package domain

import domain.entity.User
import domain.repository.scalikejdbc.DomainDB
import domain.service.UserService

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

import org.pankuzu.util.Log.{log}
/**
  * Created by elyzion on 4/26/16.
  */
object TestTransactions {
  DomainDB.setup()
  DomainDB.createTables()
  def getValue[A](f: Future[A]): A = Await.result(f, Duration(300, "seconds"))

  def main(args: Array[String]): Unit = {
    log("getValue(UserService.create(\"olive\")) ************** start")
    val olive = getValue(UserService.create("olive"))
    log(s"$olive")
    log("end ********************************************************")
    getValue(UserService.create3("a", "b", "c"))
    val u = getValue(UserService.create("test"))
    getValue(UserService.read(u.id))
    getValue(UserService.create3CommitEach("aa", "bb", "cc"))

  }

}