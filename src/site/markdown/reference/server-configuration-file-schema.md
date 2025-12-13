# Server Configuration File Schema

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">

  <xs:element name="configuration" type="configuration"/>

  <xs:complexType name="configuration">
    <xs:sequence>
      <xs:element name="settings" type="settings" minOccurs="0"/>
    </xs:sequence>
  </xs:complexType>

  <xs:complexType name="settings">
    <xs:sequence>
      <xs:element name="setting" type="setting" minOccurs="0" maxOccurs="unbounded"/>
    </xs:sequence>
  </xs:complexType>

  <xs:complexType name="setting">
    <xs:all>
      <xs:element name="name" type="xs:string"/>
      <xs:choice>
        <xs:element name="encryptedPassword" type="encryptedPassword"/>
        <xs:element name="hostAddressTypes" type="hostAddressTypes"/>
        <xs:element name="portRanges" type="portRanges"/>
        <xs:element name="rule" type="rule"/>
        <xs:element name="socketSettings" type="socketSettings"/>
        <xs:element name="socks5.gssapiauthmethod.protectionLevels" type="socks5.gssapiauthmethod.protectionLevels"/>
        <xs:element name="socks5.methods" type="socks5.methods"/>
        <xs:element name="socks5.userpassauthmethod.userRepository" type="socks5.userpassauthmethod.userRepository"/>
        <xs:element name="value" type="xs:string"/>
        <xs:element name="values" type="values"/>
      </xs:choice>
      <xs:element name="doc" type="xs:string" minOccurs="0"/>
    </xs:all>
  </xs:complexType>

  <xs:complexType name="encryptedPassword">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:all>
          <xs:element name="typeName" type="xs:string"/>
          <xs:element name="encryptedPasswordValue" type="xs:string"/>
        </xs:all>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="value" abstract="true">
    <xs:sequence/>
  </xs:complexType>

  <xs:complexType name="hostAddressTypes">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:sequence>
          <xs:element name="hostAddressType" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
        </xs:sequence>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="portRanges">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:sequence>
          <xs:element name="portRange" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
        </xs:sequence>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="rule">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:all>
          <xs:element name="ruleConditions" type="ruleConditions" minOccurs="0"/>
          <xs:element name="ruleActions" type="ruleActions" minOccurs="0"/>
        </xs:all>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="ruleConditions">
    <xs:sequence>
      <xs:element name="ruleCondition" type="ruleCondition" minOccurs="0" maxOccurs="unbounded"/>
    </xs:sequence>
  </xs:complexType>

  <xs:complexType name="ruleCondition">
    <xs:all>
      <xs:element name="name" type="xs:string"/>
      <xs:element name="value" type="xs:string"/>
    </xs:all>
  </xs:complexType>

  <xs:complexType name="ruleActions">
    <xs:sequence>
      <xs:element name="ruleAction" type="ruleAction" minOccurs="0" maxOccurs="unbounded"/>
    </xs:sequence>
  </xs:complexType>

  <xs:complexType name="ruleAction">
    <xs:all>
      <xs:element name="name" type="xs:string"/>
      <xs:choice>
        <xs:element name="socketSetting" type="socketSetting"/>
        <xs:element name="value" type="xs:string"/>
      </xs:choice>
    </xs:all>
  </xs:complexType>

  <xs:complexType name="socketSetting">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:all>
          <xs:element name="name" type="xs:string"/>
          <xs:element name="value" type="xs:string"/>
          <xs:element name="doc" type="xs:string" minOccurs="0"/>
        </xs:all>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="socketSettings">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:sequence>
          <xs:element name="socketSetting" type="socketSetting" minOccurs="0" maxOccurs="unbounded"/>
        </xs:sequence>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="socks5.gssapiauthmethod.protectionLevels">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:sequence>
          <xs:element name="socks5.gssapiauthmethod.protectionLevel" type="xs:string" maxOccurs="unbounded"/>
        </xs:sequence>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="socks5.methods">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:sequence>
          <xs:element name="socks5.method" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
        </xs:sequence>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="socks5.userpassauthmethod.userRepository">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:all>
          <xs:element name="typeName" type="xs:string"/>
          <xs:element name="initializationString" type="xs:string"/>
        </xs:all>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>

  <xs:complexType name="values">
    <xs:complexContent>
      <xs:extension base="value">
        <xs:sequence>
          <xs:element name="value" type="xs:string" minOccurs="0" maxOccurs="unbounded"/>
        </xs:sequence>
      </xs:extension>
    </xs:complexContent>
  </xs:complexType>
</xs:schema>

```

