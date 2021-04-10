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
      cmdReturns(Json.arr(spaces map (idx => Json.obj("index" -> Json.fromInt(idx)))*))
      yabai.spaces equalsTo spaces
   }

   test("config boolean") {
      yabai config "window_shadow" -> false cmdEqualsTo "yabai -m config window_shadow off"
   }

   test("config string") {
      yabai config "layout" -> "bsp" cmdEqualsTo "yabai -m config layout bsp"
   }

   test("config double") {
      yabai config "split_ratio" -> 0.5 cmdEqualsTo "yabai -m config split_ratio 0.5000"
   }
