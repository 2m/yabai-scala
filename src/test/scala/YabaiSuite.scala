package lt.dvim.yabai

import io.circe.Json

class YabaiSuiteSuite extends munit.FunSuite:
   case class TestRunner() extends Runner:
      var json: Option[Json] = None
      var cmd = ""
      def run(command: String) =
         cmd = command
         json.map(_.toString).getOrElse(command)

   val yabai = FunFixture[(Yabai, TestRunner)](
      setup = { test =>
         val runner = TestRunner()
         (Yabai(runner), runner)
      },
      teardown = { _ => }
   )

   yabai.test("!") { (yabai, _) =>
      assertEquals(yabai ! "rule --remove 0", "yabai -m rule --remove 0")
   }

   yabai.test("?") { (yabai, runner) =>
      runner.json = Some(Json.obj())
      val result = yabai ? "spaces"
      assertEquals(result, Json.obj())
   }

   yabai.test("spaces") { (yabai, runner) =>
      val spaces = (0 to 9).toList
      runner.json = Some(Json.arr(spaces map (idx => Json.obj("index" -> Json.fromInt(idx)))*))
      assertEquals(yabai.spaces, spaces)
   }

   yabai.test("config boolean") { (yabai, runner) =>
      yabai.config("window_shadow" -> false)
      assertEquals(runner.cmd, "yabai -m config window_shadow off")
   }

   yabai.test("config string") { (yabai, runner) =>
      yabai.config("layout" -> "bsp")
      assertEquals(runner.cmd, "yabai -m config layout bsp")
   }

   yabai.test("config double") { (yabai, runner) =>
      yabai.config("split_ratio" -> 0.5)
      assertEquals(runner.cmd, "yabai -m config split_ratio 0.5000")
   }
