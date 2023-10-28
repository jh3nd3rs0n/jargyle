# Reference

## Page Contents

-   [String Value Syntaxes](#string-value-syntaxes)
    -   [Address Range](#address-range)
    -   [Port Range](#port-range)
-   [The SOCKS5 RESOLVE Command](#the-socks5-resolve-command)

## String Value Syntaxes

The following are common string value syntaxes that require further definition.

### Address Range

An address range can be specified in the following formats:

-   `ADDRESS` : Range is limited to a single address expressed in `ADDRESS`. 
Address can be an IPv4 address, an IPv6 address, or a domain name.
-   `ADDRESS1-ADDRESS2` : Range is limited to addresses between the address 
expressed in `ADDRESS1` (inclusive) and the address expressed in `ADDRESS2` 
(inclusive). `ADDRESS1` and `ADDRESS2` must be of the same address type 
(IPv4 or IPv6). `ADDRESS1` and `ADDRESS2` cannot be domain names.
-   `regex:REGULAR_EXPRESSION` : Range is limited to domain names that 
match the regular expression expressed in `REGULAR_EXPRESSION`

### Port Range

A port range can be specified in the following formats:

-   `PORT` : Range is limited to a single port number expressed in `PORT`
-   `PORT1-PORT2` : Range is limited to port numbers between the port number 
expressed in `PORT1` (inclusive) and the port number expressed in `PORT2` 
(inclusive)

## The SOCKS5 RESOLVE Command

The SOCKS5 RESOLVE command specifies the type of SOCKS5 request sent by the 
client for the server to perform: to resolve the provided host name and reply 
with the resolved IPv4 or IPv6 address. At the time of this writing, the SOCKS5 
RESOLVE command is an additional SOCKS5 command made for Jargyle. It is not a 
part of the SOCKS5 protocol specification. 

The following is the specification for defining a SOCKS5 request with the 
RESOLVE command and the reply to that SOCKS5 request. It is described in 
expressions, names, and terms that are based off of the SOCKS5 protocol 
specification described in RFC 
[1928](https://datatracker.ietf.org/doc/html/rfc1928).

In a SOCKS request, the RESOLVE command is represented as `X'04'` in the `CMD` 
field.  In the SOCKS request, the `ATYP` field SHOULD be `X'03'` (DOMAINNAME) 
and the `DST.ADDR` field SHOULD be a fully-qualified domain name with the 
first octet containing the number of octets of the name that follows. The 
`DST.PORT` field in the SOCKS request can be of any value in network octet 
order (`X'0000'` to `X'FFFF'` inclusive).

In reply to a SOCKS request with the RESOLVE command, the `ATYP` field in the 
reply MUST be of any value other than `X'03'` (DOMAINNAME) and the `BND.ADDR` 
field in the reply MUST be the resolved address of the `DST.ADDR` field of the 
SOCKS request. The `BND.PORT` field in the reply can be of any value in 
network octet order (`X'0000'` to `X'FFFF'` inclusive). If the `ATYP` field 
and the `DST.ADDR` field of the SOCKS request is not a fully-qualified domain 
name, the `ATYP` field and the `BND.ADDR` field in the reply MUST be the same 
as the `ATYP` field and the `DST.ADDR` field of the SOCKS request. After the 
reply is sent, the connection between the client and the server is then closed.
