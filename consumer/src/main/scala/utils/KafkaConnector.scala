package utils

import config.Config.{bootstrapServers, groupId}
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.util.Properties

object KafkaConnector {

  private val props = new Properties()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
  props.put(
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringDeserializer"
  )
  props.put(
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringDeserializer"
  )

  val consumer = new KafkaConsumer[String, String](props)
}
