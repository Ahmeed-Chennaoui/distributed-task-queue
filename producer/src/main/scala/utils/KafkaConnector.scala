package utils

import java.util.Properties
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}

import scala.jdk.CollectionConverters._
import config.Config._
import utils.Logger.logger
object KafkaConnector {

  /** Kafka producer properties
    */
  private val props = new Properties()
  props.put("bootstrap.servers", bootstrapServers)
  props.put(
    "key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer"
  )
  props.put(
    "value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer"
  )

  val producer = new KafkaProducer[String, String](props)

  /** Kafka AdminClient properties
    */
  private val adminProps = new Properties()
  adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)

  def createTopic(
      topic: String,
      partitions: Int,
      replicationFactor: Int
  ): Unit = {
    val adminClient = AdminClient.create(adminProps)
    try {
      val newTopic = new NewTopic(topic, partitions, replicationFactor.toShort)
      val createTopicOutcome = adminClient.createTopics(List(newTopic).asJava)
      createTopicOutcome.all().get()
    } catch {
      case e: Exception =>
        logger.error(s"Error creating topic '$topic': ${e.getMessage}")
        e.printStackTrace()
    } finally {
      adminClient.close()
    }

  }
}
