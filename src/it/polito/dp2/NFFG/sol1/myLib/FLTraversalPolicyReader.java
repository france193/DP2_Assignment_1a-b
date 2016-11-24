package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.FunctionalType;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLTraversalPolicyReader extends FLReachabilityPolicyReader implements it.polito.dp2.NFFG.TraversalPolicyReader {

    Set<FunctionalType> listOfRequiredTraversedNode = new LinkedHashSet();

    /**
     * Class' constructor
     *
     * @param policy_name_id
     * @param nffg_refer
     * @param policyVerificationReader
     * @param isPositive
     * @param nodeSource
     * @param destination
     * @param listOfRequiredTraversedNode
     */
    FLTraversalPolicyReader(String policy_name_id,
                          FLNffgReader nffg_refer,
                          FLVerificationResultReader policyVerificationReader,
                          boolean isPositive,
                          FLNodeReader nodeSource,
                          FLNodeReader destination,
                          Set<FunctionalType> listOfRequiredTraversedNode) {

        super(policy_name_id, nffg_refer, policyVerificationReader, isPositive, nodeSource, destination);

        if (listOfRequiredTraversedNode != null) {
            this.listOfRequiredTraversedNode.addAll(listOfRequiredTraversedNode);
        }
    }

    /**
     * Gives the set of network functionalities that must be traversed for the property of this policy to hold.
     *
     * @return
     */
    @Override
    public Set<FunctionalType> getTraversedFuctionalTypes() {
        return new LinkedHashSet(this.listOfRequiredTraversedNode);
    }
}
