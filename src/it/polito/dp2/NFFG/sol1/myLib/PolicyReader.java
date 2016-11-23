package it.polito.dp2.NFFG.sol1.myLib;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class PolicyReader extends NamedEntityReader implements it.polito.dp2.NFFG.PolicyReader {

    /**
     * Class' attributes
     */
    private NffgReader nffg_refer;
    private VerificationResultReader policyVerificationReader;
    private boolean isPositive;

    /**
     * Class' constructor
     *
     * @param policy_name_id
     * @param nffg_refer
     * @param policyVerificationReader
     * @param isPositive
     */
    PolicyReader(String policy_name_id, NffgReader nffg_refer, VerificationResultReader policyVerificationReader, boolean isPositive) {
        super(policy_name_id);
        this.nffg_refer = nffg_refer;
        this.policyVerificationReader = policyVerificationReader;
        this.isPositive = isPositive;
    }

    /**
     * Gives the name of the NF-FG on which this policy has to be verified (a string made of alphanumeric characters only, where the first character is alphabetic).
     *
     * @return
     */
    @Override
    public NffgReader getNffg() {
        return this.nffg_refer;
    }

    /**
     * Gives the result of the verification of this policy.
     *
     * @return
     */
    @Override
    public VerificationResultReader getResult() {
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
}
