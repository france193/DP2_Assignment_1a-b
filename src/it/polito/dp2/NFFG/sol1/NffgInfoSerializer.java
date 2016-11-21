package it.polito.dp2.NFFG.sol1;

/**
 * Created by Francesco Longo on 14/11/2016.
 * <p>
 * Aim of this application is to read information passed from a NFFG random
 * generator and fill java classes representing my xsd schema.
 * Then Marshalling this document, it is possible to obtain a valid xml
 * respecting constraints obtained from the previous created xsd schema
 **/

//library path

import it.polito.dp2.NFFG.*;

//generated path
import it.polito.dp2.NFFG.sol1.jaxb_generated.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.GregorianCalendar;
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
 *                |-- POLICY(*) (policy_name_id,
 *                      |        nffg_name_id_refer,
 *                      |        policy_kind,
 *                      |        verification_result,
 *                      |        policy_source_node_name_id_refer,
 *                      |        policy_destination_node_name_id_refer,
 *                      |        last_update_time)
 *                      |
 *                      |-- TRAVERSAL_REQUESTED_NODE(?) (functional_type)
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

            myInfoserializer.printXMLOnConsole();
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
            try {
                GregorianCalendar calendar = (GregorianCalendar) nffg.getUpdateTime();
                XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                myNffg.setLastUpdatedTime(xmlCal);
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }

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

        for (PolicyReader policy : policy_set) {
            //policy.
        }
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

            OutputStream fileOutputStream = new FileOutputStream(xml_filename);
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
}
