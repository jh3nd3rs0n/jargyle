# Frequently Asked Questions

## Contents

-   [Why?](#why)
-   [Why the name Jargyle?](#why-the-name-jargyle)
-   [Will SOCKS4(a) be supported?](#will-socks4a-be-supported)

## Why?

TL;DR: For fun and a desire to make something potentially useful for others.

Jargyle came from a discarded project. Instead of discarding the Java SOCKS5 
server source code with the rest of the project, the Java source code was used 
to create a SOCKS server that implemented all the SOCKS5 protocol 
specification described in RFC 
[1928](https://datatracker.ietf.org/doc/html/rfc1928) for educational purposes 
for others. Included in that specification was GSS-API authentication and 
encapsulation.

Because most applications rarely access a SOCKS server using GSS-API 
authentication, the idea of a locally accessible SOCKS server that required no 
authentication routing traffic through a remote SOCKS server that required 
GSS-API authentication became desirable. The routing of traffic through 
another SOCKS server, or chaining, however, required the SOCKS server to use a 
SOCKS client API capable of routing traffic directly and routing traffic 
through another SOCKS server. So a SOCKS client API and chaining both became 
features. 

Since setting up a security mechanism that implements the GSS-API (such as 
Kerberos) can be a bit difficult, the idea of a SOCKS server having its 
traffic between clients and other SOCKS servers layered over with SSL/TLS/DTLS 
became desirable. This too became a feature.

As features were being added, Jargyle began stand out as unique compared to 
other Java SOCKS APIs and servers (at least from the author's viewpoint).

But the driving force behind Jargyle was for fun and a desire to make 
something potentially useful for others. 

## Why the name Jargyle?

The beginning "J" of the name is the naming convention for some Java 
applications (for example, JEdit and JCreator). The rest of the name, 
"argyle", is usually associated with socks which is the name of the protocol 
that Jargyle deals with. The name would have been "JArgyle" which would have 
been pronounced in four syllables, but the current name "Jargyle" is 
pronounced in three syllables. Also, the name rolls off the tongue easily. 

<a id="will-socks4a-be-supported"></a>
## Will SOCKS4(a) be supported?

Although the code leaves open for the possibility of supporting SOCKS4(a), 
there are no plans for such support in the foreseeable future.
