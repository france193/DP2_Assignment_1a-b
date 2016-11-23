package it.polito.dp2.NFFG.sol1;

/** Assignment 1b.2 **/

import it.polito.dp2.NFFG.sol1.myLib.NffgVerifier;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory {

    /**
     * Void constructor
     */
    public NffgVerifierFactory() {
    }

    /**
     * Create an instance of the concrete class NffgVerifier which extends the abstrac interface NffgVerifier
     * @return
     * @throws it.polito.dp2.NFFG.NffgVerifierException
     */
    @Override
    public it.polito.dp2.NFFG.NffgVerifier newNffgVerifier() throws it.polito.dp2.NFFG.NffgVerifierException {
        return new NffgVerifier();
    }
}

