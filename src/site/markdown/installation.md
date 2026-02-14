# Installation

## Contents

-   [Overview](#overview)
-   [Installation of the Command Line Interface](#installation-of-the-command-line-interface)
    -    [Prerequisites for the Command Line Interface](#prerequisites-for-the-command-line-interface)
    -    [Installing the Command Line Interface](#installing-the-command-line-interface)

## Overview

This document discusses how to install Jargyle's command line interface.

## Installation of the Command Line Interface

The command line interface is installed manually by downloading the archive, 
extracting the archive, and adding its `bin` directory to your `PATH` 
environment variable.

### Prerequisites for the Command Line Interface

You need a Java Development Kit (JDK) installed. Either set the `JAVA_HOME` 
environment variable to the path of your JDK installation or add the path of 
the `java` executable to the `PATH` environment variable.

The command line interface requires JDK 9 or higher. It is recommended to use 
JDK 21 or higher to take advantage of using virtual threads instead of 
platform threads. 

### Installing the Command Line Interface

To install the command line interface, extract the archive and add its `bin` 
directory to the `PATH` environment variable.

Detailed steps are:

1.   Download the Jargyle 
     [binary distribution archive](./downloads.md#binary-distribution).

2.   Extract the archive in any directory. Use any of the following commands 
     depending on the archive (Replace `VERSION` with the actual version 
     listed in the name of the archive):
     
     ```bash
     tar xvf jargyle-VERSION-bin.tar.bz2
     ```
     
     ```bash
     tar xzvf jargyle-VERSION-bin.tar.gz
     ```
     
     ```bash
     unzip jargyle-VERSION-bin.zip
     ```
     
3.   Add the `bin` directory of the created directory to the `PATH` 
     environment variable.

4.   Confirm with `jargyle -V` in a new shell. The result should look similar 
     to the following:
     
     ```text
     Jargyle CLI VERSION
     ```

     (The actual version would be displayed instead of `VERSION`)

The command line interface is now installed.