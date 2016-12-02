package it.polito.dp2.NFFG.sol1;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

// import of ONLY NECESSARY resources of library classes and types
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.VerificationResultReader;

// import of ONLY NECESSARY resources of jaxb generated classes  and types
import it.polito.dp2.NFFG.sol1.jaxb.NetworkService;
import it.polito.dp2.NFFG.sol1.jaxb.ReachabilityPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb.TraversalPolicyType;

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

    /**
     * true to enable debug printing
     */
    private static final Boolean DEBUG = true;
    private static final Boolean VERBOSE = false;

    private static final String XSD_FOLDER = "xsd/";
    private static final String XSD_FILE = "nffgInfo.xsd";
    private static final String PACKAGE = "it.polito.dp2.NFFG.sol1.jaxb";

    // set of Nffgs and Policy of all Nffgs
    private HashMap<String, FLNffgReader> myNffgs;
    private HashMap<String, PolicyReader> allMyPolicies = new HashMap<>();

    private NetworkService myNetworkServices = null;

    private int link_num;
    private int policy_num;

    /**
     * Class' constructor that read an xml file and generate corresponding classes filled with xml data
     *
     * @throws SAXException
     * @throws JAXBException
     */
    public FLNffgVerifier() throws SAXException, JAXBException {

        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss z");

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

        if (VERBOSE) {
            System.out.println("******************** This NetworkService has " + myNetworkServices.getNffg().size() + " NFFGS ****************");
        }

        // add all of my nffgs on my HashMap of my NffgReader
        myNffgs = new HashMap<>();
        for (NetworkService.Nffg t_nffg : myNetworkServices.getNffg()) {

            // NEW NFFG
            FLNffgReader flNffgReader = new FLNffgReader(t_nffg.getNffgNameId().toString(),
                    t_nffg.getLastUpdatedTime().toGregorianCalendar());

            /** Nodes **/
            if (VERBOSE) {
                System.out.println("--------------------------- NFFG: " + t_nffg.getNffgNameId() + " ---------------------------");
                System.out.println("--(N)--> This Nffg has " + t_nffg.getNode().size() + " NODES");
                System.out.println("--(L)--> This Nffg has " + t_nffg.getLink().size() + " LINKS");
                System.out.println("--(P)--> This Nffg has " + t_nffg.getReachabilityPolicyTypeOrTraversalPolicyType().size() + " POLICIES");
            }
            for (NetworkService.Nffg.Node node : t_nffg.getNode()) {
                // create a new node
                FLNodeReader nodeReader = new FLNodeReader(FunctionalType.valueOf(node.getFunctionalType().toString()),
                        node.getNodeNameId());
                // add node to my nffg
                flNffgReader.addNode(nodeReader);
            }
            if (VERBOSE) {
                System.out.println("--(N)--> Created " + flNffgReader.getNodes().size() + " NODES");
            }

            /** Links **/
            link_num = 0;
            for (NetworkService.Nffg.Node node : t_nffg.getNode()) {
                for (NetworkService.Nffg.Link link : t_nffg.getLink()) {
                    if (node.getNodeNameId().toString().equals(link.getLinkSourceNodeNameIdRefer().toString())) {

                        FLNodeReader srcNode;

                        srcNode = flNffgReader.getNode(link.getLinkSourceNodeNameIdRefer().toString());

                        FLLinkReader linkReader = new FLLinkReader(link.getLinkNameId(),
                                srcNode,
                                flNffgReader.getNode(link.getLinkDestinationNodeNameIdRefer().toString()));

                        srcNode.addLink(linkReader);
                    }

                }
                link_num += flNffgReader.getNode(node.getNodeNameId()).getLinks().size();
            }

            /** Policies **/
            policy_num = 0;
            for (ReachabilityPolicyType policy : t_nffg.getReachabilityPolicyTypeOrTraversalPolicyType()) {
                if (flNffgReader.getName().contains(policy.getNffgNameIdRefer().toString())) {
                    if (!(policy instanceof TraversalPolicyType)) {

                        FLReachabilityPolicyReader flReachabilityPolicyReader = new FLReachabilityPolicyReader(policy.getPolicyNameId(),
                                flNffgReader,
                                policy.isIsPositive(),
                                flNffgReader.getNode(policy.getPolicySourceNodeNameIdRefer().toString()),
                                flNffgReader.getNode(policy.getPolicyDestinationNodeNameIdRefer().toString()));

                        FLVerificationResultReader policyVerificationReader;

                        if ((policy.getVerificationResult().isResult() != null) &&
                                (policy.getVerificationResult().getTime() != null) &&
                                (policy.getVerificationResult().getMessage() != null)) {

                            policyVerificationReader = new FLVerificationResultReader(flReachabilityPolicyReader,
                                    policy.getVerificationResult().isResult(),
                                    policy.getVerificationResult().getTime().toGregorianCalendar(),
                                    policy.getVerificationResult().getMessage());
                        } else {
                            policyVerificationReader = null;
                        }

                        flReachabilityPolicyReader.setVerificationResult(policyVerificationReader);

                        flNffgReader.addPolicy(flReachabilityPolicyReader);
                        policy_num++;
                    } else {

                        TraversalPolicyType p = (TraversalPolicyType) policy;

                        FLTraversalPolicyReader flTraversalPolicyReader = new FLTraversalPolicyReader(p.getPolicyNameId(),
                                flNffgReader,
                                p.isIsPositive(),
                                flNffgReader.getNode(p.getPolicySourceNodeNameIdRefer().toString()),
                                flNffgReader.getNode(p.getPolicyDestinationNodeNameIdRefer().toString()),
                                p.getTraversalRequestedNode());

                        FLVerificationResultReader policyVerificationReader;

                        if ((policy.getVerificationResult().isResult() != null) &&
                                (policy.getVerificationResult().getTime() != null) &&
                                (policy.getVerificationResult().getMessage() != null)) {

                            policyVerificationReader = new FLVerificationResultReader(flTraversalPolicyReader,
                                    policy.getVerificationResult().isResult(),
                                    policy.getVerificationResult().getTime().toGregorianCalendar(),
                                    policy.getVerificationResult().getMessage());
                        } else {
                            policyVerificationReader = null;
                        }

                        flTraversalPolicyReader.setVerificationResult(policyVerificationReader);

                        flNffgReader.addPolicy(flTraversalPolicyReader);
                        policy_num++;
                    }
                }
            }

            myNffgs.put(flNffgReader.getName(), flNffgReader);
            if (VERBOSE) {
                System.out.println("--(L)--> Created " + link_num + " LINKS");
                System.out.println("--(P)--> Created " + policy_num + " POLICIES");
            }
        }

        /* Retrieve all policies in a unique HashMap */
        for (Map.Entry<String, FLNffgReader> entry : myNffgs.entrySet()) {
            for ( PolicyReader policy : entry.getValue().getPolicies()) {
                allMyPolicies.put(policy.getName(), policy);
            }
        }

        if (VERBOSE) {
            System.out.println("---------------------------------------------------------------------------------");
            System.out.println("Now " + this.myNffgs.get("Nffg0").getName() + " has " + this.myNffgs.get("Nffg0").getPolicies().size() + " policies");
            System.out.println("Now " + this.myNffgs.get("Nffg1").getName() + " has " + this.myNffgs.get("Nffg1").getPolicies().size() + " policies");
            System.out.println("Now " + this.myNffgs.get("Nffg2").getName() + " has " + this.myNffgs.get("Nffg2").getPolicies().size() + " policies");
            System.out.println("Now " + this.myNffgs.get("Nffg3").getName() + " has " + this.myNffgs.get("Nffg3").getPolicies().size() + " policies");
            System.out.println("Now " + this.myNffgs.get("Nffg4").getName() + " has " + this.myNffgs.get("Nffg4").getPolicies().size() + " policies");
            System.out.println("Now " + this.myNffgs.get("Nffg5").getName() + " has " + this.myNffgs.get("Nffg5").getPolicies().size() + " policies");
            System.out.println("Now " + this.myNffgs.get("Nffg6").getName() + " has " + this.myNffgs.get("Nffg6").getPolicies().size() + " policies");
            System.out.println("Now " + this.myNffgs.get("Nffg7").getName() + " has " + this.myNffgs.get("Nffg7").getPolicies().size() + " policies");
            System.out.println("*************************** Created a total of " + this.myNffgs.size() + " NFFGS *****************************");
            System.out.println("*************************** Created a total of" + this.allMyPolicies.size() + " POLICIES *************************");
        }


        if (DEBUG) {
            printAll();
        }
    }

    /**
     * Gives access to the set of known NF-FGs.
     *
     * @return
     */
    @Override
    public Set<NffgReader> getNffgs() {
        return new LinkedHashSet(this.myNffgs.values());
    }

    /**
     * Gives access to a single NF-FG given its entityName.
     *
     * @param s
     * @return
     */
    @Override
    public NffgReader getNffg(String s) {
        return (s != null && this.myNffgs != null) ? (this.myNffgs.get(s)) : (null);
    }

    /**
     * Gives access to the set of known policies to be verified.
     *
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies() {
        return new LinkedHashSet(this.allMyPolicies.values());
    }

    /**
     * Gives access to the set of known policies to be verified, filtered by NF-FG's entityName.
     *
     * @param s
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies(String s) {
        Set<PolicyReader> policies = new HashSet<>();

        for (Map.Entry<String, PolicyReader> entry : allMyPolicies.entrySet()) {
            if (entry.getValue().getNffg().getName().contains(s)) {
                policies.add(entry.getValue());
            }
        }

        //TODO check
        return (policies.size() == 0) ? (null) : policies;
    }

    /**
     * Gives access to the set of known policies to be verified, filtered by verification verificationTime and date.
     *
     * @param calendar
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies(Calendar calendar) {
        Set<PolicyReader> policies = new HashSet<>();

        for (Map.Entry<String, PolicyReader> entry : allMyPolicies.entrySet()) {
            if (entry.getValue().getResult().getVerificationTime().after(calendar)) {
                policies.add(entry.getValue());
            }
        }

        return (policies.size() == 0) ? (null) : policies;
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

    /**
     * DEBUG PRINT INFORMATION
     */
    public void printAll() {
        printNffgs(getNffgs());
        printPolicies(getPolicies());
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
                System.out.println("Reachability");
            } else {
                System.out.println("Traversal");
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
            printHeader("Update time: " + dateFormat.format(updateTime.getTime()));

            // Print nodes
            Set<NodeReader> nodeSet = nffg_r.getNodes();
            printHeader("Number of Nodes: " + nodeSet.size(), '%');
            for (NodeReader nr : nodeSet) {
                System.out.println("Node " + nr.getName() + "\tType: " + nr.getFuncType().toString() + "\tNumber of links: " + nr.getLinks().size());
                Set<LinkReader> linkSet = nr.getLinks();
                System.out.println("List of Links for node: " + nr.getName());
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
