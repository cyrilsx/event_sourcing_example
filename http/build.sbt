
val scalaTestVersion = "2.2.4"
val akkaVersion = "2.0-M2"
val json4sVersion = "3.3.0.RC5"
val akkaHttpJsonVersion = "1.1.0"


val scalaTest = "org.scalatest" % "scalatest_2.11" % scalaTestVersion % Test
val akkaStream = "com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaVersion
val akkaHttpCore = "com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaVersion
val akkaHttp = "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaVersion
val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion



libraryDependencies ++= Seq(scalaTest, akkaStream, akkaHttpCore, akkaHttp, akkaHttpSprayJson)