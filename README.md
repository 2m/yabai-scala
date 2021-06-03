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
However it works realy great with [tsk][].
This allows seamless integration into system configuration dotfiles.

For example the following file:

```scala
// 2> /dev/null \
/*
scala_version="3.0.0-RC2"
dependencies="
  lt.dvim.yabai:yabai-scala_3.0.0-RC2:1.0.2
"
source $(curl -sL git.io/boot-tsk | sh)
run
exit
 */

import lt.dvim.yabai.*

import scala.language.implicitConversions

@main def run() =
  for {
    _ <- (yabai.spaces.max until 10)
  } yield yabai ! "space --create"
```

is a standalone script, which when run by `sh` will create yabai spaces until the
total number of spaces reaches 10:

```sh
~ ─╼ sh Yabai.scala                                                                                   0
Fetching all libraries the script depends upon
 [##########]   Downloaded 1 POM files in 0 s
 [##########]   Downloaded 22 JAR files in 10 s
Compiling Yabai (1 Scala source)
Compiled Yabai (229ms)
Executing: yabai -m query --spaces
```

For a more complete example, check out [full Yabai configuration][yabai-config].

[tsk]: https://github.com/tsk-tsk/tsk-tsk
[yabai-config]: https://github.com/2m/dotfiles/blob/main/home/private_Library/private_Application%20Support/yabai-scala/Yabai.scala
