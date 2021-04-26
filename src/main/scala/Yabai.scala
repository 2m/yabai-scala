/*
 * Copyright 2020 github.com/2m/yabai-scala/contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lt.dvim.yabai

import scala.Conversion
import sys.process.Process
import io.circe.Json
import io.circe.parser.parse
import io.circe.optics.JsonPath.root
import monocle.Traversal

extension (j: Json) def /[O](trav: Traversal[Json, O]) = trav.getAll(j)
extension (d: Double) def str = f"$d%.4f"
extension (i: Int) def hex = s"0x${i.toHexString}"

trait Runner:
  def run(command: String): String
case class SystemRunner() extends Runner:
  def run(command: String) =
    println(s"Executing: $command")
    Process(command).!!.trim

enum Rule:
  case App(app: String)
  case Title(title: String)

trait on; case object on extends on
trait off; case object off extends off

trait bsp; case object bsp extends bsp
trait stack; case object stack extends stack
trait float; case object float extends float

trait first_child; case object first_child extends first_child
trait second_child; case object second_child extends second_child

trait autofocus; case object autofocus extends autofocus
trait autoraise; case object autoraise extends autoraise

trait cmd; case object cmd extends cmd
trait alt; case object alt extends alt
trait shift; case object shift extends shift
trait ctrl; case object ctrl extends ctrl
trait fn; case object fn extends fn

trait move; case object move extends move
trait resize; case object resize extends resize

case class Yabai(runner: Runner):
  private def parsed(result: String) =
    parse(result) match
      case Left(failure) => throw Exception("Got an invalid json")
      case Right(json)   => json

  private def cleanRules() =
    for {
      _ <- spaces
    } yield this ! "rule --remove 0"

  def !(args: String) =
    runner.run(s"yabai -m $args")

  def ?(query: String): Json = parsed(this ! s"query --$query")
  def rule(query: String): Json = parsed(this ! s"rule --$query")

  def spaces: List[Int] = this ? "spaces" / root.each.id.int

  infix def no_manage(rules: Rule*) =
    cleanRules()
    rules
      .map {
        case Rule.App(app)     => s""""app=^$app$$""""
        case Rule.Title(title) => s""""title=^$title$$""""
      }
      .map(r => s"rule --add $r manage=off")
      .map(this ! _)

  infix def window_topmost(v: on | off) = this.ifChanged("window_topmost", v.toString)
  infix def window_gap(v: Int) = this.ifChanged("window_gap", v.toString)
  infix def window_shadow(v: on | off) = this.ifChanged("window_shadow", v.toString)

  infix def layout(v: bsp | stack | float) = this.ifChanged("layout", v.toString)
  infix def auto_balance(v: on | off) = this.ifChanged("auto_balance", v.toString)
  infix def window_placement(v: first_child | second_child) = this.ifChanged("window_placement", v.toString)
  infix def split_ratio(v: Float) = this.ifChanged("split_ratio", v.toString)

  infix def mouse_follows_focus(v: on | off) = this.ifChanged("mouse_follows_focus", v.toString)
  infix def focus_follows_mouse(v: autofocus | autoraise | off) = this.ifChanged("focus_follows_mouse", v.toString)

  infix def mouse_modifier(v: cmd | alt | shift | ctrl | fn) = this.ifChanged("mouse_modifier", v.toString)
  infix def mouse_action1(v: move | resize) = this.ifChanged("mouse_action1", v.toString)
  infix def mouse_action2(v: move | resize) = this.ifChanged("mouse_action2", v.toString)

  infix def window_opacity(v: on | off) = this.ifChanged("window_opacity", v.toString)
  infix def active_window_opacity(v: Float) = this.ifChanged("active_window_opacity", v.toString)
  infix def normal_window_opacity(v: Float) = this.ifChanged("normal_window_opacity", v.toString)

  infix def window_border(v: on | off) = this.ifChanged("window_border", v.toString)
  infix def window_border_width(v: Int) = this.ifChanged("window_border_width", v.toString)
  infix def active_window_border_color(v: Int) = this.ifChanged("active_window_border_color", v.hex)
  infix def normal_window_border_color(v: Int) = this.ifChanged("normal_window_border_color", v.hex)

  infix def padding(v: Int): Unit = List(
    "top_padding" -> s"$v",
    "right_padding" -> s"$v",
    "bottom_padding" -> s"$v",
    "left_padding" -> s"$v"
  ).map((this.ifChanged).tupled)

  private def ifChanged(key: String, value: String) =
    if (this ! s"config $key" != value) this ! s"config $key $value"

val yabai = Yabai(SystemRunner())
