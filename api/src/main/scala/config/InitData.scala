package config

object InitData {
  val queries : List[String] = List(
    """CREATE KEYSPACE IF NOT EXISTS cognira
     WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}""",

    "CREATE TABLE IF NOT EXISTS cognira.flags (flag_id TEXT PRIMARY KEY)",

    """CREATE TABLE IF NOT EXISTS cognira.tasks (
     task_id TEXT,
     task_name TEXT,
     task_type TEXT,
     parameters TEXT,
     attempts INT,
     timeout INT,
     state TEXT,
     created_at TIMESTAMP,
     period TEXT,
     priority INT,
     dependencies LIST<TEXT>,
     result TEXT,
     PRIMARY KEY ((period),priority,task_id)
  ) WITH CLUSTERING ORDER BY (priority DESC)"""
  )
}
