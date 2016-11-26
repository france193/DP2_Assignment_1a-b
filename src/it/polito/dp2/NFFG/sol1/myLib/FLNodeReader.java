package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLNodeReader extends FLNamedEntityReader implements NodeReader {

    /**
     * Class' attributes
     */
    private FunctionalType myFunctionalType;
    private HashMap<String, FLLinkReader> links = new HashMap();

    /**
     * Class' constructor
     *
     * @param type
     * @param node_name_id
     */
    FLNodeReader(FunctionalType type, String node_name_id) {
        super(node_name_id);
        this.myFunctionalType = type;
        this.links = new HashMap<String, FLLinkReader>();
    }

    /**
     * Gives the functional type of the node.
     *
     * @return
     */
    @Override
    public FunctionalType getFuncType() {
        return this.myFunctionalType;
    }

    /**
     * Gives the known links that connect this node (i.e. have this node as the source node) in the network topology.
     *
     * @return
     */
    @Override
    public Set<LinkReader> getLinks() {
        return new LinkedHashSet(this.links.values());
    }

    @Override
    public String toString() {
        return "\t Node Name: " + this.getName() + " --\n" +
                "\t FunctionalType: " + myFunctionalType + " \n" +
                "\t -- links -- \n" + links.toString();
    }
}
