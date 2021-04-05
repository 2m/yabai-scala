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

case class Config(key: String, value: String)
object Config:
    given fromBoolean: Conversion[(String, Boolean), Config] = (key, value) => Config(key, if (value) "on" else "off")
    given fromString: Conversion[(String, String), Config] = (key, value) => Config(key, value)
    given fromInt: Conversion[(String, Int), Config] = (key, value) => Config(key, value.toString)
    given fromDouble: Conversion[(String, Double), Config] = (key, value) => Config(key, value.str)

case class Yabai(runner: Runner):
    private def parsed(result: String) =
        parse(result) match
            case Left(failure) => throw new Exception("Got an invalid json")
            case Right(json)   => json

    private def cleanRules() =
        val indices = this.rule("list") / root.each.selectDynamic("index").int
        for {
        _ <- indices
        } yield this ! "rule --remove 0"

    def !(args: String) =
        runner.run(s"yabai -m $args")

    def ?(query: String): Json = parsed(this ! s"query --$query")
    def rule(query: String): Json = parsed(this ! s"rule --$query")

    infix def spaces = this ? "spaces" / root.each.selectDynamic("index").int

    infix def no_manage(rules: Rule*) =
        cleanRules()
        rules
            .map {
                case Rule.App(app)     => s""""app=^$app$$""""
                case Rule.Title(title) => s""""title=^$title$$""""
            }
            .map(r => s"rule --add $r manage=off")
            .map(this ! _)

    infix def config(config: Config) = this.configIfChanged(config.key, config.value)

    infix def padding(top: Int, right: Int, bottom: Int, left: Int): Unit =
        List(
            "top_padding" -> s"$top",
            "right_padding" -> s"$right",
            "bottom_padding" -> s"$bottom",
            "left_padding" -> s"$left"
        ).map((this.configIfChanged).tupled)

    infix def padding(all: Int): Unit = padding(all, all, all, all)

    infix def mouse(mod: String, action1: String, action2: String) =
        List(
            "mouse_modifier" -> mod,
            "mouse_action1" -> action1,
            "mouse_action2" -> action2
        ).map((this.configIfChanged).tupled)

    infix def opacity(active: Double, normal: Double) =
        (
            if (active + normal == 2.0) List("window_opacity" -> "off")
            else
                List(
                    "window_opacity" -> "on",
                    "active_window_opacity" -> active.str,
                    "normal_window_opacity" -> normal.str
                )).map((this.configIfChanged).tupled)

    infix def border(width: Int, active: Int, normal: Int) =
        (if (width == 0) List("window_border" -> "off")
        else
        List(
            "window_border" -> "on",
            "window_border_width" -> s"$width",
            "active_window_border_color" -> active.hex,
            "normal_window_border_color" -> normal.hex
        )).map((this.configIfChanged).tupled)

    private def configIfChanged(key: String, value: String) =
        if (this ! s"config $key" != value) this ! s"config $key $value"


val yabai = Yabai(SystemRunner())
