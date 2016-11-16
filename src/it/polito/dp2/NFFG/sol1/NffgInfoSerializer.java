package it.polito.dp2.NFFG.sol1;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import it.polito.dp2.NFFG.*;
import it.polito.dp2.NFFG.sol1.jaxb.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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

/**
 * Created by FLDeviOS on 14/11/2016.
 * <p>
 * Aim of this application is to read information passed from java classes
 * and create a valid xml respecting constraints obtained from the previous
 * created xsd schema
 **/
public class NffgInfoSerializer {
    private NffgVerifier monitor;
    private DateFormat dateFormat;

    private static String xml_filename;

    /**
     * Default constructror
     *
     * @throws NffgVerifierException
     */
    public NffgInfoSerializer() throws NffgVerifierException {
        NffgVerifierFactory factory = NffgVerifierFactory.newInstance();
        monitor = factory.newNffgVerifier();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        NffgInfoSerializer myNFFG;

        //read desidered name for new xml file
        xml_filename = args[0].toString();

        try {
            myNFFG = new NffgInfoSerializer();
            myNFFG.printAll();
        } catch (NffgVerifierException e) {
            e.printStackTrace();
        }
    }

    public void printAll() {
        printNffgs();
        //printPolicies();
    }

    private void printNffgs() {
        // get the list of NFFGs as a set
        Set<NffgReader> nffgs_set = monitor.getNffgs();

        // for each NFFG
        for (NffgReader nffg : nffgs_set) {

            // name
            String nffg_name = nffg.getName();
            // get last updated time
            Calendar updateTime = nffg.getUpdateTime();
            // get node set
            Set<NodeReader> node_set = nffg.getNodes();

            // for each node
            for (NodeReader node : node_set) {
                // name
                String node_name = node.getName();

                Set<LinkReader> link_set = node.getLinks();

                for (LinkReader link : link_set) {
                    String link_name = link.getName();
                    String link_src_node = link.getSourceNode().getName();
                    String link_dest_node = link.getDestinationNode().getName();

                    //TODO
                }
            }
        }
    }
}
