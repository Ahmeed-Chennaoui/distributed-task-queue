import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.session.Session
import config.Config.keyspace
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.PrivateMethodTester
import controllers.TaskControllers
import controllers.TaskControllers.prepareQuery
import models.TaskModel.Task
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.wordspec.AnyWordSpec
import spray.json.JsObject


class TaskControllersSpec extends AnyWordSpec with Matchers with PrivateMethodTester
 {


  val getTaskPeriod: PrivateMethod[String] = PrivateMethod[String]('getTaskPeriod)

  val periodTestCases : List[(String,Long,String)] = List(
    ("Day", 1617583199000L, "5-4-2021"),
    ("Week", 1617583199000L, "15-2021"),
    ("Month", 1617583199000L, "4-2021"),
    ("Quarter", 1617583199000L, "2-2021"),
    ("Year", 1617583199000L, "2021"),
    ("InvalidType", 1617583199000L, null)
  )

  "getTaskPeriod" should{
    periodTestCases.foreach { case (periodType, timestamp, expectedResult) =>
      s"return ${periodType}" in {
        if (expectedResult != null) {
          val result =
            TaskControllers invokePrivate getTaskPeriod(timestamp, periodType)
          result shouldEqual expectedResult
        } else {
          assertThrows[Exception] {
            TaskControllers invokePrivate getTaskPeriod(timestamp, periodType)
          }
        }
      }

    }
  }


}
