import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import akka.util.Timeout
import models.TaskModel.Task
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import routes.TaskRoutes.addTask
import spray.json.JsObject

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class RouteSpec extends AnyWordSpecLike with Matchers with ScalatestRouteTest {
  implicit val timeout: Timeout = Timeout(5.seconds)
  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(5.seconds)


  "TaskRoutes" should {
    "add a task successfully" in {
      val taskJson =Task( task_name="a",
        task_type="a",
        parameters=new JsObject(Map()),
        timeout=5,
        priority= None,
        dependencies=None).toJson.toString()

      Post("/task").withEntity(HttpEntity(ContentTypes.`application/json`, taskJson)) ~> addTask ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "Task created successfully."
      }
    }
//    "fail to parse task" in {
//      val taskJson =""
//
//      Post("/task").withEntity(HttpEntity(ContentTypes.`application/json`, taskJson)) ~> addTask ~> check {
//        status shouldBe StatusCodes.BadRequest
//        responseAs[String] shouldBe "Invalid Parameters."
//      }
//    }

  }
}