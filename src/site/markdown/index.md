# Jargyle 

Jargyle is a Java SOCKS5 server that has the following features:

-   Chain to multiple specified chains of SOCKS servers
-   Use SSL/TLS and DTLS for TCP and UDP traffic from clients and SOCKS servers
-   Resolve host names from an additional SOCKS5 command called RESOLVE

It also has a rule system that allows you to manage traffic in the following ways:

-   Allow or deny traffic
-   Allow a limited number of simultaneous instances of traffic
-   Route traffic through multiple selectable routes
-   Redirect the desired destination
-   Configure sockets
-   Configure relay settings
-   Limit relay bandwidth