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
However it works realy great with [scala-cli][].
It allows seamless integration into system configuration dotfiles.

For example the following file:

```scala
//> using lib "lt.dvim.yabai::yabai-scala:2.0.1"

import lt.dvim.yabai.*

@main def run() =
  for {
    _ <- (yabai.spaces.max until 10)
  } yield yabai ! "space --create"
```

is a standalone Scala program, which when run with `scala-cli` will create
yabai spaces until the total number of spaces reaches 10:

```sh
~ ─╼ scala-cli run Yabai.scala
Compiling project (Scala 3.1.1, JVM)
Executing: yabai -m query --spaces
```

For a more complete example, check out [full Yabai configuration][yabai-config].

[scala-cli]: https://scala-cli.virtuslab.org/docs/overview
[yabai-config]: https://github.com/2m/dotfiles/blob/main/home/private_Library/private_Application%20Support/yabai-scala/Yabai.scala
