package config

object Config {
  val bootstrapServers: String =
    sys.env.getOrElse("bootstrap_servers", "0.0.0.0:9092")
  val keyspace: String = sys.env.getOrElse("keyspace", "cognira")
  val DB_URL: String = sys.env.getOrElse("DB_URL", "localhost")
  val CACHE_URL: String = sys.env.getOrElse("CACHE_URL", "localhost")
  val topics : String = sys.env.getOrElse("topics","first_prime_larger_than_n,benchmark_primes")
  val DB_PORT = 9042
  val MAX_ATTEMPTS = 3
  object States extends Enumeration {
    type State = Value
    val Completed, Processing, Pending, Failed, Queued, TimedOut = Value
  }
}
