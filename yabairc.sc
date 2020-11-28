// scala 2.13.3

import $file.Yabai, Yabai._

for {
  _ <- (yabai.spaces.max until 10)
} yield yabai ! "space --create"

// Query for app names and title of running apps with:
// yabai -m query --windows | jq '.[] | {app: .app, title: .title}'
yabai no_manage (App("choose"), App("System Preferences"), Title("gpg-copy"))

yabai config 'window_topmost -> true
yabai config 'window_gap -> 2
yabai config 'window_shadow -> false

yabai config 'layout -> 'bsp
yabai config 'auto_balance -> false
yabai config 'window_placement -> 'second_child
yabai config 'split_ratio -> 0.5

yabai config 'mouse_follows_focus -> false
yabai config 'focus_follows_mouse -> 'autofocus

yabai mouse ('ctrl, 'resize, 'move)
yabai padding 5
yabai opacity (1.0, 0.9)
yabai border (0, 0xff775759, 0xff505050)
