package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.FunctionalType;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class NodeReader extends NamedEntityReader implements it.polito.dp2.NFFG.NodeReader {

    /**
     * Class' attributes
     */
    private FunctionalType myFunctionalType;
    private HashMap<String, LinkReader> links = new HashMap();

    /**
     * Class' constructor
     *
     * @param type
     * @param node_name_id
     */
    NodeReader(FunctionalType type, String node_name_id) {
        super(node_name_id);
        this.myFunctionalType = type;
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
    public Set<it.polito.dp2.NFFG.LinkReader> getLinks() {
        return new LinkedHashSet(this.links.values());
    }

    /**
     * Add a link to the set of link of the node
     *
     * @param link
     */
    public void addLink(LinkReader link) {
        if (link != null) {
            this.links.put(link.getName(), link);
        }
    }
}

//TODO check "it.polito.dp2.NFFG.LinkReader"
