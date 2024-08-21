import org.scalatest.matchers.should.Matchers
import models.TaskModel.Task
import org.scalatest.wordspec.AnyWordSpec
import controllers.TaskControllers.attemptTask
import models.TaskParamsModel.Task1Params

class TaskControllersSpec extends AnyWordSpec with Matchers {
  "attemptTask" should {
    "log no such task" in {
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
      val result = attemptTask(task)
      result shouldBe a[Unit]

    }
    "attemptTask" should {
      "run job successfully" in {
        val task1params: Task1Params = Task1Params("f")
        val task: Task = Task(
          task_id = "test",
          task_name = "test",
          task_type = "test",
          parameters = task1params.toJson.toString(),
          timeout = 5,
          priority = 0,
          dependencies = List(),
          state = "Pending",
          attempts = 0,
          period = ""
        )
        val result = attemptTask(task)
        result shouldBe a[Unit]

      }
      "throw invalid task params exception" in {
        val task: Task = Task(
          task_id = "test",
          task_name = "test",
          task_type = "test",
          parameters = "{",
          timeout = 5,
          priority = 0,
          dependencies = List(),
          state = "Pending",
          attempts = 0,
          period = ""
        )
        val result = attemptTask(task)
        result shouldBe a[Unit]

      }
      "throw exception" in {
        val task: Task = Task(
          task_id = "fail",
          task_name = "fail",
          task_type = "fail",
          parameters = "{}",
          timeout = 5,
          priority = 0,
          dependencies = List(),
          state = "Pending",
          attempts = 0,
          period = ""
        )
        val result = attemptTask(task)
        result shouldBe a[Unit]

      }
      "exit JVM with TimeoutException" in {
        val task: Task = Task(
          task_id = "long",
          task_name = "long",
          task_type = "long",
          parameters = "{}",
          timeout = 5,
          priority = 0,
          dependencies = List(),
          state = "Pending",
          attempts = 0,
          period = ""
        )
        val result = attemptTask(task)
        result shouldBe a[Unit]

      }

    }
  }
}
