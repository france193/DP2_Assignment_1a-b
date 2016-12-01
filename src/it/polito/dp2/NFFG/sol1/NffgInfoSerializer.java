package it.polito.dp2.NFFG.sol1;

/**
 * Created by Francesco Longo on 14/11/2016.
 * <p>
 * Aim of this application is to read information passed from a NFFG random
 * generator and fill java classes representing my xsd schema.
 * Then Marshalling this document, it is possible to obtain a valid xml
 * respecting constraints obtained from the previous created xsd schema
 **/

// import of ONLY NECESSARY resources of library classes and types
import it.polito.dp2.NFFG.NffgVerifierFactory;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.TraversalPolicyReader;
import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.FactoryConfigurationError;

// import of ONLY NECESSARY resources of jaxb generated classes  and types
import it.polito.dp2.NFFG.sol1.jaxb_gen.NetworkService;
import it.polito.dp2.NFFG.sol1.jaxb_gen.ReachabilityPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb_gen.TraversalPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb_gen.NodeFunctionalType;

// other import
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

/************************************************************************
 * ant -Doutput=file1.xml -Dseed=100000 -Dtestcase=1 NffgInfoSerializer *
 ************************************************************************
 *
 * -Doutput
 * give name to assign to the desidered xml file
 *
 * -Dseed
 * use seed with a value of 100000 for pseudo-random generator
 *
 * -Dtestcase
 * create only one nffg with no policies
 *
 * execute the target NffgInfoSerializer of the build.xml ant file
 *
 * test with policies:
 * ant -Doutput=file1.xml -Dseed=100000 NffgInfoSerializer
 **/

/******************************************************
 * ant -Dseed=100000 -Dtestcase=1 NFFGInfo > out1.txt *
 ******************************************************
 *
 * -Dseed
 * use seed with a value of 100000 for pseudo-random generator
 *
 * -Dtestcase
 * create only one nffg with no policies
 *
 * execute the target NFFGInfo of the build.xml ant file
 *
 * test with policies:
 * ant -Dseed=100000 NFFGInfo > out1.txt
 **/

public class NffgInfoSerializer {

    public static final String XSD_FOLDER = "xsd/";
    public static final String XSD_FILE = "nffgInfo.xsd";
    public static final String XSD_LOCATION = "https://france193.wordpress.com";
    public static final String PACKAGE = "it.polito.dp2.NFFG.sol1.jaxb_gen";

    private NffgVerifier monitor;
    private NetworkService myNetworkService;

    private static String xml_filename;

    /**
     * Default constructror
     *
     * @throws NffgVerifierException
     */
    public NffgInfoSerializer() throws NffgVerifierException {
        NffgVerifierFactory factory = NffgVerifierFactory.newInstance();
        monitor = factory.newNffgVerifier();
        // create a new NetworkServices root element
        myNetworkService = new NetworkService();
    }


    /**
     * @param args
     */
    public static void main(String[] args) {

        // check parameter
        if (args.length != 1) {
            System.err.println("(!) - Error! Usage: <program_name> <output.xml>");
            System.err.println("args.length is equal to " + args.length);
            return;
        }

        System.out.println(" > This program will serialize your Nffg into an XML file!");

        //read desidered name for new xml file
        xml_filename = args[0].toString();

        try {
            NffgInfoSerializer myInfoserializer = new NffgInfoSerializer();
            myInfoserializer.readAll();
            System.out.println(" > The data structure was created!\n");
            myInfoserializer.writeXMLToFile();
            System.out.println(" > File xml correctly generated!\n");
        } catch (FactoryConfigurationError e) {
            System.err.println("(!) - Could not create a DocumentBuilderFactory: " + e.getMessage());
            e.printStackTrace();
            System.exit(11);
        } catch (NffgVerifierException e) {
            System.err.println("(!) - Could not instantiate the class: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (JAXBException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(2);
        } catch (IllegalArgumentException e) {
            System.err.println("(!) - Error, some argument are wrong!");
            e.printStackTrace();
            System.exit(3);
        } catch (FileNotFoundException e) {
            System.err.println("(!) - Error! The file: " + args[0] + " does not exists!");
            e.printStackTrace();
            System.exit(4);
        } catch (SAXException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(5);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(6);
        }
    }

    public void readAll() {
        readNffgs();
        readPolicy();
    }

    private void readNffgs() {
        // get the list of NFFGs as a set
        Set<NffgReader> nffgs_set = monitor.getNffgs();

        // for each Nffg read
        for (NffgReader nffg : nffgs_set) {

            /** NFFG **/
            /* create myNffg */
            NetworkService.Nffg myNffg = new NetworkService.Nffg();
            /* setting parameter on myNffg */
            myNffg.setNffgNameId(nffg.getName());
            myNffg.setLastUpdatedTime(getXMLCal(nffg.getUpdateTime()));

            /** NODES **/
            /* take nodes of myNffg */
            Set<NodeReader> node_set = nffg.getNodes();

            /* for each node */
            for (NodeReader node : node_set) {

                /* create myNode */
                NetworkService.Nffg.Node myNode = new NetworkService.Nffg.Node();
                /* setting parameter of myNode */
                myNode.setNodeNameId(node.getName());
                myNode.setFunctionalType(NodeFunctionalType.valueOf(node.getFuncType().value()));

                /* add myNode to my myNffg */
                myNffg.getNode().add(myNode);

                /** LINKS **/
                /* take links of every node and save it to my nffg */
                Set<LinkReader> link_set = node.getLinks();

                for (LinkReader link : link_set) {
                    /* create myNode */
                    NetworkService.Nffg.Link myLink = new NetworkService.Nffg.Link();

                    /* setting parameter of myLink */
                    myLink.setLinkNameId(link.getName());
                    myLink.setLinkSourceNodeNameIdRefer(link.getSourceNode().getName());
                    myLink.setLinkDestinationNodeNameIdRefer(link.getDestinationNode().getName());

                    myNffg.getLink().add(myLink);
                }
            }

            /* add myNffg to my NetworkService */
            myNetworkService.getNffg().add(myNffg);
        }
    }

    private void readPolicy() {
        // get the list of NFFGs as a set
        Set<PolicyReader> policy_set = monitor.getPolicies();

        for (PolicyReader general_policy : policy_set) {

            // find the Nffg of the policy
            NetworkService.Nffg myNffg = findNffg(general_policy.getNffg().getName());

            // read Reachability Policy
            if (!(general_policy instanceof TraversalPolicyReader)) {
                ReachabilityPolicyReader policy = (ReachabilityPolicyReader) general_policy;
                ReachabilityPolicyType myPolicy = new ReachabilityPolicyType();

                myPolicy.setPolicyNameId(policy.getName());
                myPolicy.setNffgNameIdRefer(policy.getNffg().getName());
                myPolicy.setIsPositive(policy.isPositive().booleanValue());

                readVerificationResult(myPolicy, policy);

                myPolicy.setPolicySourceNodeNameIdRefer(policy.getSourceNode().getName());
                myPolicy.setPolicyDestinationNodeNameIdRefer(policy.getDestinationNode().getName());

                myNffg.getReachabilityPolicyTypeOrTraversalPolicyType().add(myPolicy);

            } else {
                // read Traversal Policy
                TraversalPolicyReader policy = (TraversalPolicyReader) general_policy;
                TraversalPolicyType myPolicy = new TraversalPolicyType();

                myPolicy.setPolicyNameId(policy.getName());
                myPolicy.setNffgNameIdRefer(policy.getNffg().getName());
                myPolicy.setIsPositive(policy.isPositive());

                readVerificationResult(myPolicy, policy);

                myPolicy.setPolicySourceNodeNameIdRefer(policy.getSourceNode().getName());
                myPolicy.setPolicyDestinationNodeNameIdRefer(policy.getDestinationNode().getName());
                if (policy.getTraversedFuctionalTypes().size() > 0) {
                    for (FunctionalType myFunctionalType : policy.getTraversedFuctionalTypes()) {
                        TraversalPolicyType.TraversalRequestedNode myRequestedNode = new TraversalPolicyType.TraversalRequestedNode();
                        myRequestedNode.setFunctionalType(NodeFunctionalType.valueOf(myFunctionalType.value()));
                        myPolicy.getTraversalRequestedNode().add(myRequestedNode);
                    }
                }

                myNffg.getReachabilityPolicyTypeOrTraversalPolicyType().add(myPolicy);
            }
        }

    }

    private void readVerificationResult(ReachabilityPolicyType myPolicy, PolicyReader policy) {
        //create a new verification result element
        ReachabilityPolicyType.VerificationResult  verificationResult = new ReachabilityPolicyType.VerificationResult();

        if (policy.getResult() == null) {
            verificationResult.setPolicyNameIdRefer(policy.getName());
            verificationResult.setResult(null);
            verificationResult.setTime(null);
            verificationResult.setMessage(null);
        } else {
            verificationResult.setPolicyNameIdRefer(policy.getName());
            verificationResult.setResult(policy.getResult().getVerificationResult());
            verificationResult.setTime(getXMLCal(policy.getResult().getVerificationTime()));
            verificationResult.setMessage(policy.getResult().getVerificationResultMsg());
        }

        myPolicy.setVerificationResult(verificationResult);
    }

    private NetworkService.Nffg findNffg(String name) {

        // get the list of NFFGs as a set
        List<NetworkService.Nffg> nffg_list = myNetworkService.getNffg();

        for (NetworkService.Nffg nffg : nffg_list) {
            if (nffg.getNffgNameId().contains(name)) {
                return nffg;
            }
        }

        return null;
    }

    private void writeXMLToFile() throws SAXException, JAXBException, IOException {
        try {
            // create jaxb context
            JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);

            // set validation with my xsd
            SchemaFactory mySchemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema mySchema = mySchemaFactory.newSchema(new File(XSD_FOLDER + XSD_FILE));

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
            myMarchaller.marshal(myNetworkService, fileOutputStream);

            fileOutputStream.close();
        } catch (JAXBException e) {
            throw new JAXBException("(!) - Error creating the new instance of the JAXBContent");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("(!) - Error in the IO");
        } catch (SAXException e) {
            throw new SAXException("(!) - Error creating the XML Schema object");
        } catch (NullPointerException e) {
            throw new NullPointerException("(!) - Error! The instance of the schema or the file of the schema is not well created!");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("(!) - Error! No implementation of the schema language is available");
        }
    }

    private XMLGregorianCalendar getXMLCal(Calendar calendar) {
        GregorianCalendar cal = (GregorianCalendar) calendar;
        XMLGregorianCalendar xmlCal = null;

        try {
            xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return xmlCal;
    }
}