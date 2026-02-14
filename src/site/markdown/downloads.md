# Downloading Jargyle

## Contents

-   [Overview](#overview)
-   [Prerequisites](#prerequisites)
-   [Distributions](#distributions)
    -   [Binary Distribution](#binary-distribution)
    -   [Source Distribution](#source-distribution)
-   [Build System Dependencies](#build-system-dependencies)

## Overview

This document discusses the types of downloads for Jargyle.

## Prerequisites

Jargyle requires JDK 9 or higher. It is recommended to use JDK 21 or higher 
to take advantage of using virtual threads instead of platform threads. 

## Distributions

Jargyle is distributed in many archive formats for your convenience.

### Binary Distribution

The binary distribution archive is distributed in many archive formats with 
the following file suffixes:

-   `-bin.tar.bz2`
-   `-bin.tar.gz` 
-   `-bin.zip`

Simply pick a ready-made binary distribution archive from 
[here](https://github.com/jh3nd3rs0n/jargyle/releases) and follow the 
[installation instructions](./installation.md).

### Source Distribution

The source distribution archive is distributed in many archive formats with 
the following file suffixes:

-   `-src.tar.bz2`
-   `-src.tar.gz`
-   `-src.zip`

Use a source distribution archive from 
[here](https://github.com/jh3nd3rs0n/jargyle/releases) if you intend to build 
Jargyle yourself.

## Build System Dependencies

Add either of the following APIs as a dependency to your build system:

-   [Client API](./jargyle-client/dependency-info.html)
-   [Server API](./jargyle-server/dependency-info.html)

**Note**: The build system dependencies are only available on GitHub for now. 