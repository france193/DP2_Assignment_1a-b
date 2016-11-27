package it.polito.dp2.NFFG.sol1.myLib;

import java.io.File;
import java.util.*;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.FunctionalType;

import it.polito.dp2.NFFG.sol1.jaxb_generated.TraversalPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb_generated.NetworkService;
import it.polito.dp2.NFFG.sol1.jaxb_generated.ReachabilityPolicyType;

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

    public static final String XSD_FOLDER = "xsd/";
    public static final String XSD_FILE = "nffgInfo.xsd";
    public static final String PACKAGE = "it.polito.dp2.NFFG.sol1.jaxb_generated";
    public static final String NO_RESULT_MSG = "No verification result for policy";

    // set of Nffgs and Policy of all Nffgs
    private Set<FLNffgReader> myNffgs;
    private Set<FLPolicyReader> myPolicies;

    private NetworkService myNetworkServices = null;

    public FLNffgVerifier() throws SAXException, JAXBException {

        myPolicies = new HashSet<FLPolicyReader>();

        try {
            // take file name from the property
            String fileName = System.getProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file");
            myNetworkServices = unmarshallDocument(new File(fileName));
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

        System.out.println("In this NetworkService there are " + myNetworkServices.getNffg().size() + " Nffgs");

        // add all of my nffgs on my HashMap of my NffgReader
        myNffgs = new HashSet<FLNffgReader>();
        for (NetworkService.Nffg nffg : myNetworkServices.getNffg()) {

            // create a new nffg
            FLNffgReader myNffg = new FLNffgReader(nffg.getNffgNameId(), nffg.getLastUpdatedTime().toGregorianCalendar());

            // add all information about the nffg
            /** Nodes **/
            System.out.println("In this Nffg there are " + nffg.getNode().size() + " nodes");
            for (NetworkService.Nffg.Node node : nffg.getNode()) {
                // create a new node
                FLNodeReader nodeReader = new FLNodeReader(FunctionalType.valueOf(node.getFunctionalType().toString()),
                        node.getNodeNameId());
                // add node to my nffg
                myNffg.getNodes().add(nodeReader);
            }
            System.out.println(myNffg.getNodes().size() + " nodes were created.");

            /** Links **/
            System.out.println("In this Nffg there are " + nffg.getLink().size() + " links");
            for (NetworkService.Nffg.Link link : nffg.getLink()) {
                FLNodeReader srcNode = fromNodeReaderTOFLNodeReader(myNffg.getNode(link.getLinkSourceNodeNameIdRefer()));
                FLNodeReader destNode = fromNodeReaderTOFLNodeReader(myNffg.getNode(link.getLinkDestinationNodeNameIdRefer()));

                        // if the node hasn't in the list the considered link, add it to the node's links list
                srcNode.getLinks().add(new FLLinkReader(link.getLinkNameId(), srcNode, destNode));
                //System.out.println("Link: " + link.getLinkNameId() + " was created.");
            }

            /** Policies **/
            System.out.println("In this Nffg there are " + nffg.getReachabilityPolicyTypeOrTraversalPolicyType().size() + " policies");
            for (ReachabilityPolicyType policy : nffg.getReachabilityPolicyTypeOrTraversalPolicyType()) {

                // Reachability Policy
                if (!(policy instanceof TraversalPolicyType)) {
                    ReachabilityPolicyType p = (ReachabilityPolicyType) policy;
                    FLReachabilityPolicyReader myPolicy = new FLReachabilityPolicyReader(p.getPolicyNameId(),
                            myNffg,
                            p.isIsPositive(),
                            fromNodeReaderTOFLNodeReader(myNffg.getNode(p.getPolicySourceNodeNameIdRefer())),
                            fromNodeReaderTOFLNodeReader(myNffg.getNode(p.getPolicyDestinationNodeNameIdRefer())));

                    FLVerificationResultReader verificationResultReader;

                    if ( p.getVerificationTime() == null) {
                        verificationResultReader = new FLVerificationResultReader(myPolicy,
                                null,
                                NO_RESULT_MSG,
                                null);
                    } else {
                        verificationResultReader = new FLVerificationResultReader(myPolicy,
                                p.isIsPositive(),
                                p.getVerificationMessage(),
                                p.getVerificationTime().toGregorianCalendar());
                    }

                    myPolicy.setPolicyVerificationReader(verificationResultReader);

                    myPolicies.add(myPolicy);

                } else {
                    // Traversal Policy

                    TraversalPolicyType p = (TraversalPolicyType) policy;
                    FLTraversalPolicyReader myPolicy = new FLTraversalPolicyReader(p.getPolicyNameId(),
                            myNffg,
                            p.isIsPositive(),
                            fromNodeReaderTOFLNodeReader(myNffg.getNode(p.getPolicySourceNodeNameIdRefer())),
                            fromNodeReaderTOFLNodeReader(myNffg.getNode(p.getPolicyDestinationNodeNameIdRefer())));

                    if (p.getTraversalRequestedNode().size() > 0) {
                        for (TraversalPolicyType.TraversalRequestedNode f : p.getTraversalRequestedNode()) {
                            myPolicy.getTraversedFuctionalTypes().add(FunctionalType.valueOf(f.getFunctionalType().value()));
                        }
                    }

                    FLVerificationResultReader verificationResultReader;

                    if ( p.getVerificationTime() == null) {
                        verificationResultReader = new FLVerificationResultReader(myPolicy,
                                null,
                                NO_RESULT_MSG,
                                null);
                    } else {
                        verificationResultReader = new FLVerificationResultReader(myPolicy,
                                p.isIsPositive(),
                                p.getVerificationMessage(),
                                p.getVerificationTime().toGregorianCalendar());
                    }

                    myPolicy.setPolicyVerificationReader(verificationResultReader);

                    myPolicies.add(myPolicy);
                }
                //System.out.println("created " + myPolicies.size() + " policies");

            }
            System.out.println(myPolicies.size() + " policies were created.");

            myNffgs.add(myNffg);
        }
    }

    private FLNodeReader fromNodeReaderTOFLNodeReader(NodeReader node) {
        return new FLNodeReader(FunctionalType.valueOf(node.getFuncType().value()), node.getName());
    }

    @Override
    public Set<NffgReader> getNffgs() {
        return new LinkedHashSet(this.myNffgs);
    }

    @Override
    public FLNffgReader getNffg(String s) {
        for (FLNffgReader nffg : myNffgs) {
            if (nffg.getName().contains(s)) {
                return nffg;
            }
        }
        return null;
    }

    @Override
    public Set<PolicyReader> getPolicies() {
        return new LinkedHashSet(this.myPolicies);
    }

    @Override
    public Set<PolicyReader> getPolicies(String s) {
        Set<PolicyReader> policies = new HashSet<PolicyReader>();

        for (FLPolicyReader policy : myPolicies) {
            if (policy.getNffg().getName().contains(s)) {
                policies.add(policy);
            }
        }

        return policies;
    }

    @Override
    public Set<PolicyReader> getPolicies(Calendar calendar) {
        Set<PolicyReader> policies = new HashSet<PolicyReader>();

        for (FLPolicyReader policy : myPolicies) {
            if (policy.getResult().getVerificationTime() != null) {
                policies.add(policy);
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
    private NetworkService unmarshallDocument(File inputFile) throws JAXBException, SAXException, IllegalArgumentException {
        JAXBContext myJAXBContext = JAXBContext.newInstance(PACKAGE);

        SchemaFactory mySchemaFactory;
        Schema mySchema;

		/* - creating the XML schema to validate the XML file before read it - */
        try {
            mySchemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            mySchema = mySchemaFactory.newSchema(new File(XSD_FOLDER + XSD_FILE));
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

        return (NetworkService) myUnmarshaller.unmarshal(inputFile);
    }

    @Override
    public String toString() {
        StringBuffer tostring = new StringBuffer();

        for (FLNffgReader nffg : myNffgs) {
            tostring.append(nffg.toString());
        }
        tostring.append("\n");
        tostring.append("-- Policies -- " + myPolicies.toString());

        return tostring.toString();
    }

}
