package fujitask

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import _root_.scalikejdbc._

import org.pankuzu.util.Log.{log}

package object scalikejdbc {

  def ask: Task[Transaction, DBSession] =
    new Task[Transaction, DBSession] {
      def execute(transaction: Transaction)(implicit ec: ExecutionContext): Future[DBSession] = {
        log()
        Future.successful(transaction.asInstanceOf[ScalikeJDBCTransaction].session)
      }
    }

  implicit def readRunner[R >: ReadTransaction] : TaskRunner[R] =
    new TaskRunner[R] {
      def run[A](task: Task[R, A]): Future[A] = {
        log()
        println("ReadRunner")
        val session = DB.readOnlySession()
        val future = task.execute(new ScalikeJDBCReadTransaction(session))
        future.onComplete(_ => session.close())
        future
      }
    }

  implicit def readWriteRunner[R >: ReadWriteTransaction]: TaskRunner[R] =
    new TaskRunner[R] {
      def run[A](task: Task[R, A]): Future[A] = {
        log()
        println("ReadWriteRunner")
        DB.futureLocalTx(session => task.execute(new ScalikeJDBCReadWriteTransaction(session)))
      }
    }

}
