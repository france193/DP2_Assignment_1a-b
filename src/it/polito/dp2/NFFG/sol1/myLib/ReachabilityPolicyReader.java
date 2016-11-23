package it.polito.dp2.NFFG.sol1.myLib;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class ReachabilityPolicyReader extends PolicyReader implements it.polito.dp2.NFFG.ReachabilityPolicyReader {

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
     * @param policyVerificationReader
     * @param isPositive
     * @param nodeSource
     * @param destination
     */
    ReachabilityPolicyReader(String policy_name_id,
                             NffgReader nffg_refer,
                             VerificationResultReader policyVerificationReader,
                             boolean isPositive,
                             NodeReader nodeSource,
                             NodeReader destination) {

        super(policy_name_id, nffg_refer, policyVerificationReader, isPositive);
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
}
