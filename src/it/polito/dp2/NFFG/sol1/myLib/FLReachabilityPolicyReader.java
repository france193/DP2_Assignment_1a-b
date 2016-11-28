package it.polito.dp2.NFFG.sol1.myLib;


import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLReachabilityPolicyReader extends FLPolicyReader implements ReachabilityPolicyReader {

    /**
     * Class' attributes
     */
    private NodeReader nodeSource;
    private NodeReader nodeDestination;

    /**
     * Class' constructor
     *
     * @param policy_name_id
     * @param nffg_refer
     * @param isPositive
     * @param nodeSource
     * @param destination
     */
    FLReachabilityPolicyReader(String policy_name_id,
                             NffgReader nffg_refer,
                             boolean isPositive,
                             NodeReader nodeSource,
                             NodeReader destination) {

        super(policy_name_id, nffg_refer, isPositive);
        this.nodeSource = nodeSource;
        this.nodeDestination = destination;
    }

    /**
     * Gives the nodeSourceNode node of this policy.
     *
     * @return
     */
    @Override
    public NodeReader getSourceNode() {
        return this.nodeSource;
    }

    /**
     * Gives the destination node of this policy.
     *
     * @return
     */
    @Override
    public NodeReader getDestinationNode() {
        return this.nodeDestination;
    }

    @Override
    public void setPolicyVerificationReader(VerificationResultReader policyVerificationReader) {
        super.setPolicyVerificationReader(policyVerificationReader);
    }

}
