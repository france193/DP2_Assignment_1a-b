package it.polito.dp2.NFFG.sol1.myLib;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class LinkReader extends NamedEntityReader implements it.polito.dp2.NFFG.LinkReader {

    /**
     * Class' attributes
     */
    private NodeReader sourceNode;
    private NodeReader destinationNode;

    /**
     * Class' constructor
     *
     * @param link_name_id
     * @param sourceNode
     * @param destinationNode
     */
    LinkReader(String link_name_id, NodeReader sourceNode, NodeReader destinationNode) {
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
    public NodeReader getSourceNode() {
        return this.sourceNode;
    }

    /**
     * Gives access to the information associated to the sourceNode node of this link.
     *
     * @return
     */
    @Override
    public NodeReader getDestinationNode() {
        return this.destinationNode;
    }
}
