//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.18 at 12:10:22 PM CET 
//


package it.polito.dp2.NFFG.sol1.jaxb_generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nffg" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="node" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="node_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="functional_type" use="required" type="{https://france193.wordpress.com}nodeFunctionalType" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="link" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="link_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="link_source_node_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="link_destination_node_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element name="reachabilityPolicyType" type="{https://france193.wordpress.com}reachabilityPolicyType"/>
 *                     &lt;element name="traversalPolicyType" type="{https://france193.wordpress.com}traversalPolicyType"/>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *                 &lt;attribute name="nffg_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="last_updated_time" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "nffg"
})
@XmlRootElement(name = "network_service")
public class NetworkService {

    protected List<Nffg> nffg;

    /**
     * Gets the value of the nffg property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nffg property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNffg().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nffg }
     */
    public List<Nffg> getNffg() {
        if (nffg == null) {
            nffg = new ArrayList<Nffg>();
        }
        return this.nffg;
    }


    /**
     * <p>Java class for anonymous complex type.
     * <p>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="node" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="node_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="functional_type" use="required" type="{https://france193.wordpress.com}nodeFunctionalType" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="link" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="link_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="link_source_node_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="link_destination_node_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;choice maxOccurs="unbounded" minOccurs="0">
     *           &lt;element name="reachabilityPolicyType" type="{https://france193.wordpress.com}reachabilityPolicyType"/>
     *           &lt;element name="traversalPolicyType" type="{https://france193.wordpress.com}traversalPolicyType"/>
     *         &lt;/choice>
     *       &lt;/sequence>
     *       &lt;attribute name="nffg_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="last_updated_time" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "node",
            "link",
            "reachabilityPolicyTypeOrTraversalPolicyType"
    })
    public static class Nffg {

        @XmlElement(required = true)
        protected List<Node> node;
        @XmlElement(required = true)
        protected List<Link> link;
        @XmlElements({
                @XmlElement(name = "reachabilityPolicyType", type = ReachabilityPolicyType.class),
                @XmlElement(name = "traversalPolicyType", type = TraversalPolicyType.class)
        })
        protected List<PolicyBaseType> reachabilityPolicyTypeOrTraversalPolicyType;
        @XmlAttribute(name = "nffg_name_id", required = true)
        protected String nffgNameId;
        @XmlAttribute(name = "last_updated_time", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar lastUpdatedTime;

        /**
         * Gets the value of the node property.
         * <p>
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the node property.
         * <p>
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getNode().add(newItem);
         * </pre>
         * <p>
         * <p>
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Node }
         */
        public List<Node> getNode() {
            if (node == null) {
                node = new ArrayList<Node>();
            }
            return this.node;
        }

        /**
         * Gets the value of the link property.
         * <p>
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the link property.
         * <p>
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLink().add(newItem);
         * </pre>
         * <p>
         * <p>
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Link }
         */
        public List<Link> getLink() {
            if (link == null) {
                link = new ArrayList<Link>();
            }
            return this.link;
        }

        /**
         * Gets the value of the reachabilityPolicyTypeOrTraversalPolicyType property.
         * <p>
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the reachabilityPolicyTypeOrTraversalPolicyType property.
         * <p>
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getReachabilityPolicyTypeOrTraversalPolicyType().add(newItem);
         * </pre>
         * <p>
         * <p>
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ReachabilityPolicyType }
         * {@link TraversalPolicyType }
         */
        public List<PolicyBaseType> getReachabilityPolicyTypeOrTraversalPolicyType() {
            if (reachabilityPolicyTypeOrTraversalPolicyType == null) {
                reachabilityPolicyTypeOrTraversalPolicyType = new ArrayList<PolicyBaseType>();
            }
            return this.reachabilityPolicyTypeOrTraversalPolicyType;
        }

        /**
         * Gets the value of the nffgNameId property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getNffgNameId() {
            return nffgNameId;
        }

        /**
         * Sets the value of the nffgNameId property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setNffgNameId(String value) {
            this.nffgNameId = value;
        }

        /**
         * Gets the value of the lastUpdatedTime property.
         *
         * @return possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getLastUpdatedTime() {
            return lastUpdatedTime;
        }

        /**
         * Sets the value of the lastUpdatedTime property.
         *
         * @param value allowed object is
         *              {@link XMLGregorianCalendar }
         */
        public void setLastUpdatedTime(XMLGregorianCalendar value) {
            this.lastUpdatedTime = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * <p>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="link_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="link_source_node_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="link_destination_node_name_id_refer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Link {

            @XmlAttribute(name = "link_name_id", required = true)
            protected String linkNameId;
            @XmlAttribute(name = "link_source_node_name_id_refer", required = true)
            protected String linkSourceNodeNameIdRefer;
            @XmlAttribute(name = "link_destination_node_name_id_refer", required = true)
            protected String linkDestinationNodeNameIdRefer;

            /**
             * Gets the value of the linkNameId property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getLinkNameId() {
                return linkNameId;
            }

            /**
             * Sets the value of the linkNameId property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setLinkNameId(String value) {
                this.linkNameId = value;
            }

            /**
             * Gets the value of the linkSourceNodeNameIdRefer property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getLinkSourceNodeNameIdRefer() {
                return linkSourceNodeNameIdRefer;
            }

            /**
             * Sets the value of the linkSourceNodeNameIdRefer property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setLinkSourceNodeNameIdRefer(String value) {
                this.linkSourceNodeNameIdRefer = value;
            }

            /**
             * Gets the value of the linkDestinationNodeNameIdRefer property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getLinkDestinationNodeNameIdRefer() {
                return linkDestinationNodeNameIdRefer;
            }

            /**
             * Sets the value of the linkDestinationNodeNameIdRefer property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setLinkDestinationNodeNameIdRefer(String value) {
                this.linkDestinationNodeNameIdRefer = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * <p>
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="node_name_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="functional_type" use="required" type="{https://france193.wordpress.com}nodeFunctionalType" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Node {

            @XmlAttribute(name = "node_name_id", required = true)
            protected String nodeNameId;
            @XmlAttribute(name = "functional_type", required = true)
            protected NodeFunctionalType functionalType;

            /**
             * Gets the value of the nodeNameId property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getNodeNameId() {
                return nodeNameId;
            }

            /**
             * Sets the value of the nodeNameId property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setNodeNameId(String value) {
                this.nodeNameId = value;
            }

            /**
             * Gets the value of the functionalType property.
             *
             * @return possible object is
             * {@link NodeFunctionalType }
             */
            public NodeFunctionalType getFunctionalType() {
                return functionalType;
            }

            /**
             * Sets the value of the functionalType property.
             *
             * @param value allowed object is
             *              {@link NodeFunctionalType }
             */
            public void setFunctionalType(NodeFunctionalType value) {
                this.functionalType = value;
            }

        }

    }

}
