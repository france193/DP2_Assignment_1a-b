package it.polito.dp2.NFFG.sol1;

import java.io.File;
import java.util.*;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

// import of ONLY NECESSARY resources of library classes and types
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.sol1.jaxb.FLNodeFunctionalType;
import it.polito.dp2.NFFG.sol1.jaxb.LinkType;
import it.polito.dp2.NFFG.sol1.jaxb.NffgType;
import it.polito.dp2.NFFG.sol1.jaxb.Nffgs;
import it.polito.dp2.NFFG.sol1.jaxb.NodeType;
import it.polito.dp2.NFFG.sol1.jaxb.PolicyType;
import it.polito.dp2.NFFG.sol1.jaxb.VerificationResultType;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * Created by Francesco Longo (223428) on 10/02/2017.
 */
public class FLNffgVerifier implements NffgVerifier {

    private static final String XSD_FOLDER = "xsd/";
    private static final String XSD_FILE = "nffgInfo.xsd";
    private static final String PACKAGE = "it.polito.dp2.NFFG.sol1.jaxb";

    // set of Nffgs and Policy of all Nffgs
    private HashMap<String, FLNffgReader> myNffgs = new HashMap<>();
    private HashMap<String, FLPolicyReader> myPolicies = new HashMap<>();

    private Nffgs nffgs = null;

    /**
     * Class' constructor that read an xml file and generate corresponding
     * classes filled with xml data
     *
     * @throws SAXException
     * @throws JAXBException
     */
    public FLNffgVerifier() throws NffgVerifierException {

        try {
            // take file name from the property
            String fileName = System.getProperty("it.polito.dp2.NFFG.sol1.NffgInfo.file");
            if (fileName == null) {
                throw new NffgVerifierException("Property not setted!");
            }
            nffgs = unmarshallDocument(new File(fileName));
        } catch (SAXException e) {
            e.printStackTrace();
            throw new NffgVerifierException("SAXException (!): " + e.getMessage());
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new NffgVerifierException("JAXBException (!): " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new NffgVerifierException("IllegalArgumentException (!): " + e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NffgVerifierException("NullPointerException (!): " + e.getMessage());
        }

        // NFFGS
        for (NffgType nffg : nffgs.getNffg()) {
            FLNffgReader flNffgReader = new FLNffgReader(nffg.getName(), nffg.getLastUpdatedTime().toGregorianCalendar());

            // NODES
            for (NodeType node : nffg.getNode()) {
                FLNodeReader nodeReader = new FLNodeReader(FunctionalType.valueOf(node.getFunctionalType().value()), node.getName());

                // LINKS
                for (LinkType link : node.getLink()) {
                    FLNodeReader dstNodeReader = findNodeFromName(link.getDestinationNode(), nffg);
                    if (dstNodeReader == null) {
                        throw new NffgVerifierException("dstNodeReader not found");
                    }
                    FLLinkReader linkReader = new FLLinkReader(link.getName(), nodeReader, dstNodeReader);

                    nodeReader.addLink(linkReader);
                }

                flNffgReader.addNode(nodeReader);
            }

            myNffgs.put(flNffgReader.getName(), flNffgReader);
        }

        // POLICIES
        for (PolicyType policy : nffgs.getPolicy()) {
            String policyName = policy.getName();
            FLNffgReader policyNffg = myNffgs.get(policy.getNffgName());
            Boolean policyPositive = policy.isIsPositive();

            NffgType nffgType = null;
            for (NffgType nffg : nffgs.getNffg()) {
                if (nffg.getName().equals(policy.getNffgName())) {
                    nffgType = nffg;
                }
            }

            if (nffgType == null) {
                throw new NffgVerifierException("nffgType not found");
            }

            FLNodeReader srcNode = findNodeFromName(policy.getSourceNode(), nffgType);
            if (srcNode == null) {
                throw new NffgVerifierException("srcNode not found");
            }
            FLNodeReader dstNode = findNodeFromName(policy.getDestinationNode(), nffgType);
            if (dstNode == null) {
                throw new NffgVerifierException("dstNode not found");
            }

            FLVerificationResultReader verificationResultReader;

            // is a Taversal Policy
            if (policy.getTraversedNode().size() > 0) {
                Set<FunctionalType> traversedFunctionalType = new LinkedHashSet<FunctionalType>();

                for (FLNodeFunctionalType functionalType : policy.getTraversedNode()) {
                    traversedFunctionalType.add(FunctionalType.valueOf(functionalType.value()));
                }

                FLTraversalPolicyReader traversalPolicyReader = new FLTraversalPolicyReader(policyName,
                        policyNffg,
                        policyPositive,
                        srcNode,
                        dstNode,
                        traversedFunctionalType);

                verificationResultReader = resultExist(traversalPolicyReader, nffgs.getVerificationResult());
                if ( verificationResultReader != null ) {
                    traversalPolicyReader.setVerificationResultReader(verificationResultReader);
                } else {
                    traversalPolicyReader.setVerificationResultReader(null);
                }

                myNffgs.get(policyNffg.getName()).addPolicy(traversalPolicyReader);
                myPolicies.put(traversalPolicyReader.getName(), traversalPolicyReader);
            } else {
                // is a Reachability Policy
                FLReachabilityPolicyReader reachabilityPolicyReader = new FLReachabilityPolicyReader(policyName,
                        policyNffg,
                        policyPositive,
                        srcNode,
                        dstNode);

                verificationResultReader = resultExist(reachabilityPolicyReader, nffgs.getVerificationResult());
                if ( verificationResultReader != null ) {
                    reachabilityPolicyReader.setVerificationResultReader(verificationResultReader);
                } else {
                    reachabilityPolicyReader.setVerificationResultReader(null);
                }

                myNffgs.get(policyNffg.getName()).addPolicy(reachabilityPolicyReader);
                myPolicies.put(reachabilityPolicyReader.getName(), reachabilityPolicyReader);
            }
        }
    }

    private FLVerificationResultReader resultExist(FLPolicyReader policy, List<VerificationResultType> verificationResult) {
        for (VerificationResultType verificationResultType : verificationResult) {
            if (verificationResultType.getPolicy().equals(policy.getName())) {

                FLVerificationResultReader flResultReader = new FLVerificationResultReader(policy,
                        verificationResultType.isResult(),
                        verificationResultType.getMessage(),
                        verificationResultType.getTime().toGregorianCalendar());

                return flResultReader;
            }
        }

        return null;
    }

    private FLNodeReader findNodeFromName(String node, NffgType nffg) {
        for (NodeType nodeType : nffg.getNode()) {
            if (nodeType.getName().equals(node)) {
                return new FLNodeReader(FunctionalType.valueOf(nodeType.getFunctionalType().value()), nodeType.getName());
            }
        }

        return null;
    }

    /**
     * Gives access to the set of known NF-FGs.
     *
     * @return
     */
    @Override
    public Set<NffgReader> getNffgs() {
        return new LinkedHashSet<NffgReader>(this.myNffgs.values());
    }

    /**
     * Gives access to a single NF-FG given its entityName.
     *
     * @param s
     * @return
     */
    @Override
    public NffgReader getNffg(String s) {

        NffgReader nffg = this.myNffgs.get(s);

        if (nffg != null) {
            return nffg;
        } else {
            return null;
        }
    }

    /**
     * Gives access to the set of known policies to be verified.
     *
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies() {
        return new LinkedHashSet<PolicyReader>(this.myPolicies.values());
    }

    /**
     * Gives access to the set of known policies to be verified, filtered by
     * NF-FG's entityName.
     *
     * @param s
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies(String s) {
        Set<PolicyReader> policies = new HashSet<>();

        for (Map.Entry<String, FLPolicyReader> entry : myPolicies.entrySet()) {
            if (entry.getValue().getNffg().getName().contains(s)) {
                policies.add(entry.getValue());
            }
        }

        return policies;
    }

    /**
     * Gives access to the set of known policies to be verified, filtered by
     * verification verificationTime and date.
     *
     * @param calendar
     * @return
     */
    @Override
    public Set<PolicyReader> getPolicies(Calendar calendar) {
        Set<PolicyReader> policies = new HashSet<>();

        for (Map.Entry<String, FLPolicyReader> entry : myPolicies.entrySet()) {
            if (entry.getValue().getResult().getVerificationTime().after(calendar)) {
                policies.add(entry.getValue());
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
    private Nffgs unmarshallDocument(File inputFile) throws JAXBException, SAXException, IllegalArgumentException {
        JAXBContext myJAXBContext = JAXBContext.newInstance(PACKAGE);

        SchemaFactory mySchemaFactory;
        Schema mySchema;

		/*
		 * - creating the XML schema to validate the XML file before read it -
		 */
        mySchemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        mySchema = mySchemaFactory.newSchema(new File(XSD_FOLDER + XSD_FILE));

        Unmarshaller myUnmarshaller = myJAXBContext.createUnmarshaller();
        myUnmarshaller.setSchema(mySchema);

        return (Nffgs) myUnmarshaller.unmarshal(inputFile);
    }

}
