package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLNodeReader extends FLNamedEntityReader implements NodeReader {

    /**
     * Class' attributes
     */
    private FunctionalType myFunctionalType;
    private Set<LinkReader> Links;

    /**
     * Class' constructor
     *
     * @param type
     * @param node_name_id
     */
    FLNodeReader(FunctionalType type, String node_name_id) {
        super(node_name_id);
        this.myFunctionalType = type;

        Links = new HashSet<LinkReader>();
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
        return this.Links;
    }

}
