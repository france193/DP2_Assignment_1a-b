//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.14 at 09:58:04 PM CET 
//


package it.polito.dp2.NFFG.sol1.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nodeFunctionalType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="nodeFunctionalType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="firewall"/>
 *     &lt;enumeration value="dpi"/>
 *     &lt;enumeration value="nat"/>
 *     &lt;enumeration value="anti_spam"/>
 *     &lt;enumeration value="web_cache"/>
 *     &lt;enumeration value="vpn_gateway"/>
 *     &lt;enumeration value="web_server"/>
 *     &lt;enumeration value="web_client"/>
 *     &lt;enumeration value="mail_server"/>
 *     &lt;enumeration value="mail_client"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "nodeFunctionalType")
@XmlEnum
public enum NodeFunctionalType {

    @XmlEnumValue("firewall")
    FIREWALL("firewall"),
    @XmlEnumValue("dpi")
    DPI("dpi"),
    @XmlEnumValue("nat")
    NAT("nat"),
    @XmlEnumValue("anti_spam")
    ANTI_SPAM("anti_spam"),
    @XmlEnumValue("web_cache")
    WEB_CACHE("web_cache"),
    @XmlEnumValue("vpn_gateway")
    VPN_GATEWAY("vpn_gateway"),
    @XmlEnumValue("web_server")
    WEB_SERVER("web_server"),
    @XmlEnumValue("web_client")
    WEB_CLIENT("web_client"),
    @XmlEnumValue("mail_server")
    MAIL_SERVER("mail_server"),
    @XmlEnumValue("mail_client")
    MAIL_CLIENT("mail_client");
    private final String value;

    NodeFunctionalType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NodeFunctionalType fromValue(String v) {
        for (NodeFunctionalType c: NodeFunctionalType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
