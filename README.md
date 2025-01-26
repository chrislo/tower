# Prerequisites

This script relies on the `XPlayBuf` UGen. Zack Schollz compiles a version of this in the [portedplugins](https://github.com/schollz/portedplugins) project.

I downloaded the version 0.4.5 [PortedPlugins-macOS.zip](https://github.com/schollz/portedplugins/releases/download/v0.4.5/PortedPlugins-macOS-ARM.zip).

I placed the `XPlayBuf.sc` and `XPlayBuf_scsynth.scx` files in my SuperCollider `Platform.userExtensionDir`. I also had to remove the `com.apple.quarantine` flag with `xattr -d com.apple.quarantine XplayBuf*`.
