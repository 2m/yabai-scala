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

class YabaiSuite extends MySuite:

  test("!") {
    yabai ! "rule --remove 0" equalsTo "yabai -m rule --remove 0"
  }

  test("?") {
    cmdReturns(Json.obj())
    yabai ? "spaces" equalsTo Json.obj()
  }

  test("spaces") {
    val spaces = (0 to 9).toList
    cmdReturns(Json.arr(spaces map (idx => Json.obj("id" -> Json.fromInt(idx)))*))
    yabai.spaces equalsTo spaces
  }

  test("window_topmost")(yabai window_topmost off cmdIs "yabai -m config window_topmost off")
  test("window_gap")(yabai window_gap 2 cmdIs "yabai -m config window_gap 2")
  test("window_shadow")(yabai window_shadow on cmdIs "yabai -m config window_shadow on")

  test("layout")(yabai layout bsp cmdIs "yabai -m config layout bsp")
  test("auto_balance")(yabai auto_balance on cmdIs "yabai -m config auto_balance on")
  test("window_placement")(yabai window_placement second_child cmdIs "yabai -m config window_placement second_child")
  test("split_ratio")(yabai split_ratio 0.5 cmdIs "yabai -m config split_ratio 0.5")

  test("mouse_follows_focus")(yabai mouse_follows_focus on cmdIs "yabai -m config mouse_follows_focus on")
  test("focus_follows_mouse")(yabai focus_follows_mouse autofocus cmdIs "yabai -m config focus_follows_mouse autofocus")

  test("mouse_modifier")(yabai mouse_modifier cmd cmdIs "yabai -m config mouse_modifier cmd")
  test("mouse_action1")(yabai mouse_action1 move cmdIs "yabai -m config mouse_action1 move")
  test("mouse_action2")(yabai mouse_action2 resize cmdIs "yabai -m config mouse_action2 resize")

  test("window_opacity")(yabai window_opacity on cmdIs "yabai -m config window_opacity on")
  test("active_window_opacity")(yabai active_window_opacity 0.5 cmdIs "yabai -m config active_window_opacity 0.5")
  test("normal_window_opacity")(yabai normal_window_opacity 0.5 cmdIs "yabai -m config normal_window_opacity 0.5")

  test("window_border")(yabai window_border on cmdIs "yabai -m config window_border on")
  test("window_border_width")(yabai window_border_width 2 cmdIs "yabai -m config window_border_width 2")
  test("active_window_border_color")(
    yabai active_window_border_color 0xff775759 cmdIs "yabai -m config active_window_border_color 0xff775759"
  )
  test("normal_window_border_color")(
    yabai normal_window_border_color 0xff775759 cmdIs "yabai -m config normal_window_border_color 0xff775759"
  )

  test("padding") {
    Seq(
      "yabai -m config top_padding 5",
      "yabai -m config right_padding 5",
      "yabai -m config bottom_padding 5",
      "yabai -m config left_padding 5"
    ).foreach(yabai padding 5 cmdIs _)
  }
