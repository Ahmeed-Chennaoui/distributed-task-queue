ThisBuild / version := "0.14.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"
enablePlugins(JavaAppPackaging)
//enablePlugins(ScoverageSbtPlugin)
enablePlugins(JacocoPlugin)
//coverageEnabled := true
//jacocoReportSettings := JacocoReportSettings().withFormats(JacocoReportFormats.XML,JacocoReportFormats.HTML)
//coverageExcludedPackages:= "utils|config"
sonarProperties := Map(
  "sonar.projectKey" -> "distributed_task_queue_consumer",
  "sonar.sources" -> "src/main/scala",
  "sonar.host.url" -> "http://localhost:9000",
  "sonar.login" -> "sqp_3c361b3a80d58737321b5667c2e084cc8c2ebcc9",
  "sonar.java.coveragePlugin" -> "jacoco",
  "sonar.coverage.jacoco.xmlReportPaths" -> "target/scala-2.13/jacoco/report/jacoco.xml"
)
libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "3.7.0",
  "org.slf4j" % "slf4j-api" % "2.0.9",
  "ch.qos.logback" % "logback-classic" % "1.5.2",
  "com.typesafe.akka" %% "akka-http" % "10.5.0",
  "com.typesafe.akka" %% "akka-actor" % "2.8.0",
  "com.typesafe.akka" %% "akka-stream" % "2.8.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0",
  "com.datastax.oss" % "java-driver-core" % "4.15.0",
  "redis.clients" % "jedis" % "5.1.0",
  "org.scalatest" %% "scalatest" % "3.2.17" % "test",
  "org.cassandraunit" % "cassandra-unit" % "4.3.1.0" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.8.0" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.5.0" % Test,
  "org.scalatestplus" %% "mockito-5-10" % "3.2.18.0" % "test"
)
lazy val root = (project in file("."))
  .settings(
    name := "consumer"
  )
