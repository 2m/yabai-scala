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

import io.circe.Json

open trait MySuite extends munit.FunSuite:
  case class TestRunner() extends Runner:
    var json: Option[Json] = None
    var cmd = ""
    def run(command: String) =
      cmd = command
      json.map(_.toString).getOrElse(command)

  type Testable = Yabai ?=> TestRunner ?=> Any
  type WithRunner = TestRunner ?=> Any
  type WithYabai[T] = Yabai ?=> T

  extension [T](v: T) infix def equalsTo(v2: T) = assertEquals(v, v2)
  extension (u: Unit) infix def cmdEqualsTo(str: String): WithRunner = assertEquals(summon[Runner].cmd, str)

  case class MyFixture(underlying: FunFixture[(Yabai, TestRunner)]):
    def test(s: String)(body: Testable) = underlying.test(s) { (yabai, runner) =>
      body(using yabai)(using runner)
    }

  object MyFixture:
    def apply(setup: () => (Yabai, TestRunner)): MyFixture = MyFixture(
      FunFixture[(Yabai, TestRunner)](
        setup = { test =>
          val runner = TestRunner()
          (Yabai(runner), runner)
        },
        teardown = { _ => }
      )
    )

  def cmdReturns(json: Json): WithRunner =
    summon[TestRunner].json = Some(json)

  def yabai: WithYabai[Yabai] =
    summon[Yabai]

  val yabaiFixture = MyFixture {
    val runner = TestRunner()
    () => (Yabai(runner), runner)
  }

  def test(s: String)(body: Testable) = yabaiFixture.test(s)(body)
