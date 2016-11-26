package it.polito.dp2.NFFG.sol1.myLib;


import it.polito.dp2.NFFG.ReachabilityPolicyReader;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLReachabilityPolicyReader extends FLPolicyReader implements ReachabilityPolicyReader {

    /**
     * Class' attributes
     */
    private FLNodeReader nodeSource;
    private FLNodeReader nodeDestination;

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
                             FLNffgReader nffg_refer,
                             boolean isPositive,
                             FLNodeReader nodeSource,
                             FLNodeReader destination) {

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
    public FLNodeReader getSourceNode() {
        return this.nodeSource;
    }

    /**
     * Gives the destination node of this policy.
     *
     * @return
     */
    @Override
    public FLNodeReader getDestinationNode() {
        return this.nodeDestination;
    }

    @Override
    public void setPolicyVerificationReader(FLVerificationResultReader policyVerificationReader) {
        super.setPolicyVerificationReader(policyVerificationReader);
    }

    @Override
    public String toString() {
        return super.toString() + " \n" +
                "\t Source Node: " + nodeSource.getName() + " \n" +
                "\t Destination Node: " + nodeDestination.getName() + " \n";
    }
}
