package it.polito.dp2.NFFG.sol1.myLib;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLVerificationResultReader implements it.polito.dp2.NFFG.VerificationResultReader {

    /**
     * Class' attributes
     */
    private FLPolicyReader policy;
    private boolean verificationResult;
    private String verificationMessage;
    private GregorianCalendar verificationTime;

    /**
     * Class' constructor
     *
     * @param policy
     * @param verificationResult
     * @param verificationMessage
     * @param verificationTime
     */
    public FLVerificationResultReader(FLPolicyReader policy,
                                      boolean verificationResult,
                                      String verificationMessage,
                                      GregorianCalendar verificationTime) {
        this.policy = policy;
        this.verificationResult = verificationResult;
        this.verificationMessage = verificationMessage;
        this.verificationTime = verificationTime;
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
}
