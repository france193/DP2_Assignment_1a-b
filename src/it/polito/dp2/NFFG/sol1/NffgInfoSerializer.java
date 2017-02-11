package it.polito.dp2.NFFG.sol1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.NFFG.FactoryConfigurationError;
import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NffgVerifierFactory;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.TraversalPolicyReader;
import it.polito.dp2.NFFG.sol1.jaxb.FLNodeFunctionalType;
import it.polito.dp2.NFFG.sol1.jaxb.LinkType;
import it.polito.dp2.NFFG.sol1.jaxb.NffgType;
import it.polito.dp2.NFFG.sol1.jaxb.Nffgs;
import it.polito.dp2.NFFG.sol1.jaxb.NodeType;
import it.polito.dp2.NFFG.sol1.jaxb.PolicyType;
import it.polito.dp2.NFFG.sol1.jaxb.VerificationResultType;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

/**
 * Created by Francesco Longo (223428) on 10/11/2016.
 *
 * Aim of this application is to read information passed from a NFFG random
 * generator and fill java classes representing my xsd schema. Then Marshalling
 * this document, it is possible to obtain a valid xml respecting constraints
 * obtained from the previous created xsd schema
 **/

public class NffgInfoSerializer {
    public static final String XSD_FOLDER = "xsd/";
    public static final String XSD_FILE = "nffgInfo.xsd";
    public static final String XSD_LOCATION = "https://france193.wordpress.com";
    public static final String PACKAGE = "it.polito.dp2.NFFG.sol1.jaxb";

    private NffgVerifier monitor;
    private static Nffgs myNffgs;

    private static String xml_filename;

    /**
     * Default constructror
     *
     * @throws NffgVerifierException
     */
    public NffgInfoSerializer() throws NffgVerifierException {
        NffgVerifierFactory factory = NffgVerifierFactory.newInstance();
        monitor = factory.newNffgVerifier();
    }

    /**
     *
     * @param args
     * @throws NffgVerifierException
     */
    public static void main(String[] args) throws NffgVerifierException {

        // check parameter
        if (args.length != 1) {
            System.err.println("(!) - Error! Usage: <program_name> <output.xml>");
            System.err.println("args.length is equal to " + args.length);
            return;
        }

        System.out.println(" > This program will serialize your Nffg into an XML file!");

        // read desidered name for new xml file
        xml_filename = args[0].toString();

        try {
            NffgInfoSerializer myInfoserializer = new NffgInfoSerializer();
            myInfoserializer.readAll();
            System.out.println(" > Data structure correctly created!\n");
            myInfoserializer.checkConstraints(myNffgs);
            System.out.println(" > Constraints correctly satisfied!\n");
            myInfoserializer.writeXMLToFile();
            System.out.println(" > File xml correctly generated!\n");
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
            throw new NffgVerifierException("(!) - Could not create a DocumentBuilderFactory: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new NffgVerifierException("(!) - Error, some argument are wrong!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new NffgVerifierException("(!) - Error! The file: " + args[0] + " does not exists!");
        } catch (SAXException e) {
            e.printStackTrace();
            throw new NffgVerifierException("(!) - Error! SAXExceptions!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new NffgVerifierException("(!) - Error! IOException!");
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new NffgVerifierException("(!) - Error! Conversion error from Calendar to XMLCalendar!");
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new NffgVerifierException("(!) - JAXBException: " + e.getMessage());
        }
    }

    private void readAll() throws DatatypeConfigurationException {
        // create a new NetworkServices root element
        myNffgs = new Nffgs();

        // NFFGS
        for (NffgReader nffg : monitor.getNffgs()) {

            // NFFG
            NffgType nffgType = new NffgType();
            nffgType.setName(nffg.getName());
            nffgType.setLastUpdatedTime(getXMLCal(nffg.getUpdateTime()));

            // NODES
            for (NodeReader node : nffg.getNodes()) {
                NodeType nodeType = new NodeType();
                nodeType.setName(node.getName());
                nodeType.setFunctionalType(FLNodeFunctionalType.valueOf(node.getFuncType().value()));

                // LINKS
                for (LinkReader link : node.getLinks()) {
                    LinkType linkType = new LinkType();
                    linkType.setName(link.getName());
                    linkType.setDestinationNode(link.getDestinationNode().getName());

                    // add links to node
                    nodeType.getLink().add(linkType);
                }

                // add node to nffg
                nffgType.getNode().add(nodeType);
            }

            // add nffg to nffgs
            myNffgs.getNffg().add(nffgType);
        }

        // POLICIES
        for (PolicyReader policy : monitor.getPolicies()) {
            PolicyType policyType = new PolicyType();

            // set common parameters of Policy
            policyType.setName(policy.getName());
            policyType.setNffgName(policy.getNffg().getName());
            policyType.setIsPositive(policy.isPositive());

            if (policy.getResult() != null) {
                VerificationResultType verificationResultType = new VerificationResultType();

                verificationResultType.setPolicy(policy.getName());
                verificationResultType.setResult(policy.getResult().getVerificationResult());
                verificationResultType.setTime(getXMLCal(policy.getResult().getVerificationTime()));
                verificationResultType.setMessage(policy.getResult().getVerificationResultMsg());

                myNffgs.getVerificationResult().add(verificationResultType);
            }

            if (policy instanceof ReachabilityPolicyReader) {
                // cast to Reachability policy to get Source and Destination
                // Node
                ReachabilityPolicyReader policy_r = (ReachabilityPolicyReader) policy;
                policyType.setSourceNode(policy_r.getSourceNode().getName());
                policyType.setDestinationNode(policy_r.getDestinationNode().getName());

                // if it is a traversal, cast to Traversal and read also
                // Traversed Node
                if (policy instanceof TraversalPolicyReader) {
                    TraversalPolicyReader policy_t = (TraversalPolicyReader) policy;

                    for (FunctionalType functionalType : policy_t.getTraversedFuctionalTypes()) {
                        policyType.getTraversedNode().add(FLNodeFunctionalType.valueOf(functionalType.value()));
                    }
                }
            }

            myNffgs.getPolicy().add(policyType);
        }
    }

    private void checkConstraints(Nffgs nffgs) throws JAXBException {
        for (PolicyType p : myNffgs.getPolicy()) {
            specificNffgContainsAllNodesWhichAPolicyRefersTo(p.getSourceNode(), p.getDestinationNode(), p.getNffgName(), nffgs);
        }
    }

    private void specificNffgContainsAllNodesWhichAPolicyRefersTo(String srcNode, String dstNode, String nffgName, Nffgs nffgs) throws JAXBException {
        NffgType n = findNffg(nffgName, nffgs);

        if (n != null) {
            if ( nodeExists(n, srcNode) == false ) {
                throw new JAXBException("Error: can't find srcNode: \"" + srcNode + "\" on Nffg: " + n.getName());
            }
            if ( nodeExists(n, dstNode) == false ) {
                throw new JAXBException("Error: can't find dstNode: \"" + dstNode + "\" on Nffg: " + n.getName());
            }
        }
    }

    private boolean nodeExists(NffgType n, String srcNode) {
        for (NodeType node : n.getNode()) {
            if (node.getName().equals(srcNode)) {
                return true;
            }
        }

        return false;
    }

    private NffgType findNffg(String name, Nffgs nffgs) {
        for (NffgType n : nffgs.getNffg()) {
            if (n.getName().equals(name)) {
                return n;
            }
        }

        return null;
    }

    private void writeXMLToFile() throws SAXException, IOException, JAXBException {
        try {
            // create jaxb context
            JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);

            // set validation with my xsd
            SchemaFactory mySchemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema mySchema;
            mySchema = mySchemaFactory.newSchema(new File(XSD_FOLDER + XSD_FILE));

            String extension;
            if (xml_filename.contains(".xml")) {
                extension = "";
            } else {
                extension = ".xml";
            }

            OutputStream fileOutputStream = new FileOutputStream(xml_filename + extension);

            // Creating the XML document
            Marshaller myMarchaller = jaxbContext.createMarshaller();
            myMarchaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            myMarchaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION + " " + XSD_FOLDER + XSD_FILE);
            myMarchaller.setSchema(mySchema);
            myMarchaller.marshal(myNffgs, fileOutputStream);

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("(!) - Error in the IO");
        } catch (NullPointerException e) {
            throw new NullPointerException(
                    "(!) - Error! The instance of the schema or the file of the schema is not well created!");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("(!) - Error! No implementation of the schema language is available");
        }
    }

    /**
     * If the conversion fail, it will generate a DatatypeConfigurationException
     *
     * @param calendar
     * @return
     * @throws DatatypeConfigurationException
     */
    private XMLGregorianCalendar getXMLCal(Calendar calendar) throws DatatypeConfigurationException {
        XMLGregorianCalendar xmlCal = null;
        GregorianCalendar cal = (GregorianCalendar) calendar;
        xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        return xmlCal;
    }
}
