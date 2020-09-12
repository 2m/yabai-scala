#!/usr/bin/env amm
// scala 2.13.3

import $ivy.`com.lihaoyi::ammonite-ops:2.2.0`
import ammonite.ops.{root => _, _}
import ammonite.ops.ImplicitWd._

import $ivy.`io.circe::circe-parser:0.13.0`
import io.circe.parser._

import $ivy.`io.circe::circe-optics:0.13.0`
import io.circe.Json
import io.circe.optics.JsonPath._
import monocle._

class Yabai {
  def ?(query: String): Json = parsed(%%("yabai", "-m", "query", s"--$query"))
  def !(args: String) = %("yabai", "-m", args.split(" ").toList)

  private def parsed(result: CommandResult) =
    parse(result.out.string) match {
      case Left(failure) => throw new Exception("Got an invalid json")
      case Right(json)   => json
    }
}
val yabai = new Yabai()

class JsonOps(j: Json) {
  def /[O](trav: Traversal[Json, O]) = trav.getAll(j)
}
implicit def jsonOps(j: Json) = new JsonOps(j)

// config

val spaces = yabai ? "spaces" / root.each.selectDynamic("index").int
for {
  _ <- (spaces.max until 10)
} yield yabai ! "space --create"

yabai ! "config window_topmost on"
