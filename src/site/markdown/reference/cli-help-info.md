# Command Line Interface Help Information

## Page Contents

-   [Help Information](#help-information)
    -   [Help Information for manage-socks5-users](#help-information-for-manage-socks5-users)
    -   [Help Information for new-server-config-file](#help-information-for-new-server-config-file)
    -   [Help Information for start-server](#help-information-for-start-server)

## Help Information

```text
Usage: jargyle COMMAND
       jargyle --help
       jargyle --version

COMMANDS:
  generate-reference-docs
      Generate reference documents
  manage-socks5-users USER_REPOSITORY COMMAND
      Manage SOCKS5 users
  new-server-config-file [OPTIONS] FILE
      Create a new server configuration file based on the provided options
  start-server [OPTIONS] [MONITORED_CONFIG_FILE]
      Start the SOCKS server

OPTIONS:
  --help, -h
      Print this help and exit
  --version, -V
      Print version information and exit

```

### Help Information for manage-socks5-users

```text
Usage: jargyle manage-socks5-users USER_REPOSITORY COMMAND
       jargyle manage-socks5-users --help

USER_REPOSITORIES:

  FileSourceUserRepository:FILE
      User repository that handles the storage of the users from a provided file of a list of URL encoded username and hashed password pairs (If the file does not exist, it will be created and used.)

COMMANDS:

  add
      Add user(s) through an interactive prompt
  list
      List users to standard output
  remove NAME
      Remove user by name

OPTIONS:
  --help, -h
      Print this help and exit

```

### Help Information for new-server-config-file

```text
Usage: jargyle new-server-config-file [OPTIONS] FILE
       jargyle new-server-config-file --help
       jargyle new-server-config-file --settings-help

OPTIONS:
  --config-file=FILE, -f FILE
      A configuration file
  --enter-chaining-dtls-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
  --enter-chaining-socks5-userpassmethod-pass
      Enter through an interactive prompt the password to be used to access the other SOCKS5 server
  --enter-chaining-ssl-key-store-pass
      Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the other SOCKS server
  --enter-chaining-ssl-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the other SOCKS server
  --enter-dtls-key-store-pass
      Enter through an interactive prompt the password for the key store for the DTLS connections to the SOCKS server
  --enter-ssl-key-store-pass
      Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the SOCKS server
  --enter-ssl-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the SOCKS server
  --help, -h
      Print this help and exit
  --setting=NAME=VALUE, -s NAME=VALUE
      A setting for the SOCKS server
  --settings-help, -H
      Print the list of available settings for the SOCKS server and exit

```

### Help Information for start-server

```text
Usage: jargyle start-server [OPTIONS] [MONITORED_CONFIG_FILE]
       jargyle start-server --help
       jargyle start-server --settings-help

OPTIONS:
  --config-file=FILE, -f FILE
      A configuration file
  --enter-chaining-dtls-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
  --enter-chaining-socks5-userpassmethod-pass
      Enter through an interactive prompt the password to be used to access the other SOCKS5 server
  --enter-chaining-ssl-key-store-pass
      Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the other SOCKS server
  --enter-chaining-ssl-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the other SOCKS server
  --enter-dtls-key-store-pass
      Enter through an interactive prompt the password for the key store for the DTLS connections to the SOCKS server
  --enter-ssl-key-store-pass
      Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the SOCKS server
  --enter-ssl-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the SOCKS server
  --help, -h
      Print this help and exit
  --setting=NAME=VALUE, -s NAME=VALUE
      A setting for the SOCKS server
  --settings-help, -H
      Print the list of available settings for the SOCKS server and exit

```

