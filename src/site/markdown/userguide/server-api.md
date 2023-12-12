# Server API

## Page Contents

-   [Introduction](#introduction)
-   [The Setting Object and the Settings Object](#the-setting-object-and-the-settings-object)
-   [The Configuration Object](#the-configuration-object)
-   [The SocksServer Object](#the-socksserver-object)

## Introduction

The main entry point object for the server API is the `SocksServer` object. To 
construct the `SocksServer` object, a `Configuration` object must be 
provided. Depending on how a `Configuration` object is constructed, a 
`Settings` object containing zero to many `Setting` objects must be provided.

The following is a simple example of constructing a `SocksServer` object:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        
        Setting<Object> setting = 
            Setting.newInstanceWithParsableValue(
                "port", "1234");
        
        Settings settings = Settings.newInstance(setting);
        
        Configuration configuration = 
            Configuration.newUnmodifiableInstance(settings);
        
        SocksServer socksServer = new SocksServer(configuration);
        
        // ...
    }
}
```

## The Setting Object and the Settings Object

The simplest way to create a `Setting` object is to use the method 
`Setting.newInstanceWithParsableValue(String, String)`. The first 
`String` parameter would be the name of the setting. The second `String` 
parameter would be the parsable value of the setting.

Server API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        
        Setting<Object> port = 
            Setting.newInstanceWithParsableValue("port", "1234");
        
        Setting<Object> backlog = 
            Setting.newInstanceWithParsableValue(
                "backlog", "100");
        
        Setting<Object> socksServerSocketSettings = 
            Setting.newInstanceWithParsableValue(
                "socksServerSocketSettings", "SO_TIMEOUT=0");
        
        // ...
    }
}
```

A complete listing of the settings can be found 
[here](../reference/server-configuration-settings.md).

A `Settings` object can be created by using the method 
`Settings.newInstance(Setting...)`. The parameter is a varargs parameter 
of `Setting` objects.

Server API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        
        Setting<Object> port = Setting.newInstanceWithParsableValue(
            "port", "1234");
        
        Setting<Object> backlog = 
            Setting.newInstanceWithParsableValue(
                "backlog", "100");
        
        Setting<Object> socksServerSocketSettings = 
            Setting.newInstanceWithParsableValue(
                "socksServerSocketSettings", "SO_TIMEOUT=0");
        
        Settings settings = Settings.newInstance(
            port, backlog, socksServerSocketSettings);
        
        // ...
    }
}
```

## The Configuration Object

A `Configuration` object can be constructed by any of the following methods:

-   `Configuration.newModifiableInstance(Settings)`: Constructs a 
modifiable `Configuration` object with the provided `Settings` object.
-   `Configuration.newUnmodifiableInstance(Settings)`: Constructs an 
unmodifiable `Configuration` object with the provided `Settings` object. 
Any method that is to modify the `Configuration` object will throw a 
`UnsupportedOperationException`.
-   `Configuration.newUpdatedInstance(ConfigurationRepository)`: 
Constructs an updated `Configuration` object with the provided 
`ConfigurationRepository` object. A `ConfigurationRepository` object 
provides the `Configuration` object from another source such as a file.

Server API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        
        Setting<Object> port = Setting.newInstanceWithParsableValue(
            "port", "1234");
        
        Setting<Object> backlog = 
            Setting.newInstanceWithParsableValue(
                "backlog", "100");
        
        Setting<Object> socksServerSocketSettings = 
            Setting.newInstanceWithParsableValue(
                "socksServerSocketSettings", "SO_TIMEOUT=0");
        
        Settings settings = Settings.newInstance(
            port, backlog, socksServerSocketSettings);
        
        Configuration configuration =
            Configuration.newUnmodifiableInstance(settings);
        
        // ...
    }
}
```

## The SocksServer Object

As mentioned earlier, to construct the `SocksServer` object, a 
`Configuration` object must be provided.

Server API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        
        Setting<Object> port = Setting.newInstanceWithParsableValue(
            "port", "1234");
        
        Setting<Object> backlog = 
            Setting.newInstanceWithParsableValue(
                "backlog", "100");
        
        Setting<Object> socksServerSocketSettings = 
            Setting.newInstanceWithParsableValue(
                "socksServerSocketSettings", "SO_TIMEOUT=0");
        
        Settings settings = Settings.newInstance(
            port, backlog, socksServerSocketSettings);
        
        Configuration configuration =
            Configuration.newUnmodifiableInstance(settings);
        
        SocksServer socksServer = new SocksServer(configuration);
        
        // ...
    }
}
```

The `SocksServer` object has the following methods:

-   `getConfiguration()`: Returns the `Configuration` object provided to 
this `SocksServer` object
-   `getHost()`: Returns the `Host` object that this `SocksServer` object 
is bound to when the state of this `SocksServer` is set to `STARTED`. 
Otherwise it returns `null`
-   `getPort()`: Returns the `Port` object that this `SocksServer` object 
is bound to when the state of this `SocksServer` is set to `STARTED`. 
Otherwise it returns `null`
-   `getState()`: Returns the state of this `SocksServer` object
-   `start()`: Starts the `SocksServer` object
-   `stop()`: Stops the `SocksServer` object

Server API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        
        Setting<Object> port = Setting.newInstanceWithParsableValue(
            "port", "1234");
        
        Setting<Object> backlog = 
            Setting.newInstanceWithParsableValue(
                "backlog", "100");
        
        Setting<Object> socksServerSocketSettings = 
            Setting.newInstanceWithParsableValue(
                "socksServerSocketSettings", "SO_TIMEOUT=0");
        
        Settings settings = Settings.newInstance(
            port, backlog, socksServerSocketSettings);
        
        Configuration configuration =
            Configuration.newUnmodifiableInstance(settings);
        
        SocksServer socksServer = new SocksServer(configuration);
        
        socksServer.start();
        
        System.out.println(
            "Listening on port " 
            + socksServer.getPort() 
            + " at " 
            + socksServer.getHost());
    }
}
```