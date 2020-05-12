# Jargyle

Jargyle is a Java SOCKS5 server. It has the following features:

- It is compliant with the [SOCKS5 specification](https://tools.ietf.org/html/rfc1928)

- It can have one or more of the following SOCKS5 authentication methods:

  - No authentication
  
  - Username/password authentication
  
  - GSS-API authentication
  
- It can have its external connections be set through another SOCKS5 server

Disclaimer:

Jargyle is a hobby project and is currently subject to breaking changes. Jargyle is currently not production ready but it aims to be.

## Contents

- <a href="#requirements">Requirements</a>

- <a href="#building">Building</a>

- <a href="#running">Running Jargyle</a>

  - <a href="#usage">Usage</a>
  
  - <a href="#creating_configuration_file">Creating a Configuration File</a>
  
  - <a href="#supplementing_configuration_file">Supplementing a Configuration File with Command Line Options</a>
  
  - <a href="#combining_configuration_files">Combining Configuration Files</a>
  
  - <a href="#running_with_configuration_file">Running Jargyle with a Configuration File</a>
  
  - <a href="#managing_socks5_users">Managing SOCKS5 Users (for Username/Password Authentication)</a>
  
  	- <a href="#creating_socks5_users_file">Creating a Users File</a>
  	
  	- <a href="#adding_socks5_users_to_existing_socks5_users_file">Adding Users to an Existing Users File</a>
  	
  	- <a href="#removing_socks5_user_from_existing_socks5_users_file">Removing a User from an Existing Users File</a>
  	
  - <a href="#using_socks5_authentication">Using SOCKS5 Authentication</a>
  
    - <a href="#using_socks5_no_authentication">Using No Authentication</a>
    
    - <a href="#using_socks5_username_password_authentication">Using Username/Password Authentication</a>
    
    - <a href="#using_socks5_gssapi_authentication">Using GSS-API Authentication</a>

- <a href="#integration_testing">Integration Testing</a>

- <a href="#todo">TODO</a>

- <a href="#contact">Contact</a>

<a name="requirements"></a>

## Requirements

- Apache Maven&#8482; 3.3.9 or higher 

- Java&#8482; SDK 1.8 or higher

<a name="building"></a>

## Building

To build and package Jargyle as an executable jar file, run the following commands:

<pre>

$ cd jargyle
$ mvn package

</pre>

<a name="running"></a>

## Running Jargyle 

To run Jargyle, you can run the following command:

<pre>

$ mvn exec:java

</pre>

If you have Jargyle packaged as an executable jar file, you can run the following command:

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar

</pre>

<a name="usage"></a>

### Usage

The following is the command line help for Jargyle (displayed when using the command line option `--help`):

<pre>

Usage: jargyle.server.SocksServer [OPTIONS]
       jargyle.server.SocksServer --config-file-xsd
       jargyle.server.SocksServer --help
       jargyle.server.SocksServer [OPTIONS] --new-config-file=FILE
       jargyle.server.SocksServer --settings-help
       jargyle.server.SocksServer --socks5-users ARGS

OPTIONS:
  --config-file=FILE, -f FILE
      The configuration file
  --config-file-xsd, -x
      Print the configuration file XSD and exit
  --enter-external-client-socks5-user-pass
      Enter through an interactive prompt the username/password for the external SOCKS5 server for external connections
  --external-client-socks5-user-pass=USERNAME:PASSWORD
      The username/password for the external SOCKS5 server for external connections
  --help, -h
      Print this help and exit
  --new-config-file=FILE, -n FILE
      Create a new configuration file based on the preceding options and exit
  --settings-help, -H
      Print the list of available settings for the SOCKS server and exit
  --settings=[NAME1=VALUE1[,NAME2=VALUE2[...]]], -s [NAME1=VALUE1[,NAME2=VALUE2[...]]]
      The comma-separated list of settings for the SOCKS server
  --socks5-user-pass-authenticator=CLASSNAME[:PARAMETER_STRING]
      The SOCKS5 username/password authenticator for the SOCKS server
  --socks5-users
      Mode for managing SOCKS5 users (add --help for more information)

</pre>

The following is a list of available settings for the SOCKS server (displayed when using the command line option `--settings-help`):

<pre>

SETTINGS:

  backlog=INTEGER_BETWEEN_0_AND_2147483647
      The maximum length of the queue of incoming connections (default is 50)

  clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
      The space separated list of socket settings for the client socket

  externalClient.connectTimeout=INTEGER_BETWEEN_1_AND_2147483647
      The timeout in milliseconds on waiting to TCP connect to the external SOCKS server for external connections (default is 60000)

  externalClient.externalServerUri=SCHEME://HOST[:PORT]
      The URI of the external SOCKS server for external connections.

  externalClient.socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
      The space separated list of socket settings to TCP connect to the external SOCKS server for external connections

  externalClient.socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]
      The space separated list of acceptable authentication methods to the external SOCKS5 server for external connections (default is NO_AUTHENTICATION_REQUIRED)

  externalClient.socks5.gssapiMechanismOid=GSSAPI_MECHANISM_OID
      The object ID for the GSSAPI authentication mechanism to the external SOCKS5 server for external connections (default is 1.2.840.113554.1.2.2)

  externalClient.socks5.gssapiNecReferenceImpl=true|false
      The boolean value to indicate if the exchange of the GSSAPI protection level negotiation must be unprotected should the external SOCKS5 server for external connections use the NEC reference implementation (default is false)

  externalClient.socks5.gssapiProtectionLevels=SOCKS5_GSSAPI_PROTECTION_LEVEL1[ SOCKS5_GSSAPI_PROTECTION_LEVEL2[...]]
      The space separated list of acceptable protection levels after GSSAPI authentication with the external SOCKS5 server for external connections (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)

  externalClient.socks5.gssapiServiceName=GSSAPI_SERVICE_NAME
      The GSSAPI service name for the external SOCKS5 server for external connections

  port=INTEGER_BETWEEN_1_AND_65535
      The port for the SOCKS server (default is 1080)

  socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
      The space separated list of socket settings for the SOCKS server

  socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]
      The space separated list of acceptable authentication methods in order of preference (default is NO_AUTHENTICATION_REQUIRED)

  socks5.gssapiNecReferenceImpl=true|false
      The boolean value to indicate if the exchange of the GSSAPI protection level negotiation must be unprotected according to the NEC reference implementation (default is false)

  socks5.gssapiProtectionLevels=SOCKS5_GSSAPI_PROTECTION_LEVEL1[ SOCKS5_GSSAPI_PROTECTION_LEVEL2[...]]
      The space separated list of acceptable protection levels after GSSAPI authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)

  socks5.onBind.incomingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
      The space separated list of socket settings for the incoming socket

  socks5.onBind.listenPortRanges=PORT_RANGE1[ PORT_RANGE2[...]]
      The space separated list of acceptable port ranges for the listen socket (default is 1-65535)

  socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
      The space separated list of socket settings for the listen socket

  socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
      The buffer size in bytes for relaying the data (default is 1024)

  socks5.onBind.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
      The timeout in milliseconds on relaying no data (default is 60000)

  socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
      The buffer size in bytes for relaying the data (default is 1024)

  socks5.onConnect.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
      The timeout in milliseconds on relaying no data (default is 60000)

  socks5.onConnect.serverConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
      The timeout in milliseconds on waiting the server-facing socket to connect (default is 60000)

  socks5.onConnect.serverSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
      The space separated list of socket settings for the server-facing socket

  socks5.onUdpAssociate.clientPortRanges=PORT_RANGE1[ PORT_RANGE2[...]]
      The space separated list of acceptable port ranges for the client-facing UDP socket (default is 1-65535)

  socks5.onUdpAssociate.clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
      The space separated list of socket settings for the client-facing UDP socket

  socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
      The buffer size in bytes for relaying the data (default is 32768)

  socks5.onUdpAssociate.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
      The timeout in milliseconds on relaying no data (default is 60000)

  socks5.onUdpAssociate.serverPortRanges=PORT_RANGE1[ PORT_RANGE2[...]]
      The space separated list of acceptable port ranges for the server-facing UDP socket (default is 1-65535)

  socks5.onUdpAssociate.serverSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
      The space separated list of socket settings for the server-facing UDP socket

SCHEMES:

  socks5
      SOCKS protocol version 5

SOCKET_SETTINGS:

  IP_TOS=INTEGER_BETWEEN_0_AND_255
      The type-of-service or traffic class field in the IP header for a TCP or UDP socket

  PERF_PREF=3_DIGITS_EACH_BETWEEN_0_AND_2
      Performance preferences for a TCP socket described by three digits whose values indicate the relative importance of short connection time, low latency, and high bandwidth

  SO_BROADCAST=true|false
      Can send broadcast datagrams

  SO_KEEPALIVE=true|false
      Keeps a TCP socket alive when no data has been exchanged in either direction

  SO_LINGER=INTEGER_BETWEEN_0_AND_2147483647
      Linger on closing the TCP socket in seconds

  SO_OOBINLINE=true|false
      Can receive TCP urgent data

  SO_RCVBUF=INTEGER_BETWEEN_1_AND_2147483647
      The receive buffer size

  SO_REUSEADDR=true|false
      Can reuse socket address and port

  SO_SNDBUF=INTEGER_BETWEEN_1_AND_2147483647
      The send buffer size

  SO_TIMEOUT=INTEGER_BETWEEN_0_AND_2147483647
      The timeout in milliseconds on waiting for an idle socket

  TCP_NODELAY=true|false
      Disables Nagle's algorithm

SOCKS5_AUTH_METHODS:

  NO_AUTHENTICATION_REQUIRED
      No authentication required

  GSSAPI
      GSS-API authentication

  USERNAME_PASSWORD
      Username/password authentication

SOCKS5_GSSAPI_PROTECTION_LEVELS:

  NONE
      No protection

  REQUIRED_INTEG
      Required per-message integrity

  REQUIRED_INTEG_AND_CONF
      Required per-message integrity and confidentiality

</pre>

The following is the command line help for managing SOCKS5 users for username/password authentication (displayed when using the command line options `--socks5-users --help`):

<pre>

Usage: jargyle.server.SocksServer --socks5-users COMMAND
       jargyle.server.SocksServer --socks5-users --help
       jargyle.server.SocksServer --socks5-users --xsd

COMMANDS:
  add-users-to-file FILE
      Add users to an existing file through an interactive prompt
  create-new-file FILE
      Create a new file of zero or more users through an interactive prompt
  remove-user NAME FILE
      Remove user by name from an existing file

OPTIONS:
  --help, -h
      Print this help and exit
  --xsd, -x
      Print the XSD and exit

</pre>

<a name="creating_configuration_file"></a>

### Creating a Configuration File

You can create a configuration file by using the command line option `--new-config-file`

The following command creates an empty configuration file:

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --new-config-file=configuration.xml

</pre>

`configuration.xml`:

<pre>

&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;configuration/&gt;

</pre>

Any preceding command line options that do not terminate before the command line option `--new-config-file` will be set in the new configuration file.

The following command creates a configuration file with the port number, the number of allowed backlogged connections, and no authentication required:

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --settings=port=1234,backlog=100,socks5.authMethods=NO_AUTHENTICATION_REQUIRED --new-config-file=configuration.xml

</pre>

`configuration.xml`:

<pre>

&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;configuration&gt;
    &lt;settings&gt;
        &lt;setting name="port" value="1234"/&gt;
        &lt;setting name="backlog" value="100"/&gt;
        &lt;setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED"/&gt;
    &lt;/settings&gt;
&lt;/configuration&gt;

</pre>
  
<a name="supplementing_configuration_file"></a>

### Supplementing a Configuration File with Command Line Options

You can supplement an existing configuration file with command line options.

The following command adds one command line options before the existing configuration file and another command line option after the existing configuration file:

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --settings=clientSocketSettings=SO_TIMEOUT=500 --config-file=configuration.xml --settings=socketSettings=SO_TIMEOUT=0 --new-config-file=new_configuration.xml

</pre>

`new_configuration.xml`:

<pre>

&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;configuration&gt;
    &lt;settings&gt;
        &lt;setting name="clientSocketSettings" value="SO_TIMEOUT=500"/&gt;
        &lt;setting name="port" value="1234"/&gt;
        &lt;setting name="backlog" value="100"/&gt;
        &lt;setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED"/&gt;
        &lt;setting name="socketSettings" value="SO_TIMEOUT=0"/&gt;
    &lt;/settings&gt;
&lt;/configuration&gt;

</pre>

<a name="combining_configuration_files"></a>

### Combining Configuration Files

You can combine multiple configuration files into one configuration file.

The following command combines the two earlier configuration files into one:

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --config-file=configuration.xml --config-file=new_configuration.xml --new-config-file=combined_configuration.xml

</pre>

`combined_configuration.xml`:

<pre>

&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;configuration&gt;
    &lt;settings&gt;
        &lt;setting name="port" value="1234"/&gt;
        &lt;setting name="backlog" value="100"/&gt;
        &lt;setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED"/&gt;
        &lt;setting name="clientSocketSettings" value="SO_TIMEOUT=500"/&gt;
        &lt;setting name="port" value="1234"/&gt;
        &lt;setting name="backlog" value="100"/&gt;
        &lt;setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED"/&gt;
        &lt;setting name="socketSettings" value="SO_TIMEOUT=0"/&gt;
    &lt;/settings&gt;
&lt;/configuration&gt;

</pre>

Although the redundant settings in the combined configuration file is unnecessary, the result configuration file is for demonstration purposes only.

Also, if a setting of the same name appears more than once in the configuration file, then only the last setting of the same name is recognized. 

<a name="running_with_configuration_file"></a>

### Running Jargyle with a Configuration File

To run Jargyle with a configuration file, you can use the command line option `--config-file`

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --config-file=configuration.xml

</pre>

Also the configuration file can be supplemented with command line options and/or combined with multiple configuration files.

<a name="managing_socks5_users"></a>

### Managing SOCKS5 Users (for Username/Password Authentication)

You can manage SOCKS5 users stored in an XML file called a users file. A users file can be used for <a href="#using_socks5_username_password_authentication">username/password authentication</a>.

<a name="creating_socks5_users_file"></a>

#### Creating a Users File

To create a users file, you would run the following command:

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --socks5-users create-new-file FILE

</pre>

Where `FILE` would be the name for the new users file.

Once you have run the command, an interactive prompt will ask you if you want to enter a user.

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --socks5-users create-new-file users.xml
Would you like to enter a user? ('Y' for yes): 

</pre>

If you do not want to enter a user, a new empty users file will be created. 

<pre>

Would you like to enter a user? ('Y' for yes): n
Writing to 'users.xml'...

</pre>

`users.xml`:

<pre>

&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;users/&gt;

</pre>

If you want to enter a user, the prompt will ask you for the user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you wish not to enter any more users, the new users file will be created.

<pre>

Would you like to enter a user? ('Y' for yes): Y
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
Name: Apu
Password: 
Re-type password:
User 'Apu' added.
Would you like to enter another user? ('Y' for yes): n
Writing to 'users.xml'...

</pre>

`users.xml`:

<pre>

&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;users&gt;
    &lt;user&gt;
        &lt;name&gt;Aladdin&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;Mm0d/rQ7dJ3O+2xA45OdDtW9RIl0UE89Nv6eOgJdR9Y=&lt;/hash&gt;
            &lt;salt&gt;W+G+W1MzLq8=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
    &lt;user&gt;
        &lt;name&gt;Jasmine&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;LSOdudNif0hR7bEEN7yk6/JFC9Qgc84TiX8hnT62+Ec=&lt;/hash&gt;
            &lt;salt&gt;IUuHz9cmLok=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
    &lt;user&gt;
        &lt;name&gt;Apu&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;Tl5wqiM//Bp/QI/bZIOvp0LrGl2+feI5dbb3YoVPrTI=&lt;/hash&gt;
            &lt;salt&gt;amDm0iiyKh4=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
&lt;/users&gt;

</pre>

<a name="adding_socks5_users_to_existing_socks5_users_file"></a>

#### Adding Users to an Existing Users File

To add users to an existing users file, you would run the following command:

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --socks5-users add-users-to-file FILE

</pre>

Where `FILE` would be the name for the existing users file.

Once you have run the command, an interactive prompt will ask you for the new user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you wish not to enter any more users, the updated users file will be saved.

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --socks5-users add-users-to-file users.xml
User
Name: Jafar
Password: 
Re-type password:
User 'Jafar' added.
Would you like to enter another user? ('Y' for yes): n
Writing to 'users.xml'...

</pre>

`users.xml`:

<pre>

&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;users&gt;
    &lt;user&gt;
        &lt;name&gt;Aladdin&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;Mm0d/rQ7dJ3O+2xA45OdDtW9RIl0UE89Nv6eOgJdR9Y=&lt;/hash&gt;
            &lt;salt&gt;W+G+W1MzLq8=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
    &lt;user&gt;
        &lt;name&gt;Jasmine&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;LSOdudNif0hR7bEEN7yk6/JFC9Qgc84TiX8hnT62+Ec=&lt;/hash&gt;
            &lt;salt&gt;IUuHz9cmLok=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
    &lt;user&gt;
        &lt;name&gt;Apu&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;Tl5wqiM//Bp/QI/bZIOvp0LrGl2+feI5dbb3YoVPrTI=&lt;/hash&gt;
            &lt;salt&gt;amDm0iiyKh4=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
    &lt;user&gt;
        &lt;name&gt;Jafar&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;Qw3KXi3cO63Nb9Hb8TmZXyY0TuqVurRt8uhLKE6sYys=&lt;/hash&gt;
            &lt;salt&gt;HXX9G9LYXTg=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
&lt;/users&gt;

</pre>

<a name="removing_socks5_user_from_existing_socks5_users_file"></a>

#### Removing a User from an Existing Users File

To remove a user from an existing users file, you would run the following command:

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --socks5-users remove-user NAME FILE

</pre>

Where `NAME` would be the name of the user and `FILE` would be the name for the existing users file.

Once you have run the command, the user of the specified name will be removed from the existing users file.

<pre>

$ java -jar jargyle-1.0-SNAPSHOT.jar --socks5-users remove-user Jafar users.xml
User 'Jafar' removed
Writing to 'users.xml'...

</pre>

`users.xml`:

<pre>

&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;users&gt;
    &lt;user&gt;
        &lt;name&gt;Aladdin&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;Mm0d/rQ7dJ3O+2xA45OdDtW9RIl0UE89Nv6eOgJdR9Y=&lt;/hash&gt;
            &lt;salt&gt;W+G+W1MzLq8=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
    &lt;user&gt;
        &lt;name&gt;Jasmine&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;LSOdudNif0hR7bEEN7yk6/JFC9Qgc84TiX8hnT62+Ec=&lt;/hash&gt;
            &lt;salt&gt;IUuHz9cmLok=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
    &lt;user&gt;
        &lt;name&gt;Apu&lt;/name&gt;
        &lt;passwordHash&gt;
            &lt;hash&gt;Tl5wqiM//Bp/QI/bZIOvp0LrGl2+feI5dbb3YoVPrTI=&lt;/hash&gt;
            &lt;salt&gt;amDm0iiyKh4=&lt;/salt&gt;
        &lt;/passwordHash&gt;
    &lt;/user&gt;
&lt;/users&gt;

</pre>

<a name="using_socks5_authentication"></a>

### Using SOCKS5 Authentication

Jargyle has the following SOCKS5 authentication methods to choose from:

- `NO_AUTHENTICATION_REQUIRED`: No authentication required

- `GSSAPI`: GSS-API authentication

- `USERNAME_PASSWORD`: Username/password authentication

You can have one or more of the aforementioned authentication methods set in the setting `socks5.authMethods` as a space separated list.

Partial command line example:

<pre>

"--settings=socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSSAPI"

</pre>

Partial configuration file example:

<pre>

&lt;setting name="socks5.authMethods" value="GSSAPI USERNAME_PASSWORD"/&gt;

</pre>

If not set, the default value for the setting `socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`

<a name="using_socks5_no_authentication"></a>

#### Using No Authentication

Because the default value for the setting `socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `socks5.authMethods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `socks5.authMethods`

Partial command line example:

<pre>

"--settings=socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD"

</pre>

Partial configuration file example:

<pre>

&lt;setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD"/&gt;

</pre>

<a name="using_socks5_username_password_authentication"></a>

#### Using Username/Password Authentication

To use username/password authentication, you will need to have the setting `socks5.authMethods` to have `USERNAME_PASSWORD` included.

Partial command line example:

<pre>

--settings=socks5.authMethods=USERNAME_PASSWORD

</pre>

Partial configuration file example:

<pre>

&lt;setting name="socks5.authMethods" value="USERNAME_PASSWORD"/&gt;

</pre>

Also, you will need to specify the name of the class that extends `jargyle.server.socks5.UsernamePasswordAuthenticator` along with a parameter string

The following are two provided classes you can use:

- `jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator`
- `jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator`

`jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the parameter string of a comma separated list of USERNAME:PASSWORD pairs

Partial command line example:

<pre>

--socks5-user-pass-authenticator=jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator:Aladdin:opensesame,Jasmine:Ali-Baba

</pre>

Partial configuration file example:

<pre>

&lt;socks5UsernamePasswordAuthenticator&gt;
	&lt;className&gt;jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator&lt;/className&gt;	
	&lt;parameterString&gt;Aladdin:opensesame,Jasmine:Ali-Baba&lt;/parameterString&gt;
&lt;/socks5UsernamePasswordAuthenticator&gt;

</pre>

`jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the <a href="#managing_socks5_users">XML file of users</a> whose file name is provided as a parameter string

Partial command line example:

<pre>

--socks5-user-pass-authenticator=jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator:users.xml

</pre>

Partial configuration file example:

<pre>

&lt;socks5UsernamePasswordAuthenticator&gt;
	&lt;className&gt;jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator&lt;/className&gt;	
	&lt;parameterString&gt;users.xml&lt;/parameterString&gt;
&lt;/socks5UsernamePasswordAuthenticator&gt;

</pre>

<a name="using_socks5_gssapi_authentication"></a>

#### Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `socks5.authMethods` to have `GSSAPI` included.

Partial command line example:

<pre>

--settings=socks5.authMethods=GSSAPI

</pre>

Partial configuration file example:

<pre>

&lt;setting name="socks5.authMethods" value="GSSAPI"/&gt;

</pre>

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API)

The following is a sufficient example of using the Kerberos security mechanism:

<pre>

$ java -Djavax.security.auth.useSubjectCredsOnly=false \
	-Djava.security.auth.login.configuration=login.conf \
	-Djava.security.krb5.conf=krb5.conf \
	-jar jargyle-1.0-SNAPSHOT.jar \
	--settings=socks5.authMethods=GSSAPI 

</pre>

The Java system property `-Djavax.security.auth.useSubjectCredsOnly=false` disables JAAS-based authentication to obtain the credentials directly. We will use Kerberos to obtain them instead.

The Java system property `-Djava.security.auth.login.configuration=login.conf` provides a JAAS configuration file to the underlying security mechanism.

`login.conf`:

<pre>

com.sun.security.jgss.accept  {
  com.sun.security.auth.module.Krb5LoginModule required
  useKeyTab=true
  keyTab="rcmd.keytab"
  storeKey=true
  principal="rcmd/localhost";
};

</pre> 

In `login.conf`, `rcmd/localhost` is a service principal that is created by a Kerberos administrator. `localhost` is to be the fully qualified domain name of where the SOCKS server resides but for this example it is left as `localhost`. `rcmd.keytab` is a keytab file also created by a Kerberos administrator that contains the aforementioned service principal and its respective encrypted key.  

The Java system property `-Djava.security.krb5.conf=krb5.conf` provides the Kerberos configuration file that points to the Kerberos Key Distribution Center (KDC) for authentication.   

`krb5.conf`:

<pre>

[libdefaults]
    kdc_realm = EXAMPLE.COM
    default_realm = EXAMPLE.COM
    kdc_udp_port = 12345
    kdc_tcp_port = 12345

[realms]
    EXAMPLE.COM = {
        kdc = localhost:12345
    }
    
</pre>

In `krb5.conf`, a KDC is defined as running on port 12345 with its realm as EXAMPLE.COM.  

<a name="integration_testing"></a>

## Integration Testing

To run integration testing, you would run the following command:

<pre>

$ mvn integration-test

</pre>

<a name="todo"></a>

## TODO

- Documentation in README.md on setting Jargyle's external connections be set through another SOCKS5 server.

- Javadoc documentation on all types

- Unit testing on other types

- Further documentation

  - Command line reference

  - Configuration file reference
  
  - Users file reference
  
<a name="contact"></a>

## Contact

If you have any questions or comments, you can e-mail me at `j0n4th4n.h3nd3rs0n@gmail.com`
