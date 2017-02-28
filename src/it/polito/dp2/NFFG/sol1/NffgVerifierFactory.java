package it.polito.dp2.NFFG.sol1;

/**
 * Created by Francesco Longo (223428) on 10/02/2017.
 * Assignment 1b.2
 **/

import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;

/**
 * To test all the code:
 * <p>
 * ant -Dtestcase=0 -Dseed=100000 runFuncTest
 */

/**
 * Created by Francesco Longo (223428) on 10/02/2017.
 */
public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory {

    /**
     * Void constructor
     */
    public NffgVerifierFactory() {
    }

    /**
     * Create an instance of the concrete class NffgVerifier which extends the
     * abstrac interface NffgVerifier
     *
     * @return
     * @throws it.polito.dp2.NFFG.NffgVerifierException
     */
    @Override
    public NffgVerifier newNffgVerifier() throws NffgVerifierException {
        NffgVerifier myNffgVerifier = new FLNffgVerifier();
        return myNffgVerifier;
    }
}
