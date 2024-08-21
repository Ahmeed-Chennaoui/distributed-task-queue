import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.session.Session
import config.Config.keyspace
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, PrivateMethodTester}
import controllers.TaskControllers
import models.TaskModel.Task
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.wordspec.AnyWordSpec
import controllers.TaskControllers.{addTaskToQueue, loadEligibleTaskListFromDB}
import org.apache.kafka.clients.producer.KafkaProducer
import org.cassandraunit.CQLDataLoader
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet
import org.scalatestplus.mockito.MockitoSugar
import spray.json.JsObject

import scala.util.{Failure, Success}
class TaskControllersSpec extends AnyWordSpec with Matchers with MockitoSugar with BeforeAndAfterAll  {
//  implicit var session : CqlSession = _
//  override def beforeAll(): Unit = {
//    EmbeddedCassandraServerHelper.startEmbeddedCassandra("cassandra.yml")
//    val loader = new CQLDataLoader(EmbeddedCassandraServerHelper.getSession)
//    loader.load(new ClassPathCQLDataSet("InitData.cql"))
//    session = EmbeddedCassandraServerHelper.getSession
//  }
//
//  override def afterAll(): Unit = {
//    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
//    super.afterAll()
//  }
  "loadEligibleTaskListFromDB" should {
    "return Some(List[Task]) when eligible tasks are found" in {
      val result = loadEligibleTaskListFromDB /*(session)*/
      result shouldBe a[Option[List[_]]]

    }
  }
  "addTaskToQueue" should {
    "add task" in {
      val mockProducer = mock[KafkaProducer[String,String]]
      val task: Task = Task(
        task_id = "a",
        task_name = "a",
        task_type = "a",
        parameters = "{}",
        timeout = 5,
        priority = 0,
        dependencies = List(),
        state = "Pending",
        attempts = 0,
        period = ""
      )
      val result = addTaskToQueue(task,mockProducer)
      result shouldBe a[Failure[Unit]]

    }
  }
}
