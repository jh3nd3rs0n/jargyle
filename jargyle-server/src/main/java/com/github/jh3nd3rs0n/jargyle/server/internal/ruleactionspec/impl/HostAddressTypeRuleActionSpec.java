package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressType;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class HostAddressTypeRuleActionSpec
        extends RuleActionSpec<HostAddressType> {

    public HostAddressTypeRuleActionSpec(final String n) {
        super(n, HostAddressType.class);
    }

    @Override
    protected HostAddressType parse(final String value) {
        return HostAddressType.valueOfString(value);
    }

}
