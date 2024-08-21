import com.datastax.oss.driver.api.core.session.Session
import config.Config.keyspace
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.PrivateMethodTester
import controllers.TaskControllers
import controllers.TaskControllers.prepareQuery
import models.TaskModel.{OptionalStringList, Task}
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpec
import spray.json.JsObject
import spray.json._
class ModelSpec extends AnyWordSpec with Matchers {
  "OptionalStringList" should {
    val optionalStringList = OptionalStringList

    "deserialize JsNull to None" in {
      val json = JsNull
      val result = optionalStringList.read(json)
      result shouldBe None
    }

    "deserialize JsArray of JsStrings to Some(List[String])" in {
      val json = JsArray(JsString("one"), JsString("two"), JsString("three"))
      val result = optionalStringList.read(json)
      result shouldBe Some(List("one", "two", "three"))
    }

    "throw DeserializationException for JsArray with non-JsString elements" in {
      val json = JsArray(JsString("one"), JsNumber(2), JsBoolean(true))
      assertThrows[DeserializationException] {
        optionalStringList.read(json)
      }
    }

    "serialize None to JsNull" in {
      val obj: Option[List[String]] = None
      val result = optionalStringList.write(obj)
      result shouldBe JsNull
    }

    "serialize Some(List[String]) to JsArray of JsStrings" in {
      val obj: Option[List[String]] = Some(List("one", "two", "three"))
      val result = optionalStringList.write(obj)
      result shouldBe JsArray(JsString("one"), JsString("two"), JsString("three"))
    }

  }
}
