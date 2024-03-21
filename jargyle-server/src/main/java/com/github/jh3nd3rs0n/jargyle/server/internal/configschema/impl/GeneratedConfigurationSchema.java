package com.github.jh3nd3rs0n.jargyle.server.internal.configschema.impl;

import com.github.jh3nd3rs0n.jargyle.server.ConfigurationSchema;
import com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind.ConfigurationXml;

import java.io.IOException;
import java.io.OutputStream;

public class GeneratedConfigurationSchema extends ConfigurationSchema {

    @Override
    public void toOutput(final OutputStream out) throws IOException {
        ConfigurationXml.writeSchemaTo(out);
    }

}
