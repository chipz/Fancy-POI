# Fancy-POI

Fancy scala wrapper for Apache POI

[![JitPack](https://jitpack.io/v/frozenspider/fancy-poi.svg)](https://jitpack.io/#frozenspider/fancy-poi)

## Overview

This fork enhances functionality a little, as well as:

* Added SBT config
* Added cross-compilation for Scala 2.10, 2.11, 2.12
* Updated POI dependency to (currently latest) 3.15
* Replaced most deprecated stuff

Note that this wrapper library still requires a major cleanup, but it will do its job as a thin wrapper.

## How to include

In your `build.sbt`:

```scala
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.frozenspider" %% "fancy-poi" % "1.2.1"
```
