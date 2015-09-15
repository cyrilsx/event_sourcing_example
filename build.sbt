name := "EventSourcing Fun"
version := "1.0.0"
scalaVersion := "2.11.7"


libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
libraryDependencies += "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0"
libraryDependencies += "com.typesafe.akka" % "akka-http-core-experimental_2.11" % "1.0"
libraryDependencies += "com.typesafe.akka" % "akka-http-experimental_2.11" % "1.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "1.0"
libraryDependencies += "com.websudos"  %% "phantom-dsl" % "1.10.1"
// libraryDependencies += "com.websudos" %% "phantom-testkit" % "1.10.1" % "test, provided"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"
