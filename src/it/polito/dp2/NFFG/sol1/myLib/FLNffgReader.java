package it.polito.dp2.NFFG.sol1.myLib;

import java.util.*;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLNffgReader extends FLNamedEntityReader implements it.polito.dp2.NFFG.NffgReader {

    private HashMap<String, FLNodeReader> nffgNodes;
    private HashMap<String, FLLinkReader> nffgLinks;
    private GregorianCalendar last_updated_time;

    /**
     * Class' construcotr
     *
     * @param entityName
     */
    FLNffgReader(String entityName, GregorianCalendar last_updated_time) {
        super(entityName);
        this.last_updated_time = last_updated_time;
    }

    /*********************
     * LAST_UPDATED_TIME *
     *********************/

    /**
     * Gives the date and verificationTime of the last update of the NF-FG.
     *
     * @return
     */
    @Override
    public Calendar getUpdateTime() {
        return (GregorianCalendar) this.last_updated_time;
    }

    /**
     * Set the date and verificationTime of the last update of the NF-FG.
     *
     * @param last_updated_time
     */
    public void setUpdateTime(GregorianCalendar last_updated_time) {
        this.last_updated_time = last_updated_time;
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
     * Add Node with the name node_name_id as a ModeReader in the list of Node of the Nffg
     *
     * @param nffgNodes
     * @param node_name_id
     * @param node
     */
    public void setNode(HashMap<String, FLNodeReader> nffgNodes, String node_name_id, FLNodeReader node) {
        nffgNodes.put(node_name_id, node);
    }

    /**
     * Return all the Node of the Nffg considered as a List of NodeReader
     *
     * @return
     */
    @Override
    public Set<it.polito.dp2.NFFG.NodeReader> getNodes() {
        return new LinkedHashSet(this.nffgNodes.values());
    }

    /**
     * Add a set of Nodes as a set of NodeReaders in the list of Node of the Nffg
     *
     * @param nffgNodes
     */
    public void setNffgNodes(HashMap<String, FLNodeReader> nffgNodes) {
        this.nffgNodes = nffgNodes;
    }

    /*********
     * LINKS *
     *********/

    /**
     * Return a requested Link with the name link_name_id as a LinkReader
     *
     * @param link_name_id
     * @return
     */
    public FLLinkReader getLink(String link_name_id) {
        return link_name_id != null && this.nffgLinks != null ? this.nffgLinks.get(link_name_id) : null;
    }

    /**
     * Add Link with the name link_name_id as a LinkReader in the list of Link of the Nffg
     *
     * @param nffgLinks
     * @param link_name_id
     * @param link
     */
    public void setLink(HashMap<String, FLLinkReader> nffgLinks, String link_name_id, FLLinkReader link) {
        nffgLinks.put(link_name_id, link);
    }

    /**
     * Return all the Link of the Nffg considered as a List of LinkReader
     *
     * @return
     */
    public Set<it.polito.dp2.NFFG.LinkReader> getNffgLinks() {
        return new LinkedHashSet(this.nffgLinks.values());
    }

    /**
     * Add a set of Links as a set of LinkReaders in the list of Link of the Nffg
     *
     * @param nffgLinks
     */
    public void setNffgLinks(HashMap<String, FLLinkReader> nffgLinks) {
        this.nffgLinks = nffgLinks;
    }

}

//TODO check "it.polito.dp2.NFFG.LinkReader"
//TODO check "it.polito.dp2.NFFG.NodeReader"
