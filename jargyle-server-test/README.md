# jargyle-server-test

## About

This module is used to test the module `jargyle-server`.

## Why Does This Module Exist?

When the module `jargyle-server` has the module `test-help` as a test dependency, Maven (v3.3.9) gives a compilation error or failure of being unable to find the symbols that are defined in the module `test-help`. (This issue doesn't occur in the other modules `echo`, `jargyle-cli`, and `jargyle-transport`.) However, when the module `jargyle-server` has the module `test-help` as a compile dependency, no compilation error or failure occurs. But if another project or module has the module `jargyle-server` as a dependency, the module `test-help` will be included. This is undesirable because the module `test-help` is only to be used for testing within this project. This module is the best workaround so far because it has the module `test-help` as a compile dependency and it will not be used as a dependency in another project or module.