package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class NetInterfaceRuleActionSpec
        extends RuleActionSpec<NetInterface> {

    public NetInterfaceRuleActionSpec(final String n) {
        super(n, NetInterface.class);
    }

    @Override
    protected NetInterface parse(final String value) {
        return NetInterface.newInstanceFrom(value);
    }

}
