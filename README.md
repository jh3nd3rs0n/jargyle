[![CodeQL](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml) [![Java CI with Maven (Mac OS Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml) [![Java CI with Maven (Ubuntu Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml) [![Java CI with Maven (Windows Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/581706f82bf945df84bc397da4cecee5)](https://www.codacy.com/gh/jh3nd3rs0n/jargyle/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/jargyle&amp;utm_campaign=Badge_Grade)

# Jargyle

## Contents

-   [Overview](#overview)
-   [Contributing Guidelines](#contributing-guidelines) 
-   [Directory Overview](#directory-overview) 
-   [Build Requirements](#build-requirements)
-   [Frequently Used Maven Commands](#frequently-used-maven-commands)

## Overview

Jargyle is a Java SOCKS5 server and a Java SOCKS5 client/server API. It is
inspired by [JSocks](https://jsocks.sourceforge.net/),
[SocksLib](https://github.com/fengyouchao/sockslib),
[Esocks](https://github.com/fengyouchao/esocks) and
[Dante](https://www.inet.no/dante/index.html).

Jargyle is licensed under the
[MIT license](https://opensource.org/licenses/MIT).
Its license can be found [here](LICENSE).

This README file discusses how to get started in contributing and building 
Jargyle. For detailed examples and usage instructions including how to use 
Jargyle, please refer to the 
[project website](https://jh3nd3rs0n.github.io/jargyle).

## Contributing Guidelines

The contributing guidelines can be found [here](CONTRIBUTING.md).

## Directory Overview

The following is a simple overview of the directory.

-   `.github/workflows/`: Contains GitHub workflow files that perform analyses 
    and tests when a push has been made to the GitHub repository

-   `docs/`: Contains the website/documentation

-   `jargyle-argmatey/`: Maven module for the extensible command line 
    interface. It is used for the Jargyle command line interface.

-   `jargyle-cli/`: Maven module for the Jargyle command line interface

-   `jargyle-client/`: Maven module for the SOCKS client API

-   `jargyle-common/`: Maven module for the public API used by all modules

-   `jargyle-distributions/`: Maven module for creating the binary and source 
    distributions

-   `jargyle-internal/`: Maven module for the internal API used by all modules

-   `jargyle-protocolbase/`: Maven module for the foundational API for the 
    SOCKS client API and the SOCKS server API

-   `jargyle-report-aggregate/`: Maven module for generating the aggregated
    test coverage reports

-   `jargyle-server/`: Maven module for the SOCKS server API

-   `jargyle-test-echo/`: Maven module for clients and servers that 
    send/receive data and receive/send back the same data. The clients and one 
    of the servers use the SOCKS client API. When testing the clients and one 
    of the servers, the SOCKS client API and the SOCKS server API are also 
    tested.

-   `jargyle-test-echo-server-performance/`: Maven module for performance 
    testing of servers that receive data and send back the same data. It 
    includes performance testing of the SOCKS server API.

-   `jargyle-test-help/`: Maven module for the API for help with testing

-   `jargyle-test-netty-example-socksproxy/`: Maven module for the modified 
    version of the Netty example SOCKS proxy. It is used for testing.

-   `src/site/`: Contains files used to generate `docs/`

-   `.gitignore`: List of directories and files for Git to ignore such as
    Eclipse and IntelliJ IDEA project directories and files

-   `CODE_OF_CONDUCT.md`: Code of conduct for contributing to this project

-   `CONTRIBUTING.md`: Contributing guidelines

-   `LICENSE`: The license for this project

-   `pom.xml`: The Maven POM file for this project

-   `README.md`: This README file

## Build Requirements

You will need the following to build Jargyle:

-   JDK 9 or higher
-   Apache Maven 3.3.9 or higher

Once you have installed the requirements, be sure to have the environment 
variable `JAVA_HOME` set to the Java home directory.

## Frequently Used Maven Commands

The following are Maven commands that are frequently used for this project.
These commands are to be executed at the top directory of Jargyle.

-   `mvn clean`: Deletes directories and files created by this project.

-   `mvn clean compile site:site site:stage site:deploy`: Performs a clean 
    build and produces the website/documentation. 
    
    The produced website/documentation can be found in `docs/`. 
    
    Included in the website/documentation is the reference documentation. The 
    reference documentation is generated from special annotations within the 
    source code. If these annotations are added, changed, moved, removed, or 
    handled differently, you'll need to regenerate the reference 
    documentation. Here's how:
    
    1.   **Build the project:** This creates the necessary files to run the 
         reference documentation generator. The following command creates the 
         necessary files to run the reference documentation generator:
         
         ```bash
         # Perform a build of the binary distribution
         mvn clean package -DskipTests=true         
         ```
    
    2.   **Generate the reference documentation:** This step extracts the 
         information from the annotations and creates Markdown files. The 
         following command extracts the information from the annotations 
         and creates Markdown files:
         
         ```bash
         # Run Jargyle to generate Markdown reference documents to the directory of Markdown reference documentation
         jargyle-distributions/target/jargyle-5.0.0-SNAPSHOT-bin/jargyle-5.0.0-SNAPSHOT/bin/jargyle generate-reference-docs -d src/site/markdown/reference/
         ```
    
    3.   **Build the website/documentation**: This step takes the generated 
         Markdown files and integrates them into the website/documentation. 
         The following command takes the generated Markdown files and 
         integrates them into the website/documentation:
         
         ```bash
         # Produce the website/documentation with the updated reference documentation
         mvn compile site:site site:stage site:deploy
         ```
    
-   `mvn clean package -DskipTests=true`: Performs a clean build of the binary 
    and source distributions skipping the execution of all tests. 
    
    The built binary and source distributions can be found as directories and 
    in multiple archive formats in `jargyle-distributions/target/`.
    
-   `mvn clean test -Pcoverage`: Performs a clean build, executes all tests 
    except the integration tests, and produces the aggregated test coverage 
    reports.
    
    The aggregated test coverage reports can be found in
    `jargyle-report-aggregate/target/`.
    
    The option `-Pcoverage` can be removed if you do not want the aggregated 
    test coverage reports produced.
    
-   `mvn clean verify --projects=\!jargyle-test-echo-server-performance,\!jargyle-distributions -Pcoverage`: 
    Performs a clean build, executes all tests except the ones from the 
    project `jargyle-test-echo-server-performance`, skips building the binary 
    and source distributions, and produces the aggregated test coverage 
    reports.
    
    The argument `\!jargyle-test-echo-server-performance,` from the option 
    `--projects` can be removed if you want the performance tests to be 
    executed. If the performance tests are executed, the results can be found 
    in `jargyle-test-echo-server-performance/target/performance-results/`.
    
    The aggregated test coverage reports can be found in 
    `jargyle-report-aggregate/target/`. 
    
    The option `-Pcoverage` can be removed if you do not want the aggregated 
    test coverage reports produced.
    
    **Important: Port Requirements for Integration Tests**
    
    The integration tests use the loopback address and local port numbers. To 
    ensure the integration tests run correctly, the following local ports must 
    be available (not blocked by a firewall or other application):
    
    -   `1080`: This port is used by the SOCKS servers created during testing. 
        Ensure no other SOCKS server is running on this port.
    
    -   `8000` and `9000`: These ports are used by Kerberos Key Distribution 
        Center (KDC) instances for testing GSS-API/Kerberos functionality. 
        These are needed for running tests that involve GSS-API 
        authentication.
    