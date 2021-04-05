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

@main def run() =
    import lt.dvim.yabai.*

    for {
    _ <- (yabai.spaces.max until 10)
    } yield yabai ! "space --create"

    // Query for app names and title of running apps with:
    // yabai -m query --windows | jq '.[] | {app: .app, title: .title}'
    yabai no_manage (Rule.App("choose"), Rule.App("System Preferences"), Rule.Title("gpg-copy"))

    yabai config "window_topmost" -> true
    yabai config "window_gap" -> 2
    yabai config "window_shadow" -> false

    yabai config "layout" -> "bsp"
    yabai config "auto_balance" -> false
    yabai config "window_placement" -> "second_child"
    yabai config "split_ratio" -> 0.5

    yabai config "mouse_follows_focus" -> false
    yabai config "focus_follows_mouse" -> "autofocus"

    yabai mouse ("ctrl", "resize", "move")
    yabai padding 5
    yabai opacity (1.0, 0.9)
    yabai border (0, 0xff775759, 0xff505050)
