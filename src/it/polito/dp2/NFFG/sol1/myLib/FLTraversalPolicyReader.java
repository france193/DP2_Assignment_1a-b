package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.TraversalPolicyReader;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
                          FLNffgReader nffg_refer,
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
    public void setPolicyVerificationReader(FLVerificationResultReader policyVerificationReader) {
        super.setPolicyVerificationReader(policyVerificationReader);
    }

    @Override
    public String toString() {
        StringBuffer tostring = new StringBuffer();

        tostring.append( super.toString() + " \n" +
                "\t List of Required Traversed Nodes: ");

        for (FunctionalType t : this.getTraversedFuctionalTypes()) {
            tostring.append(t.value().toString());
        }

        return tostring.toString();
    }
}
