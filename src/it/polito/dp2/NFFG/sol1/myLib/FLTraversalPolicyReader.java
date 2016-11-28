package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.TraversalPolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLTraversalPolicyReader extends FLReachabilityPolicyReader implements TraversalPolicyReader {

    private Set<FunctionalType> listOfRequiredTraversedNode;

    /**
     * Class' constructor
     *
     * @param policy_name_id
     * @param nffg_refer
     * @param isPositive
     * @param nodeSource
     * @param destination
     */
    FLTraversalPolicyReader(String policy_name_id,
                          NffgReader nffg_refer,
                          boolean isPositive,
                          FLNodeReader nodeSource,
                          FLNodeReader destination) {

        super(policy_name_id, nffg_refer, isPositive, nodeSource, destination);

        listOfRequiredTraversedNode = new HashSet<FunctionalType>();
    }

    /**
     * Gives the set of network functionalities that must be traversed for the property of this policy to hold.
     *
     * @return
     */
    @Override
    public Set<FunctionalType> getTraversedFuctionalTypes() {
        return this.listOfRequiredTraversedNode;
    }

    @Override
    public void setPolicyVerificationReader(VerificationResultReader policyVerificationReader) {
        super.setPolicyVerificationReader(policyVerificationReader);
    }

}
