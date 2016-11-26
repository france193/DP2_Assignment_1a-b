package it.polito.dp2.NFFG.sol1.myLib;

import java.io.File;
import java.util.*;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLNffgVerifier implements NffgVerifier {

    public static final String XSD_NAME = "xsd/WFInfo.xsd";
    public static final String PACKAGE = "it.polito.dp2.WF.sol2.jaxb";

    // set of Nffgs and Policy of all Nffgs
    private HashMap<String, FLNffgReader> myNffgs;
    private HashMap<String, FLPolicyReader> myPolicies;

    private NffgVerifier rootElement = null;

    private String name_id;

    public FLNffgVerifier() throws SAXException, JAXBException {

        try {
            // take file name from the property
            String fileName = System.getProperty("it.polito.dp2.WF.sol2.WorkflowInfo.file");
            rootElement = unmarshallDocument(new File(fileName));
        } catch (SAXException e) {
            System.err.println("SAXException: Error during the unmarshalling of the XML document...\n"
                    + e.getMessage());
            throw e;
        } catch (JAXBException e) {
            System.err.println("JAXBException: Error during the unmarshalling of the XML document...\n"
                    + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException: Error during the unmarshalling of the XML document...\n"
                    + e.getMessage());
            throw e;
        } catch (NullPointerException e) {
            System.err.println("NullPointerException: Error during the unmarshalling of the XML document...\n"
                    + e.getMessage());
            throw e;
        }

        System.out.println("In this NetworkService there are " + rootElement.getNffgs().size() + " Nffgs");

        // add all of my nffgs on my HashMap of my NffgReader
        myNffgs = new HashMap<String, FLNffgReader>();
        for (NffgReader nffg : rootElement.getNffgs()) {

            // create a new nffg
            FLNffgReader myNffg = new FLNffgReader(nffg.getName(), nffg.getUpdateTime());

            // add all information about the nffg
            /** Nodes **/
            System.out.println("In this Nffg there are " + nffg.getNodes().size() + " nodes");
            for (NodeReader node : nffg.getNodes()) {
                // create a new node
                FLNodeReader nodeReader = new FLNodeReader(node.getFuncType(), node.getName());

                /** Links **/
                System.out.println("This node has " + node.getLinks().size() + " links");
                for (LinkReader link : node.getLinks()) {
                    // create a new link
                    FLLinkReader linkReader = new FLLinkReader(link.getName(),
                            new FLNodeReader(link.getSourceNode().getFuncType(), link.getSourceNode().getName()),
                            new FLNodeReader(link.getDestinationNode().getFuncType(), link.getDestinationNode().getName()));
                    // add link to my node
                    nodeReader.getLinks().add(linkReader);
                }
                System.out.println(nodeReader.getLinks().size() + " links were created.");

                // add node to my nffg
                myNffg.getNodes().add(nodeReader);
            }
            System.out.println(myNffg.getNodes().size() + " nodes were created.");

            /** Policies **/
            System.out.println("In this NetworkService there are " + rootElement.getPolicies().size() + " policies");

            myPolicies = new HashMap<String, FLPolicyReader>();
            for (PolicyReader policy : rootElement.getPolicies()) {
                // create a new Policy
                FLPolicyReader policyReader = new FLPolicyReader(policy.getName(),
                        new FLNffgReader(policy.getNffg().getName(), policy.getNffg().getUpdateTime()),
                        policy.isPositive());

                FLVerificationResultReader verificationResultReader = new FLVerificationResultReader(policyReader,
                        policy.getResult().getVerificationResult(),
                        policy.getResult().getVerificationResultMsg(),
                        policy.getResult().getVerificationTime());

                policyReader.setPolicyVerificationReader(verificationResultReader);

                myPolicies.put(policyReader.getName(), policyReader);
            }
            System.out.println(myPolicies.size() + " policies were created.");

            myNffgs.put(myNffg.getName(), myNffg);
        }
    }

    @Override
    public Set<NffgReader> getNffgs() {
        return new TreeSet<NffgReader>(myNffgs.values());
    }

    @Override
    public FLNffgReader getNffg(String s) {
        return myNffgs.get(s);
    }

    @Override
    public Set<PolicyReader> getPolicies() {
        return new TreeSet<PolicyReader>(myPolicies.values());
    }

    @Override
    public Set<PolicyReader> getPolicies(String s) {
        Set<PolicyReader> policies = new TreeSet<PolicyReader>();

        for (Map.Entry<String, FLPolicyReader> entry : myPolicies.entrySet()) {
            if (entry.getValue().getNffg().getName().contains(s)) {
                policies.add(entry.getValue());
            }
        }

        return policies;
    }

    @Override
    public Set<PolicyReader> getPolicies(Calendar calendar) {
        Set<PolicyReader> policies = new TreeSet<PolicyReader>();

        for (Map.Entry<String, FLPolicyReader> entry : myPolicies.entrySet()) {
            if (entry.getValue().getResult() != null) {
                if (entry.getValue().getResult().getVerificationTime().after(calendar)) {
                    policies.add(entry.getValue());
                }
            }
        }

        return policies;
    }

    /**
     * This method converts a valid XML file into a {@link NffgVerifier} object.
     *
     * @param inputFile
     * @return
     * @throws JAXBException
     * @throws SAXException
     * @throws IllegalArgumentException
     */
    private NffgVerifier unmarshallDocument(File inputFile) throws JAXBException, SAXException, IllegalArgumentException {
        JAXBContext myJAXBContext = JAXBContext.newInstance(PACKAGE);

        SchemaFactory mySchemaFactory;
        Schema mySchema;

		/* - creating the XML schema to validate the XML file before read it - */
        try {
            mySchemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            mySchema = mySchemaFactory.newSchema(new File(XSD_NAME));
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException: Error! No implementation of the schema language is available");
            throw e;
        } catch (NullPointerException e) {
            System.err.println("NullPointerException: Error! The instance of the schema or the file of the schema is not well created!\n");
            throw e;
        } catch (SAXException e) {
            System.err.println("SAXException: Error! The instance of the schema or the file of the schema is not well created!\n");
            throw new SAXException("The schema file is null!");
        }

        Unmarshaller myUnmarshaller = myJAXBContext.createUnmarshaller();
        myUnmarshaller.setSchema(mySchema);

        return (NffgVerifier) myUnmarshaller.unmarshal(inputFile);
    }

    @Override
    public String toString() {
        StringBuffer tostring = new StringBuffer();

        for (FLNffgReader nffg : myNffgs.values()) {
            tostring.append(nffg.toString());
        }
        tostring.append("\n");
        tostring.append("-- Policies -- " + myPolicies.toString());

        return tostring.toString();
    }

}
