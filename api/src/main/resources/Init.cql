CREATE KEYSPACE IF NOT EXISTS cognira
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS cognira.flags (flag_id TEXT PRIMARY KEY);

CREATE TABLE IF NOT EXISTS cognira.tasks (
    task_id TEXT,
    task_name TEXT,
    task_type TEXT,
    parameters TEXT,
    attempts INT,
    timeout INT,
    state TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (task_id)
);