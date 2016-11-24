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

// import of ONLY NECESSARY resources of jaxb generated classes  and types
import it.polito.dp2.NFFG.sol1.jaxb_generated.NetworkService;
import it.polito.dp2.NFFG.sol1.jaxb_generated.ReachabilityPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb_generated.TraversalPolicyType;
import it.polito.dp2.NFFG.sol1.jaxb_generated.NodeFunctionalType;

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

/***************************************************************
 *                   My XSD schema structure                   *
 ***************************************************************
 *
 *    NETWORK_SERVICE
 *           |
 *           |-- NFFG(*) (nffg_name_id,
 *                |       last_update_time)
 *                |
 *                |-- NODE(*) (node_name_id,
 *                |            functional_type)
 *                |
 *                |-- LINK(*) (link_name_id,
 *                |            link_source_node_name_id_refer,
 *                |            link_destination_node_name_id_refer)
 *                |
 *                |-- REACHABILITY_POLICY(*) (policy_name_id,
 *                              |             nffg_name_id_refer,
 *                              |             isPositive,
 *                              |             verificationResult,
 *                              |             verificationTime,
 *                              |             verificationMessage,
 *                              |             policy_source_node_name_id_refer,
 *                              |             policy_destination_node_name_id_refer)
 *                              |
 *                              |
 *                              |---> TRAVERSAL_POLICY(*)
 *                                             |
 *                                             |-- TRAVERSAL_REQUESTED_NODE(*) (functional_type)
 **/

public class NffgInfoSerializer {
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
        NffgInfoSerializer myInfoserializer;

        //read desidered name for new xml file
        xml_filename = args[0].toString();

        try {
            myInfoserializer = new NffgInfoSerializer();
            myInfoserializer.readAll();

            //myInfoserializer.printXMLOnConsole();
            myInfoserializer.writeXMLToFile();
        } catch (NffgVerifierException e) {
            e.printStackTrace();
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
                myPolicy.setVerificationTime(null);
                myPolicy.setIsPositive(policy.isPositive().booleanValue());

                readVerificationResult(myPolicy, policy);

                myPolicy.setPolicySourceNodeNameIdRefer(policy.getSourceNode().getName());
                myPolicy.setPolicyDestinationNodeNameIdRefer(policy.getDestinationNode().getName());

                myNffg.getReachabilityPolicyTypeOrTraversalPolicyType().add(myPolicy);

                // read Reachability Policy
            } else {
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
        if (policy.getResult() != null) {
            myPolicy.setVerificationResult(policy.getResult().getVerificationResult());
            myPolicy.setVerificationTime(getXMLCal(policy.getResult().getVerificationTime()));
            myPolicy.setVerificationMessage(policy.getResult().getVerificationResultMsg());
        } else {
            myPolicy.setVerificationMessage("No verification result for policy");
        }
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

    private void printXMLOnConsole() {
        try {
            //Write it
            JAXBContext ctx = JAXBContext.newInstance(NetworkService.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();
            m.marshal(myNetworkService, sw);
            sw.close();

            System.out.println(sw.toString());
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeXMLToFile() {
        try {
            // create jaxb context
            JAXBContext jaxbContext = JAXBContext.newInstance(NetworkService.class);
            Marshaller myMarchaller = jaxbContext.createMarshaller();
            myMarchaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // set validation with my xsd
            SchemaFactory mySchemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema mySchema = mySchemaFactory.newSchema(new File("xsd/nffgInfo.xsd"));
            myMarchaller.setSchema(mySchema);

            String extension;
            if (xml_filename.contains(".xml")) {
                extension = "";
            } else {
                extension = ".xml";
            }

            OutputStream fileOutputStream = new FileOutputStream("xsd/" + xml_filename + extension);
            myMarchaller.marshal(myNetworkService, fileOutputStream);
            fileOutputStream.close();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
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