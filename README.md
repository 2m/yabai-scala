# [yabai-scala][] [![ci-badge][]][ci] [![gitter-badge][]][gitter]

[yabai-scala]:  https://github.com/2m/yabai-scala
[ci]:           https://github.com/2m/yabai-scala/actions
[ci-badge]:     https://github.com/2m/yabai-scala/workflows/ci/badge.svg
[gitter]:       https://gitter.im/2m/general
[gitter-badge]: https://badges.gitter.im/2m/general.svg

Yabai Scala is a DSL for [Yabai] configuration.
This was started to help with creating missing workspaces.
But eventually more useful features were implemented.

[Yabai]: https://github.com/koekeishiya/yabai

## Usage

Yabai Scala can be used as a regular Scala library.
However it also works realy great as an executable [Scala][] script.
It allows seamless integration into system configuration dotfiles.

For example the following `Yabai` file is a standalone Scala program:

```scala
#!/usr/bin/env -S scala shebang -S 3 -j 23
//> using lib "lt.dvim.yabai::yabai-scala:2.0.1"

import lt.dvim.yabai.*

for {
  _ <- (yabai.spaces.size until 10)
} yield yabai ! "space --create"

yabai window_gap 2
yabai window_shadow off

yabai padding 5
```

When run, it will create yabai spaces until the total number of spaces reaches 10.
Also it will set window gap to 2, disable window shadows and set padding to 5.

The only system dependency is `scala` itself, which can be installed with:

```sh
brew install scala
```

For a more complete example, check out [full Yabai configuration][yabai-config].

[scala]:       https://scala-cli.virtuslab.org/docs/overview
[yabai-config]: https://github.com/2m/dotfiles/blob/main/home/private_Library/private_Application%20Support/yabai-scala/executable_Yabai
