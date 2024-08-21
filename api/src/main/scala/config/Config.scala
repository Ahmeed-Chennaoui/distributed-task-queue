package config

object Config {
  val keyspace: String = sys.env.getOrElse("keyspace", "cognira")
  val DB_URL: String = sys.env.getOrElse("DB_URL", "localhost")
  val DB_PORT = 9042
  val PeriodType: String = sys.env.getOrElse("PeriodType", "Month")
  object States extends Enumeration {
    type State = Value
    val Completed, Processing, Pending, Failed, Queued, TimedOut = Value
  }
}
