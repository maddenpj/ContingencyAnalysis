lazy val commonSettings = Seq(
  organization := "com.github.maddenpj.aba",
  version := "0.1.0",
  scalaVersion := "2.11.6"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "FBATool",
    initialCommands := """
      import argonaut._, Argonaut._
      import com.github.maddenpj.aba._, Coding._, aba._
    """,
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core" % "7.1.1",
      "io.argonaut" %% "argonaut" % "6.1-M4",
      "com.typesafe" % "config" % "1.2.1"
    )
  )


