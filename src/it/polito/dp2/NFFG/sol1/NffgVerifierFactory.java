package it.polito.dp2.NFFG.sol1;

/** Assignment 1b.2 **/

import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.sol1.myLib.FLNffgVerifier;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.util.HashMap;

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
    public NffgVerifier newNffgVerifier() throws NffgVerifierException {

        NffgVerifier myNffgVerifier;

        try {
            myNffgVerifier = new FLNffgVerifier();
        } catch (JAXBException e) {
            System.err.println("JAXBException Error: "+e.getMessage());
            e.printStackTrace();
            throw new NffgVerifierException(e.getMessage());
        } catch (SAXException e) {
            System.err.println("NffgVerifierException Error: "+e.getMessage());
            e.printStackTrace();
            throw new NffgVerifierException(e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("NullPointerException Error: "+e.getMessage());
            e.printStackTrace();
            throw new NffgVerifierException(e.getMessage());
        }

        return myNffgVerifier;
    }

    //toString() implemented for debugging purposes
    @Override
    public String toString(){
        return "This is a custom NffgVerifierFactory implementation for the assignment 2.";
    }
}
