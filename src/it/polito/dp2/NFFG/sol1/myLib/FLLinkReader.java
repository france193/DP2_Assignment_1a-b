package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.LinkReader;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLLinkReader extends FLNamedEntityReader implements LinkReader {

    /**
     * Class' attributes
     */
    private FLNodeReader sourceNode;
    private FLNodeReader destinationNode;

    /**
     * Class' constructor
     *
     * @param link_name_id
     * @param sourceNode
     * @param destinationNode
     */
    FLLinkReader(String link_name_id, FLNodeReader sourceNode, FLNodeReader destinationNode) {
        super(link_name_id);
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    /**
     * Gives access to the information associated to the destination node of this link.
     *
     * @return
     */
    @Override
    public FLNodeReader getSourceNode() {
        return this.sourceNode;
    }

    /**
     * Gives access to the information associated to the sourceNode node of this link.
     *
     * @return
     */
    @Override
    public FLNodeReader getDestinationNode() {
        return this.destinationNode;
    }

    @Override
    public String toString() {
        return "\t\t Source Node: " + sourceNode.getName() + " \n" +
                "\t\t Destination Node: " + destinationNode.getName() + " \n";
    }
}
