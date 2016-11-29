package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.TraversalPolicyReader;
import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;

import it.polito.dp2.NFFG.sol1.jaxb_gen.TraversalPolicyType;

import java.util.LinkedHashSet;
import java.util.List;
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
     * @param nodeDestination
     * @param listOfRequiredTraversedNode
     */
    FLTraversalPolicyReader(String policy_name_id,
                            NffgReader nffg_refer,
                            boolean isPositive,
                            NodeReader nodeSource,
                            NodeReader nodeDestination,
                            List<TraversalPolicyType.TraversalRequestedNode> listOfRequiredTraversedNode) {

        super(policy_name_id, nffg_refer, isPositive, nodeSource, nodeDestination);

        this.listOfRequiredTraversedNode = new LinkedHashSet();

        if (listOfRequiredTraversedNode != null) {
            for (TraversalPolicyType.TraversalRequestedNode t : listOfRequiredTraversedNode) {
                this.listOfRequiredTraversedNode.add(FunctionalType.valueOf(t.getFunctionalType().value().toString()));
            }
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

    /**
     * Method that assign a VerificationResult element to the policy (if it is verified)
     *
     * @param verificationResult
     */
    public void setVerificationResult(FLVerificationResultReader verificationResult) {
        super.setVerificationResult(verificationResult);
    }
}
