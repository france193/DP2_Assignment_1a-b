package it.polito.dp2.NFFG.sol1.myLib;

import it.polito.dp2.NFFG.VerificationResultReader;

import java.util.Calendar;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLVerificationResultReader implements VerificationResultReader {

    /**
     * Class' attributes
     */
    private FLPolicyReader policy;
    private Boolean verificationResult;
    private String verificationMessage;
    private Calendar verificationTime;

    /**
     * Class' constructor
     *
     * @param policy
     * @param verificationResult
     * @param verificationMessage
     * @param verificationTime
     */
    public FLVerificationResultReader(FLPolicyReader policy,
                                      Boolean verificationResult,
                                      String verificationMessage,
                                      Calendar verificationTime) {

        this.policy = policy;
        if ( verificationResult != null ) {
            this.verificationResult = verificationResult;
        } else {
            this.verificationResult = null;
        }
        this.verificationMessage = verificationMessage;
        if ( verificationTime != null ) {
            this.verificationTime = verificationTime;
        } else {
            this.verificationTime = null;
        }
    }

    /**
     * Gives the policy to which this verificationResult is associated.
     *
     * @return
     */
    @Override
    public FLPolicyReader getPolicy() {
        return this.policy;
    }

    /**
     * Gives the verificationResult of the verification. The policy is violated if the verification verificationResult is false, otherwise it is satisfied.
     *
     * @return
     */
    @Override
    public Boolean getVerificationResult() {
        return this.verificationResult;
    }

    /**
     * Gives a human-readable verificationMessage associated with the verificationResult of the verification.
     *
     * @return
     */
    @Override
    public String getVerificationResultMsg() {
        return this.verificationMessage;
    }

    /**
     * Gives the date when the policy has been verified.
     *
     * @return
     */
    @Override
    public Calendar getVerificationTime() {
        return this.verificationTime;
    }

    @Override
    public String toString() {
        return "\t\t Verification Result=" + verificationResult + " \n" +
                "\t\t Verification Message='" + verificationMessage + " \n" +
                "\t\t Verification Time=" + verificationTime;
    }
}
