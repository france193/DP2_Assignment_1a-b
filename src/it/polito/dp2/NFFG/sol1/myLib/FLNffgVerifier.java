package it.polito.dp2.NFFG.sol1.myLib;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.VerificationResultReader;

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

    private DateFormat dateFormat;

    private static final Boolean DEBUG = false;

    private static final String XSD_FOLDER = "xsd/";
    private static final String XSD_FILE = "nffgInfo.xsd";
    private static final String PACKAGE = "it.polito.dp2.NFFG.sol1.jaxb_generated";
    private static final String NO_RESULT_MSG = "No verification result for policy";

    // set of Nffgs and Policy of all Nffgs
    private Set<NffgReader> FLNffg;
    private Set<PolicyReader> myPolicies;

    private NetworkService myNetworkServices = null;

    public FLNffgVerifier() throws SAXException, JAXBException {

        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        myPolicies = new HashSet<PolicyReader>();

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

        System.out.println("---> In this NetworkService there are " + myNetworkServices.getNffg().size() + " Nffgs");

        // add all of my nffgs on my HashMap of my NffgReader
        FLNffg = new HashSet<NffgReader>();
        for (NetworkService.Nffg t_nffg : myNetworkServices.getNffg()) {

            // NEW NFFG
            FLNffgReader flNffgReader = new FLNffgReader(t_nffg.getNffgNameId().toString(),
                    t_nffg.getLastUpdatedTime().toGregorianCalendar());

            /** Nodes **/
            //System.out.println("--> In this Nffg there are " + t_nffg.getNode().size() + " nodes");
            System.out.println("This Nffg has " + t_nffg.getLink().size() + " links");
            for (NetworkService.Nffg.Node node : t_nffg.getNode()) {
                // create a new node
                FLNodeReader nodeReader = new FLNodeReader(FunctionalType.valueOf(node.getFunctionalType().toString()),
                        node.getNodeNameId());
                // add node to my nffg
                flNffgReader.getNodes().add(nodeReader);
            }
            //System.out.println("--> " + flNffgReader.getNodes().size() + " nodes were created.");

            /** Links **/
            for (NetworkService.Nffg.Node node : t_nffg.getNode()) {
                for (NetworkService.Nffg.Link link : t_nffg.getLink()) {
                    if (node.getNodeNameId().toString().contains(link.getLinkSourceNodeNameIdRefer().toString())) {

                        FLLinkReader linkReader = new FLLinkReader(link.getLinkNameId(),
                                flNffgReader.getNode(link.getLinkSourceNodeNameIdRefer().toString()),
                                flNffgReader.getNode(link.getLinkDestinationNodeNameIdRefer().toString()));

                        flNffgReader.getNode(link.getLinkSourceNodeNameIdRefer().toString()).getLinks().add(linkReader);
                    }
                }
                System.out.println("--> " + flNffgReader.getNode(node.getNodeNameId()).getLinks().size() + " links were created.");
            }

            /** Policies **/
            System.out.println("---> " + flNffgReader.getName() + " has " + t_nffg.getReachabilityPolicyTypeOrTraversalPolicyType().size() + " policies");
            for (ReachabilityPolicyType policy : t_nffg.getReachabilityPolicyTypeOrTraversalPolicyType()) {

                if (!(policy instanceof TraversalPolicyType)) {

                    FLReachabilityPolicyReader flReachabilityPolicyReader = new FLReachabilityPolicyReader(policy.getPolicyNameId(),
                            flNffgReader,
                            policy.isIsPositive(),
                            fromNodeReaderTOFLNodeReader(flNffgReader.getNode(policy.getPolicySourceNodeNameIdRefer().toString())),
                            fromNodeReaderTOFLNodeReader(flNffgReader.getNode(policy.getPolicyDestinationNodeNameIdRefer().toString())));

                    FLVerificationResultReader verificationResultReader;

                    if ((policy.getVerificationTime() != null) &&
                            (policy.getVerificationMessage() != null) &&
                            (policy.isVerificationResult() != null)) {
                        verificationResultReader = new FLVerificationResultReader(flReachabilityPolicyReader,
                                policy.isIsPositive(),
                                policy.getVerificationMessage(),
                                policy.getVerificationTime().toGregorianCalendar());
                        System.out.println("(R) " + flReachabilityPolicyReader.getName() + " - Has been verified!");
                    } else {
                        verificationResultReader = new FLVerificationResultReader(flReachabilityPolicyReader,
                                null,
                                null,
                                null);
                        System.out.println("(R) " + flReachabilityPolicyReader.getName() + " - " + NO_RESULT_MSG);
                    }

                    flReachabilityPolicyReader.setPolicyVerificationReader(verificationResultReader);

                    myPolicies.add(flReachabilityPolicyReader);

                } else {

                    FLTraversalPolicyReader flTraversalPolicyReader = new FLTraversalPolicyReader(policy.getPolicyNameId(),
                            flNffgReader,
                            policy.isIsPositive(),
                            fromNodeReaderTOFLNodeReader(flNffgReader.getNode(policy.getPolicySourceNodeNameIdRefer().toString())),
                            fromNodeReaderTOFLNodeReader(flNffgReader.getNode(policy.getPolicyDestinationNodeNameIdRefer().toString())));

                    FLVerificationResultReader verificationResultReader;

                    if ((policy.getVerificationTime() != null) &&
                            (policy.getVerificationMessage() != null) &&
                            (policy.isVerificationResult() != null)) {
                        verificationResultReader = new FLVerificationResultReader(flTraversalPolicyReader,
                                policy.isIsPositive(),
                                policy.getVerificationMessage(),
                                policy.getVerificationTime().toGregorianCalendar());
                        System.out.println("(T) " + flTraversalPolicyReader.getName() + " - Has been verified!");
                    } else {
                        verificationResultReader = new FLVerificationResultReader(flTraversalPolicyReader,
                                null,
                                null,
                                null);
                        System.out.println("(T) " + flTraversalPolicyReader.getName() + " - " + NO_RESULT_MSG);
                    }

                    flTraversalPolicyReader.setPolicyVerificationReader(verificationResultReader);

                    TraversalPolicyType p = (TraversalPolicyType) policy;

                    if (p.getTraversalRequestedNode().size() > 0) {
                        for (TraversalPolicyType.TraversalRequestedNode traversalRequestedNode : p.getTraversalRequestedNode()) {
                            flTraversalPolicyReader.getTraversedFuctionalTypes().add(FunctionalType.valueOf(traversalRequestedNode.getFunctionalType().value()));
                        }
                    }

                    myPolicies.add(flTraversalPolicyReader);

                }
            }

            this.FLNffg.add(flNffgReader);
        }
        System.out.println("---> " + myPolicies.size() + " policies were created.");
        System.out.println("---> Created " + this.FLNffg.size() + " Nffgs");

        if (DEBUG) {
            printAll(myPolicies, FLNffg);
        }
    }


    private FLNodeReader fromNodeReaderTOFLNodeReader(NodeReader node) {
        return new FLNodeReader(FunctionalType.valueOf(node.getFuncType().value()), node.getName());
    }

    @Override
    public Set<NffgReader> getNffgs() {
        return new LinkedHashSet(this.FLNffg);
    }

    @Override
    public NffgReader getNffg(String s) {
        for (NffgReader nffg : this.FLNffg) {
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

        for (PolicyReader policy : myPolicies) {
            if (policy.getNffg().getName().contains(s)) {
                policies.add(policy);
            }
        }

        return policies;
    }

    @Override
    public Set<PolicyReader> getPolicies(Calendar calendar) {
        Set<PolicyReader> policies = new HashSet<PolicyReader>();

        for (PolicyReader policy : myPolicies) {
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

    public void printAll(Set<PolicyReader> PolicySet, Set<NffgReader> NffgSet) {
        printNffgs(NffgSet);
        printPolicies(PolicySet);
    }

    private void printPolicies(Set<PolicyReader> set) {

		/* Print the header of the table */
        System.out.println("#");
        System.out.println("#Number of Policies: " + set.size());
        System.out.println("#");
        String header = new String("#List of policies:");
        printHeader(header);

        // For each policy print related data
        for (PolicyReader pr : set) {
            pr.toString();

            if (!(pr instanceof FLTraversalPolicyReader)) {
                System.out.println("tgh");
            }
            System.out.println("Policy name: " + pr.getName());
            System.out.println("Policy nffg name: " + pr.getNffg().getName());
            if (pr.isPositive())
                System.out.println("Policy is positive.");
            else
                System.out.println("Policy is negative.");
            printVerificationResult(pr.getResult());
            System.out.println("#");
        }
        System.out.println("#End of Policies");
        System.out.println("#");
    }

    private void printVerificationResult(VerificationResultReader result) {
        if (result == null) {
            System.out.println("No verification result for policy");
            return;
        }
        if (result.getVerificationResult())
            System.out.println("Policy result is true");
        else
            System.out.println("Policy result is false");
        System.out.println("Verification result message: " + result.getVerificationResultMsg());
        System.out.println("Verification time (in local time zone): " + dateFormat.format(result.getVerificationTime().getTime()));
    }

    private void printNffgs(Set<NffgReader> set) {

		/* Print the header of the table */
        System.out.println("#");
        System.out.println("#Number of Nffgs: " + set.size());
        System.out.println("#");
        String header = new String("#List of NFFgs:");
        printHeader(header);

        // For each NFFG print related data
        for (NffgReader nffg_r : set) {
            System.out.println();
            printHeader("Data for NFFG " + nffg_r.getName());
            System.out.println();
            // Print update time
            Calendar updateTime = nffg_r.getUpdateTime();
            printHeader("Update time: " + updateTime.getTime().toString());
            printHeader("Update time: " + dateFormat.format(updateTime.getTime()));

            // Print nodes
            Set<NodeReader> nodeSet = nffg_r.getNodes();
            printHeader("Number of Nodes: " + nodeSet.size(), '%');
            for (NodeReader nr : nodeSet) {
                System.out.println("Node " + nr.getName() + "\tType: " + nr.getFuncType().toString() + "\tNumber of links: " + nr.getLinks().size());
                Set<LinkReader> linkSet = nr.getLinks();
                System.out.println("List of Links for node " + nr.getName());
                printHeader("Link name \tsource \tdestination");
                for (LinkReader lr : linkSet)
                    System.out.println(lr.getName() + "\t" + lr.getSourceNode().getName() + "\t" + lr.getDestinationNode().getName());
                System.out.println(makeLine('%'));
            }
            System.out.println("#");
        }
        System.out.println("#End of Nodes");
        System.out.println("#");
    }

    private void printHeader(String header, char c) {
        System.out.println(header);
        System.out.println(makeLine(c));
    }

    private StringBuffer makeLine(char c) {
        StringBuffer line = new StringBuffer(132);

        for (int i = 0; i < 132; ++i) {
            line.append(c);
        }
        return line;
    }

    private void printHeader(String header) {
        printHeader(header, '-');
    }

}
