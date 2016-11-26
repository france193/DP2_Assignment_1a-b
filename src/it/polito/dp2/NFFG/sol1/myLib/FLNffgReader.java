package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;

import java.util.*;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLNffgReader extends FLNamedEntityReader implements NffgReader {

    private HashMap<String, FLNodeReader> nffgNodes;
    private Calendar last_updated_time;

    /**
     * Class' constructor
     *
     * @param nffg_name_id
     * @param last_updated_time
     */
    public FLNffgReader(String nffg_name_id, Calendar last_updated_time) {
        super(nffg_name_id);
        this.last_updated_time = last_updated_time;

        nffgNodes = new HashMap<String, FLNodeReader>();
    }

    /**
     * Gives the date and verificationTime of the last update of the NF-FG.
     *
     * @return
     */
    @Override
    public Calendar getUpdateTime() {
        return this.last_updated_time;
    }

    /*********
     * NODES *
     *********/

    /**
     * Return a requested Node with the name node_name_id as a NodeReader
     *
     * @param node_name_id
     * @return
     */
    @Override
    public FLNodeReader getNode(String node_name_id) {
        return node_name_id != null && this.nffgNodes != null ? this.nffgNodes.get(node_name_id) : null;
    }

    /**
     * Return all the Node of the Nffg considered as a List of NodeReader
     *
     * @return
     */
    @Override
    public Set<NodeReader> getNodes() {
        return new LinkedHashSet(this.nffgNodes.values());
    }

    @Override
    public String toString() {
        return " *** Nffg *** \n" +
                "Nffg: " + this.getName() + " \n" +
                "Last Updated Time: " + last_updated_time.toString() + " \n" +
                "\t -- Nodes -- \n" + nffgNodes.toString() + "\n";
    }
}
