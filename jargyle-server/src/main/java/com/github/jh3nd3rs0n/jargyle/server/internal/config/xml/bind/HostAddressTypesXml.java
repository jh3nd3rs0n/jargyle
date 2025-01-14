package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressType;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "hostAddressTypes")
class HostAddressTypesXml extends ValueXml {

    @XmlElement(name = "hostAddressType")
    protected List<String> hostAddressTypes;

    public HostAddressTypesXml() {
        this.hostAddressTypes = new ArrayList<>();
    }

    public HostAddressTypesXml(final HostAddressTypes hostAddrTypes) {
        this.hostAddressTypes = new ArrayList<>();
        for (HostAddressType hostAddrType : hostAddrTypes.toList()) {
            this.hostAddressTypes.add(hostAddrType.toString());
        }
    }

    public HostAddressTypes toHostAddressTypes() {
        List<HostAddressType> hostAddrTypes = new ArrayList<>();
        for (String hostAddressType : this.hostAddressTypes) {
            hostAddrTypes.add(HostAddressType.valueOfString(hostAddressType));
        }
        return HostAddressTypes.of(hostAddrTypes);
    }

    @Override
    public Object toValue() {
        return this.toHostAddressTypes();
    }

}
