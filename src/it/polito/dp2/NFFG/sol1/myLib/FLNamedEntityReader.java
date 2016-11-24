package it.polito.dp2.NFFG.sol1.myLib;

/**
 * Created by FLDeviOS on 23/11/2016.
 */
public class FLNamedEntityReader implements it.polito.dp2.NFFG.NamedEntityReader {

    private String entityName;

    /**
     * Class' construcotr
     * @param entityName
     */
    FLNamedEntityReader(String entityName) {
        this.entityName = entityName;
    }

    /**
     * Gives the entityName of this entity.
     *
     * @return
     */
    @Override
    public String getName() {
        return this.entityName;
    }

    /**
     * Set the entityName of this entity.
     *
     * @param entityName
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
