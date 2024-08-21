package config

object Config {
  val topic: String = sys.env.getOrElse("topic","test")
  val bootstrapServers: String = sys.env.getOrElse("bootstrap_servers","kafka:9092")
  val groupId: String = sys.env.getOrElse("group_id","group-1")
  val keyspace: String = sys.env.getOrElse("keyspace","cognira")
  val DB_URL: String = sys.env.getOrElse("DB_URL", "localhost")
  val CACHE_URL : String = sys.env.getOrElse("CACHE_URL","localhost")
  val CACHE_VALIDITY : String = sys.env.getOrElse("CACHE_VALIDITY","2592000")
  val DB_PORT = 9042
  object States extends Enumeration {
    type State = Value
    val Completed, Processing, Pending, Failed, Queued, TimedOut = Value
  }
}
