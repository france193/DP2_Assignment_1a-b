package it.polito.dp2.NFFG.sol1.myLib;

import java.io.File;
import java.util.*;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.NffgReader;

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
    public static final String PACKAGE = "it.polito.dp2.NFFG.sol1.jaxb";

    // set of Nffgs and Policy of all Nffgs
    private HashMap<String, FLNffgReader> myNffgs;
    private HashMap<String, FLPolicyReader> myPolicies;

    private NetworkService myNetworkServices = null;

    private String name_id;

    public FLNffgVerifier() throws SAXException, JAXBException {

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
        myNffgs = new HashMap<String, FLNffgReader>();
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
            // read in the link list, all links with this considered node as source
            for (NetworkService.Nffg.Link link : nffg.getLink()) {
                FLNodeReader srcNode = findNodeById(link.getLinkSourceNodeNameIdRefer(), myNffg.getNffgNodes());
                FLNodeReader destNode = findNodeById(link.getLinkDestinationNodeNameIdRefer(), myNffg.getNffgNodes());

                // if the node hasn't in the list the considered link, add it to the node's links list
                if (!srcNode.getLinks().contains(link)) {
                    srcNode.getLinks().add(new FLLinkReader(link.getLinkNameId(),
                            srcNode,
                            destNode));
                }
            }

            /** Policies **/
            System.out.println("In this NetworkService there are " + nffg.getReachabilityPolicyTypeOrTraversalPolicyType().size() + " policies");

            myPolicies = new HashMap<String, FLPolicyReader>();
            for (ReachabilityPolicyType policy : nffg.getReachabilityPolicyTypeOrTraversalPolicyType()) {

                // Reachability Policy
                if (!(policy instanceof TraversalPolicyType)) {
                    ReachabilityPolicyType p = (ReachabilityPolicyType) policy;
                    FLReachabilityPolicyReader myPolicy = new FLReachabilityPolicyReader(p.getPolicyNameId(),
                            myNffg,
                            p.isIsPositive(),
                            findNodeById(p.getPolicySourceNodeNameIdRefer(), myNffg.getNffgNodes()),
                            findNodeById(p.getPolicyDestinationNodeNameIdRefer(), myNffg.getNffgNodes()));

                    FLVerificationResultReader verificationResultReader = new FLVerificationResultReader(myPolicy,
                            p.isIsPositive(),
                            p.getVerificationMessage(),
                            p.getVerificationTime().toGregorianCalendar());

                    myPolicy.setPolicyVerificationReader(verificationResultReader);

                    myPolicies.put(myPolicy.getName(), myPolicy);

                } else {
                    // Traversal Policy

                    TraversalPolicyType p = (TraversalPolicyType) policy;
                    FLTraversalPolicyReader myPolicy = new FLTraversalPolicyReader(p.getPolicyNameId(),
                            myNffg,
                            p.isIsPositive(),
                            findNodeById(p.getPolicySourceNodeNameIdRefer(), myNffg.getNffgNodes()),
                            findNodeById(p.getPolicyDestinationNodeNameIdRefer(), myNffg.getNffgNodes()));

                    if (p.getTraversalRequestedNode().size() > 0) {
                        for (TraversalPolicyType.TraversalRequestedNode f : p.getTraversalRequestedNode()) {
                            myPolicy.getTraversedFuctionalTypes().add(FunctionalType.valueOf(f.getFunctionalType().value()));
                        }
                    }

                    FLVerificationResultReader verificationResultReader = new FLVerificationResultReader(myPolicy,
                            p.isIsPositive(),
                            p.getVerificationMessage(),
                            p.getVerificationTime().toGregorianCalendar());

                    myPolicy.setPolicyVerificationReader(verificationResultReader);

                    myPolicies.put(myPolicy.getName(), myPolicy);
                }

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

    private FLNodeReader findNodeById(String linkSourceNodeNameIdRefer, Set<FLNodeReader> nodes) {
        for (FLNodeReader n : nodes) {
            if (n.getName().contains(linkSourceNodeNameIdRefer)) {
                return n;
            }
        }

        return null;
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
