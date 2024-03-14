# Command Line Interface

The following are topics for using the command line interface.

## Page Contents

-   [Environment Variables](#environment-variables)
-   [Running the Command Line Interface](#running-the-command-line-interface)
-   [Generating Reference Documentation](#generating-reference-documentation)
-   [Managing SOCKS5 Users](#managing-socks5-users)
    -   [Adding SOCKS5 Users](#adding-socks5-users)
    -   [List All SOCKS5 Users](#list-all-socks5-users)
    -   [Removing a SOCKS5 User](#removing-a-socks5-user)
-   [Creating a Server Configuration File](#creating-a-server-configuration-file)
    -   [Creating a Server Configuration File Supplemented With Command Line Options](#creating-a-server-configuration-file-supplemented-with-command-line-options)
    -   [Creating a Server Configuration File Combined From Server Configuration Files](#creating-a-server-configuration-file-combined-from-server-configuration-files)
    -   [The Doc Setting and the Doc XML Element](#the-doc-setting-and-the-doc-xml-element)
-   [Starting the Server](#starting-the-server)
    -   [Starting the Server With a Monitored Server Configuration File](#starting-the-server-with-a-monitored-server-configuration-file)

## Environment Variables

The following are required environment variables Jargyle uses:

`JAVA_HOME`: Java home directory

The following are optional environment variables Jargyle uses:

`JARGYLE_HOME`: Jargyle home directory

`JARGYLE_OPTS`: Command line options used to start up the JVM running 
Jargyle and can be used to supply additional options to it. For example, JVM 
memory settings could be defined with the value `-Xms256m -Xmx512m`

## Running the Command Line Interface

The syntax for running the command line interface is as follows:

```text
jargyle COMMAND
```

`COMMAND` is the name of one of the commands described in the 
[help information](../reference/cli-help-info.md#help-information) for 
`jargyle`.

## Generating Reference Documentation

To generate reference documentation, you would run the following command:

```text
jargyle generate-reference-docs
```

This command will generate reference documentation as markdown files in 
the present working directory.

## Managing SOCKS5 Users

To manage SOCKS5 users, you would run the following command:

```text
jargyle manage-socks5-users USER_REPOSITORY COMMAND
```

`USER_REPOSITORY` is the user repository and `COMMAND` is the name of the 
command. Both are described in the 
[help information](../reference/cli-help-info.md#help-information-for-manage-socks5-users) 
for the command `manage-socks5-users`.

The following is one provided user repository you can use for 
`USER_REPOSITORY`:

-   `FileSourceUserRepository:FILE`: This user repository handles the 
storage of the SOCKS5 users from `FILE`: a provided file of a list of URL 
encoded username and hashed password pairs. The SOCKS5 users from the file are 
loaded onto memory. Because of this, you will need at least as much memory as 
the size of the file. If the file does not exist, it will be created and used. 
If the file does exist, the existing file will be used. 

### Adding SOCKS5 Users

To add SOCKS5 users to a user repository, you would run the following command:

```text
jargyle manage-socks5-users USER_REPOSITORY add
```

`USER_REPOSITORY` is the the user repository described in the 
[help information](../reference/cli-help-info.md#help-information-for-manage-socks5-users) 
for the command `manage-socks5-users`.

Once you have run the command, an interactive prompt will ask you for the new 
SOCKS5 user's name, password, and re-typed password. It will repeat the 
process to add another SOCKS5 user if you want to continue to enter another 
SOCKS5 user. If you do not want to enter any more SOCKS5 users, the new SOCKS5 
users will be saved.

Command line example:

```bash
jargyle manage-socks5-users FileSourceUserRepository:users add
User
Name: Aladdin
Password: 
Re-type password:
User 'Aladdin' added.
Would you like to enter another user? ('Y' for yes): Y
User
Name: Jasmine
Password: 
Re-type password:
User 'Jasmine' added.
Would you like to enter another user? ('Y' for yes): Y
User
Name: Abu
Password: 
Re-type password:
User 'Abu' added.
Would you like to enter another user? ('Y' for yes): Y
User
Name: Jafar
Password: 
Re-type password:
User 'Jafar' added.
Would you like to enter another user? ('Y' for yes): n
```

### List All SOCKS5 Users

To list all SOCKS5 users from a user repository, you would run the following 
command:

```text
jargyle manage-socks5-users USER_REPOSITORY list
```

`USER_REPOSITORY` is the user repository described in the 
[help information](../reference/cli-help-info.md#help-information-for-manage-socks5-users) 
for the command `manage-socks5-users`.

Once you have run the command, it will list all the SOCKS5 users from the user 
repository.

Command line example:

```bash
jargyle manage-socks5-users FileSourceUserRepository:users list
Aladdin
Jasmine
Abu
Jafar
```

### Removing a SOCKS5 User

To remove a SOCKS5 user from a user repository, you would run the following 
command:

```text
jargyle manage-socks5-users USER_REPOSITORY remove NAME
```

`USER_REPOSITORY` is the user repository described in the 
[help information](../reference/cli-help-info.md#help-information-for-manage-socks5-users) 
for the command `manage-socks5-users` and `NAME` is the specified name of 
the SOCKS5 user to be removed from the user repository.

Once you have run the command, the SOCKS5 user of the specified name will be 
removed from the user repository.

Command line example:

```bash
jargyle manage-socks5-users FileSourceUserRepository:users remove Jafar
User 'Jafar' removed
```

## Creating a Server Configuration File

To create a server configuration file, you would run the following command:

```text
jargyle new-server-config-file [OPTIONS] FILE
```

`[OPTIONS]` are optional command line options described in the 
[help information](../reference/cli-help-info.md#help-information-for-new-server-config-file) 
for the command `new-server-config-file` and `FILE` is the new server 
configuration file.

As an example, the following command creates an empty server configuration file:

```bash
jargyle new-server-config-file empty_configuration.xml
```

`empty_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings/>
</configuration>
```

As an example, the following command creates a server configuration file with 
the port number and the number of allowed backlogged incoming client connections:

```bash
jargyle new-server-config-file \
    --setting=port=1234 \
    --setting=backlog=100 \
    general_configuration.xml
```

`general_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>port</name>
            <value>1234</value>
        </setting>
        <setting>
            <name>backlog</name>
            <value>100</value>
        </setting>
    </settings>
</configuration>
```

### Creating a Server Configuration File Supplemented With Command Line Options

You can supplement an existing server configuration file with command line 
options.

As an example, the following command creates another server configuration file 
by adding one command line option after an earlier server configuration file:

```bash
jargyle new-server-config-file \
    --config-file=general_configuration.xml \
    --setting=socksServerSocketSettings=SO_TIMEOUT=0 \
    supplemented_general_configuration.xml
```

`supplemented_general_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>port</name>
            <value>1234</value>
        </setting>
        <setting>
            <name>backlog</name>
            <value>100</value>
        </setting>
        <setting>
            <name>socksServerSocketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_TIMEOUT</name>
                    <value>0</value>
                </socketSetting>
            </socketSettings>
        </setting>
    </settings>
</configuration>
```

### Creating a Server Configuration File Combined From Server Configuration Files

You can combine multiple server configuration files into one server 
configuration file.

As an example, the following commands create another server configuration file 
and then combine an earlier server configuration file with the new server 
configuration file into one:

```bash
jargyle new-server-config-file \
    --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED \
    socks5_configuration.xml
jargyle new-server-config-file \
    --config-file=supplemented_general_configuration.xml \
    --config-file=socks5_configuration.xml \
    combined_configuration.xml
```

`socks5_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

`combined_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>port</name>
            <value>1234</value>
        </setting>
        <setting>
            <name>backlog</name>
            <value>100</value>
        </setting>
        <setting>
            <name>socksServerSocketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_TIMEOUT</name>
                    <value>0</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

### The Doc Setting and the Doc XML Element

When using an existing server configuration file to create a new server 
configuration file, any XML comments from the existing server configuration 
file cannot be transferred to the new server configuration file. To preserve 
XML comments from one server configuration file to the next server 
configuration file, you can use either or both of the following: the setting 
`doc` and the `<doc/>` XML element.

The setting `doc` can be used for documentation purposes. It can be specified 
multiple times.

As an example, the following command creates a new server configuration file by 
combining earlier server configuration files each supplemented by command line 
options that document the start and end of a configuration:

```bash
jargyle new-server-config-file \
    "--setting=doc=Start of general settings" \
    --config-file=supplemented_general_configuration.xml \
    "--setting=doc=End of general settings" \
    "--setting=doc=Start of SOCKS5 settings" \
    --config-file=socks5_configuration.xml \
    "--setting=doc=End of SOCKS5 settings" \
    documented_combined_configuration.xml
```

`documented_combined_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>doc</name>
            <value>Start of general settings</value>
        </setting>
        <setting>
            <name>port</name>
            <value>1234</value>
        </setting>
        <setting>
            <name>backlog</name>
            <value>100</value>
        </setting>
        <setting>
            <name>socksServerSocketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_TIMEOUT</name>
                    <value>0</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>doc</name>
            <value>End of general settings</value>
        </setting>
        <setting>
            <name>doc</name>
            <value>Start of SOCKS5 settings</value>
        </setting>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>doc</name>
            <value>End of SOCKS5 settings</value>
        </setting>
    </settings>
</configuration>
```

The `<doc/>` XML element can also be used for documentation purposes. It can be 
used in the following XML elements:

-   `<setting/>`
-   `<socketSetting/>`

The `<doc/>` XML element can only be added in the aforementioned XML elements 
by editing the server configuration file to include it.

`documented_combined_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>doc</name>
            <value>Start of general settings</value>
        </setting>
        <setting>
            <name>port</name>
            <value>1234</value>
        </setting>
        <setting>
            <name>backlog</name>
            <value>100</value>
            <doc>Allow for 100 backlogged incoming client connections</doc>
        </setting>
        <setting>
            <name>socksServerSocketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_TIMEOUT</name>
                    <value>0</value>
                    <doc>No timeout in waiting for a connection from a client</doc>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>doc</name>
            <value>End of general settings</value>
        </setting>
        <setting>
            <name>doc</name>
            <value>Start of SOCKS5 settings</value>
        </setting>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>doc</name>
            <value>End of SOCKS5 settings</value>
        </setting>
    </settings>
</configuration>
```

## Starting the Server

To start the server without any command line arguments, you would run the 
following command:

```bash
jargyle start-server
```

The aforementioned command will start the server on port 1080 at address 
0.0.0.0.

Supplemental command line options including multiple server configuration files 
provided by the command line options `--config-file` can be included.

As an example, the following command starts the server with earlier server 
configuration files:

```bash
jargyle start-server \
    --config-file=supplemented_general_configuration.xml \
    --config-file=socks5_configuration.xml
```

### Starting the Server With a Monitored Server Configuration File

You can start the server with a server configuration file to be monitored for 
any changes that can then be applied to the running server.

To start the server with a monitored server configuration file, you would run 
the following command:

```text
jargyle start-server [MONITORED_CONFIG_FILE]
```

`[MONITORED_CONFIG_FILE]` is the optional server configuration file to be 
monitored for any changes that can then be applied to the running server.

As an example, the following command starts the server with an earlier server 
configuration file as the monitored server configuration file:

```bash
jargyle start-server combined_configuration.xml
```

When a monitored server configuration file is provided, any supplemental command 
line options including multiple server configuration files provided by the 
command line options `--config-file` will be ignored.

The following are the settings in the monitored server configuration file that 
if changed will have no effect during the running of the server:

-   `backlog`
-   `port`
-   `socksServerBindHost`
-   `socksServerBindPortRanges`
-   `socksServerSocketSettings`

A restart of the server would be required if you want any of the changed 
aforementioned settings to be applied to the running server.