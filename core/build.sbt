
val scalaTestVersion = "2.2.4"
val akkaVersion = "2.0-M1"
val json4sVersion = "3.3.0.RC5"
val akkaHttpJsonVersion = "1.1.0"
val jodaTimeVersion = "2.8.2"


val scalaTest = "org.scalatest" % "scalatest_2.11" % scalaTestVersion % Test
val akkaStream = "com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaVersion
val akkaHttpCore = "com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaVersion
val akkaHttp = "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaVersion
val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
val json4s = "org.json4s" % "json4s-native_2.11" % json4sVersion
val json4sExt = "org.json4s" % "json4s-ext_2.11" % json4sVersion
val akkaHttpJson = "de.heikoseeberger" %% "akka-http-json4s" % akkaHttpJsonVersion
val jodaTime = "joda-time" % "joda-time" % jodaTimeVersion


libraryDependencies ++= Seq(scalaTest, akkaStream, akkaHttpCore, akkaHttp, akkaHttpSprayJson, json4s, json4sExt, akkaHttpJson,
  jodaTime)