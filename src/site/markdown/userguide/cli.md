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
-   [Configuring Encryption and Decryption of XML Elements in a Server Configuration File](#configuring-encryption-and-decryption-of-xml-elements-in-a-server-configuration-file)

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

This command will generate reference documentation as Markdown files in the 
present working directory. To specify the destination directory for the output 
files, you can use the command line option and argument `-d DIRECTORY` where 
`DIRECTORY` is the specified destination directory for the output files. The 
command line option and argument are described in the 
[help information](../reference/cli-help-info.md#help-information-for-generate-reference-docs)
for the command `generate-reference-docs`.

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

<a id="creating-a-server-configuration-file-supplemented-with-command-line-options"></a>
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

<a id="creating-a-server-configuration-file-combined-from-server-configuration-files"></a>
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
-   `socksServerBindHostAddressTypes`
-   `socksServerBindPortRanges`
-   `socksServerNetInterface`
-   `socksServerSocketSettings`

A restart of the server would be required if you want any of the changed 
aforementioned settings to be applied to the running server.

<a id="configuring-encryption-and-decryption-of-xml-elements-in-a-server-configuration-file"></a>
## Configuring Encryption and Decryption of XML Elements in a Server Configuration File

Sensitive information in XML elements in a server configuration file produced 
by the command `new-server-config-file` is encrypted. Such information 
includes passwords. 

Command line example:

```bash
jargyle new-server-config-file \
    --setting=chaining.socksServerUri=socks5://jargyle.net:8080 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassmethod.username=Aladdin \
    --setting=chaining.socks5.userpassmethod.password=opensesame \
    local_configuration.xml
```

`local_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://jargyle.net:8080</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.userpassmethod.username</name>
            <value>Aladdin</value>
        </setting>
        <setting>
            <name>chaining.socks5.userpassmethod.password</name>
            <encryptedPassword>
                <typeName>AesCfbPkcs5PaddingEncryptedPassword</typeName>
                <encryptedPasswordValue>gO14zGu9U8usCtkEwgs6sQ==,4KGFoYUP9YVA0f/UbFCDAQ==,eWgdqYYZNT8=</encryptedPasswordValue>
            </encryptedPassword>
        </setting>
    </settings>
</configuration>
```

Encrypted sensitive information in XML elements in a server configuration file 
is also decrypted at runtime when the server configuration file is used as 
input for the commands `new-server-config-file` and `start-server`.

The following system properties are used for encryption and decryption:

-   `com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile`: 
the file of the partial password to be used for encryption/decryption
-   `com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword`: 
the partial password to be used for encryption/decryption

Together these system properties are used to create a password to be used for 
encryption and decryption. Both of them, one of them, or none of them can be 
set. By default, none of them are set. You can set either of them or both of 
them in the environment variable `JARGYLE_OPTS`.

Command line example:

```bash
export JARGYLE_OPTS=-Dcom.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword=donkeyhorsefruitcakepunch
jargyle new-server-config-file \
    --setting=chaining.socksServerUri=socks5://jargyle.net:8080 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassmethod.username=Aladdin \
    --setting=chaining.socks5.userpassmethod.password=opensesame \
    local_configuration.xml
jargyle start-server local_configuration.xml
```

Alternatively, you can use the following command line options in both 
`new-server-config-file` and `start-server`:

-   `--enter-partial-encryption-pass`: Enter through an interactive prompt the 
partial password to be used for encryption/decryption. This command line 
option assigns the partial password to the system property 
`com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword`.
-   `--partial-encryption-pass=PASSWORD`: The partial password to be used for 
encryption/decryption. This command line option assigns the partial password 
to the system property 
`com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword`.
-   `--partial-encryption-pass-file=FILE`: The file of the partial password to 
be used for encryption/decryption. This command line option assigns the file 
to the system property 
`com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile`. 

Command line example:

```bash
jargyle new-server-config-file \
    --partial-encryption-pass=donkeyhorsefruitcakepunch \
    --setting=chaining.socksServerUri=socks5://jargyle.net:8080 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassmethod.username=Aladdin \
    --setting=chaining.socks5.userpassmethod.password=opensesame \
    local_configuration.xml
jargyle start-server \
    --partial-encryption-pass=donkeyhorsefruitcakepunch \
    local_configuration.xml
```

The command line option `--enter-partial-encryption-pass` can be used if you 
do not want to have the partial password appear in any script or in any part 
of the command line history for security reasons.

Command line example:

```bash
jargyle new-server-config-file \
    --enter-partial-encryption-pass \
    --setting=chaining.socksServerUri=socks5://jargyle.net:8080 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassmethod.username=Aladdin \
    --enter-chaining-socks5-userpassmethod-pass \
    local_configuration.xml
Please enter the partial password to be used for encryption/decryption: 
Please enter the password to be used to access the other SOCKS server: 
jargyle start-server \
    --enter-partial-encryption-pass \
    local_configuration.xml
Please enter the partial password to be used for encryption/decryption: 
```

**Note**: If the same set of system properties and/or command line options are 
used in the making of server configuration files produced by 
`new-server-config-file`, then that same set of system properties and/or 
command line options must be present in `new-server-config-file` and 
`start-server` when any of those server configuration files are used as input. 
Also, input server configuration files can not be mixed if there are some that 
are produced by `new-server-config-file` with a different set of system 
properties and/or command line options. They must all be produced with the 
same set of system properties and/or command line options.