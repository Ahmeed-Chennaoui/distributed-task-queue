import org.scalatest.matchers.should.Matchers
import models.TaskModel.{Task, parseTask}
import org.scalatest.wordspec.AnyWordSpec
import controllers.TaskControllers.attemptTask
import models.TaskParamsModel.{Task1Params, parseTaskParams}

class TaskModelSpec extends AnyWordSpec with Matchers {
  "parseTask" should {
    "parse task successfully" in {
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
      val result = parseTask(task.toJson.toString())
      result shouldBe a[Option[Task]]

    }
    "not parse task" in {

      val result = parseTask("")
      result shouldBe None

    }
  }
  "parseTask" should {
    "not parse invalid task and return none" in {
      val taskParams = ""
      val result = parseTaskParams[Task1Params](taskParams)
      result shouldBe None

    }
  }
}
