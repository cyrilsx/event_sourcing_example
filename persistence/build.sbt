

val phantomVersion = "1.10.1"
val reactiveMongoVersion = "0.11.7"

val phantomDsl = "com.websudos" %% "phantom-dsl" % phantomVersion
val reactiveMongo = "org.reactivemongo" %% "reactivemongo" % reactiveMongoVersion


libraryDependencies ++= Seq(phantomDsl, reactiveMongo)
