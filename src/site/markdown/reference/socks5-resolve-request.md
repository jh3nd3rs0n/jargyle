# SOCKS5 RESOLVE Request

The SOCKS5 RESOLVE request is used to resolve the provided host name, and in 
return, a reply is given with the resolved IPv4 or IPv6 address. At the time 
of this writing, the SOCKS5 RESOLVE request is an additional SOCKS5 request 
made for Jargyle. It is not a part of the SOCKS5 protocol specification. 

The following is the specification for defining a SOCKS5 RESOLVE request and 
the reply to that SOCKS5 request. It is described in expressions, names, and 
terms that are based off of the SOCKS5 protocol specification described in RFC 
[1928](https://datatracker.ietf.org/doc/html/rfc1928).

In a RESOLVE request, the `CMD` field is `X'04'`. The `ATYP` field SHOULD be 
`X'03'` (DOMAINNAME) and the `DST.ADDR` field SHOULD be a fully-qualified 
domain name with the first octet containing the number of octets of the name 
that follows. The `DST.PORT` field can be of any value in network octet order 
(`X'0000'` to `X'FFFF'` inclusive).

In a reply to a RESOLVE request, the `ATYP` field MUST be of any value other 
than `X'03'` (DOMAINNAME) and the `BND.ADDR` field MUST be the resolved 
address of the `DST.ADDR` field of the RESOLVE request. The `BND.PORT` field 
can be of any value in network octet order (`X'0000'` to `X'FFFF'` inclusive). 
If the `ATYP` field and the `DST.ADDR` field of the RESOLVE request is not a 
fully-qualified domain name, the `ATYP` field and the `BND.ADDR` field in the 
reply MUST be the same as the `ATYP` field and the `DST.ADDR` field of the 
RESOLVE request. After the reply is sent, the connection is then closed 
between the client and the server.