package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

final class Holder<T> {

    private T object;

    public Holder() {
    }

    public T get() {
        return this.object;
    }

    public void set(final T obj) {
        this.object = obj;
    }

}
