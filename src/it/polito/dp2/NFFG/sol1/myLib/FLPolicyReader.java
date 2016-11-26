package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.PolicyReader;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLPolicyReader extends FLNamedEntityReader implements PolicyReader {

    /**
     * Class' attributes
     */
    private FLNffgReader nffg_refer;
    private FLVerificationResultReader policyVerificationReader;
    private boolean isPositive;

    /**
     * Class' constructor
     *
     * @param policy_name_id
     * @param nffg_refer
     * @param isPositive
     */
    FLPolicyReader(String policy_name_id, FLNffgReader nffg_refer, boolean isPositive) {
        super(policy_name_id);
        this.nffg_refer = nffg_refer;
        this.isPositive = isPositive;
    }

    /**
     * Gives the name of the NF-FG on which this policy has to be verified (a string made of alphanumeric characters only, where the first character is alphabetic).
     *
     * @return
     */
    @Override
    public FLNffgReader getNffg() {
        return this.nffg_refer;
    }

    /**
     * Gives the result of the verification of this policy.
     *
     * @return
     */
    @Override
    public FLVerificationResultReader getResult() {
        return this.policyVerificationReader;
    }

    /**
     * Specifies whether this policy is positive or negative.
     *
     * @return
     */
    @Override
    public Boolean isPositive() {
        return this.isPositive;
    }

    public void setPolicyVerificationReader(FLVerificationResultReader policyVerificationReader) {
        this.policyVerificationReader = policyVerificationReader;
    }

    @Override
    public String toString() {
        return " -- PolicyReader: " + this.getName() + " --\n" +
                "\t Referring Nffg: " + nffg_refer + " \n" +
                "\t isPositive: " + isPositive  + " \n" +
                "\t policy Verification Result: " + policyVerificationReader.toString() + " \n";
    }
}
