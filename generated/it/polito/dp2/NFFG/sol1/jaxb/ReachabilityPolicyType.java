//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.02 at 12:57:51 PM CET 
//


package it.polito.dp2.NFFG.sol1.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for reachabilityPolicyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reachabilityPolicyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="verificationResult" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="policy_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="result" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="message" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="policy_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nffg_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isPositive" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="policy_source_node_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="policy_destination_node_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reachabilityPolicyType", propOrder = {
    "verificationResult"
})
@XmlSeeAlso({
    TraversalPolicyType.class
})
public class ReachabilityPolicyType {

    protected ReachabilityPolicyType.VerificationResult verificationResult;
    @XmlAttribute(name = "policy_name_id", required = true)
    protected String policyNameId;
    @XmlAttribute(name = "nffg_name_id_refer", required = true)
    protected String nffgNameIdRefer;
    @XmlAttribute(name = "isPositive", required = true)
    protected boolean isPositive;
    @XmlAttribute(name = "policy_source_node_name_id_refer", required = true)
    protected String policySourceNodeNameIdRefer;
    @XmlAttribute(name = "policy_destination_node_name_id_refer", required = true)
    protected String policyDestinationNodeNameIdRefer;

    /**
     * Gets the value of the verificationResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReachabilityPolicyType.VerificationResult }
     *     
     */
    public ReachabilityPolicyType.VerificationResult getVerificationResult() {
        return verificationResult;
    }

    /**
     * Sets the value of the verificationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReachabilityPolicyType.VerificationResult }
     *     
     */
    public void setVerificationResult(ReachabilityPolicyType.VerificationResult value) {
        this.verificationResult = value;
    }

    /**
     * Gets the value of the policyNameId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyNameId() {
        return policyNameId;
    }

    /**
     * Sets the value of the policyNameId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyNameId(String value) {
        this.policyNameId = value;
    }

    /**
     * Gets the value of the nffgNameIdRefer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNffgNameIdRefer() {
        return nffgNameIdRefer;
    }

    /**
     * Sets the value of the nffgNameIdRefer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNffgNameIdRefer(String value) {
        this.nffgNameIdRefer = value;
    }

    /**
     * Gets the value of the isPositive property.
     * 
     */
    public boolean isIsPositive() {
        return isPositive;
    }

    /**
     * Sets the value of the isPositive property.
     * 
     */
    public void setIsPositive(boolean value) {
        this.isPositive = value;
    }

    /**
     * Gets the value of the policySourceNodeNameIdRefer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicySourceNodeNameIdRefer() {
        return policySourceNodeNameIdRefer;
    }

    /**
     * Sets the value of the policySourceNodeNameIdRefer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicySourceNodeNameIdRefer(String value) {
        this.policySourceNodeNameIdRefer = value;
    }

    /**
     * Gets the value of the policyDestinationNodeNameIdRefer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyDestinationNodeNameIdRefer() {
        return policyDestinationNodeNameIdRefer;
    }

    /**
     * Sets the value of the policyDestinationNodeNameIdRefer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyDestinationNodeNameIdRefer(String value) {
        this.policyDestinationNodeNameIdRefer = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="policy_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="result" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *       &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;attribute name="message" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class VerificationResult {

        @XmlAttribute(name = "policy_name_id_refer", required = true)
        protected String policyNameIdRefer;
        @XmlAttribute(name = "result")
        protected Boolean result;
        @XmlAttribute(name = "time")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar time;
        @XmlAttribute(name = "message")
        protected String message;

        /**
         * Gets the value of the policyNameIdRefer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPolicyNameIdRefer() {
            return policyNameIdRefer;
        }

        /**
         * Sets the value of the policyNameIdRefer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPolicyNameIdRefer(String value) {
            this.policyNameIdRefer = value;
        }

        /**
         * Gets the value of the result property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isResult() {
            return result;
        }

        /**
         * Sets the value of the result property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setResult(Boolean value) {
            this.result = value;
        }

        /**
         * Gets the value of the time property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getTime() {
            return time;
        }

        /**
         * Sets the value of the time property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setTime(XMLGregorianCalendar value) {
            this.time = value;
        }

        /**
         * Gets the value of the message property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets the value of the message property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMessage(String value) {
            this.message = value;
        }

    }

}
