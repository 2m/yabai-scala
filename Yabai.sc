// scala 2.13.3

import $ivy.`com.lihaoyi::ammonite-ops:2.2.0`
import ammonite.ops.{root => _, _}
import ammonite.ops.ImplicitWd._

import $ivy.`io.circe::circe-parser:0.13.0`
import io.circe.parser._

import $ivy.`io.circe::circe-optics:0.13.0`
import io.circe._
import io.circe.optics.JsonPath._
import monocle._

class JsonOps(j: Json) {
  def /[O](trav: Traversal[Json, O]) = trav.getAll(j)
}
implicit def jsonOps(j: Json) = new JsonOps(j)

sealed trait Rule
case class App(app: String) extends Rule
case class Title(title: String) extends Rule

class DoubleOps(d: Double) {
  def str = f"$d%.4f"
}
implicit def doubleOps(d: Double) = new DoubleOps(d)

class IntOps(i: Int) {
  def hex = s"0x${i.toHexString}"
}
implicit def intOps(i: Int) = new IntOps(i)

sealed trait ConfigMagnet {
  def apply(): Unit
}
object ConfigMagnet {
  implicit def symbol_bool(arg: (Symbol, Boolean))(implicit yabai: Yabai) =
    new ConfigMagnet {
      override def apply() = yabai.configIfChanged(arg._1.name, if (arg._2) "on" else "off")
    }
  implicit def symbol_symbol(arg: (Symbol, Symbol))(implicit yabai: Yabai) =
    new ConfigMagnet {
      override def apply() = yabai.configIfChanged(arg._1.name, arg._2.name)
    }
  implicit def symbol_int(arg: (Symbol, Int))(implicit yabai: Yabai) =
    new ConfigMagnet {
      override def apply() = yabai.configIfChanged(arg._1.name, arg._2.toString)
    }
  implicit def symbol_double(arg: (Symbol, Double))(implicit yabai: Yabai) =
    new ConfigMagnet {
      override def apply() = yabai.configIfChanged(arg._1.name, arg._2.str)
    }
}

class Yabai {
  def ?(query: String): Json = parsed(%%("yabai", "-m", "query", s"--$query"))
  def !(args: String) = {
    val program = "yabai" :: "-m" :: args
            .split('"')
            .toList
            .sliding(2, 2)
            .map { case hd :: tl => hd.split(' ').filter(_.nonEmpty) ++ tl }
            .flatten
            .toList
    println(s"Executing: ${program.mkString("\"", "\" \"", "\"")}")
    %%(program).out.string.trim
  }

  def spaces = yabai ? "spaces" / root.each.selectDynamic("index").int
  def no_manage(rules: Rule*) = {
    cleanRules()
    rules
      .map {
        case App(app)     => s""""app=^$app$$""""
        case Title(title) => s""""title=^$title$$""""
      }
      .map(r => s"rule --add $r manage=off")
      .map(this.! _)
  }

  def padding(top: Int, right: Int, bottom: Int, left: Int): Unit =
    List(
      "top_padding" -> s"$top",
      "right_padding" -> s"$right",
      "bottom_padding" -> s"$bottom",
      "left_padding" -> s"$left"
    ).map((this.configIfChanged _).tupled)
  def padding(all: Int): Unit = padding(all, all, all, all)
  def config(magnet: ConfigMagnet) = magnet()
  def mouse(mod: Symbol, action1: Symbol, action2: Symbol) =
    List(
      "mouse_modifier" -> mod.name,
      "mouse_action1" -> action1.name,
      "mouse_action2" -> action2.name
    ).map((this.configIfChanged _).tupled)
  def opacity(active: Double, normal: Double) =
    (if (active + normal == 2.0) List("window_opacity" -> "off")
     else
       List(
         "window_opacity" -> "on",
         "active_window_opacity" -> active.str,
         "normal_window_opacity" -> normal.str
       )).map((this.configIfChanged _).tupled)
  def border(width: Int, active: Int, normal: Int) =
    (if (width == 0) List("window_border" -> "off")
     else
       List(
         "window_border" -> "on",
         "window_border_width" -> s"$width",
         "active_window_border_color" -> active.hex,
         "normal_window_border_color" -> normal.hex
       )).map((this.configIfChanged _).tupled)

  private def parsed(result: CommandResult) =
    parse(result.out.string) match {
      case Left(failure) => throw new Exception("Got an invalid json")
      case Right(json)   => json
    }
  def configIfChanged(key: String, value: String) =
    if (this.!(s"config $key") != value) this.!(s"config $key $value")
  private def cleanRules() = {
    val indices = parsed(%%("yabai", "-m", "rule", "--list")) / root.each.selectDynamic("index").int
    for {
      _ <- indices
    } yield yabai ! "rule --remove 0"
  }
}
implicit val yabai = new Yabai()
