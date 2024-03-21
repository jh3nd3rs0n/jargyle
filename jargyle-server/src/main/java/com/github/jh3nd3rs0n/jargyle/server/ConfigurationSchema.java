package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.server.internal.configschema.impl.GeneratedConfigurationSchema;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ConfigurationSchema {

    public static ConfigurationSchema newGeneratedInstance() {
        return new GeneratedConfigurationSchema();
    }

    public abstract void toOutput(final OutputStream out) throws IOException;

}
