package org.ucb.constructionFileModel;


//import org.ucb.c5.inventoryModel.ContainerType;

/**
 * Inoculate is run on streaks and yields culture tubes

 * @author lucaskampman
 */
public class Inoculate extends Step {
    private final String _sample;
    private final String _product;
    private final Antibiotic _antibiotic;
   // private final ContainerType _containerType;


    public Inoculate(String sample, Antibiotic antibiotic, /*ContainerType containerType,*/ String product) {
        this._sample = sample;
        this._product = product;
        this._antibiotic = antibiotic;
       // this._containerType = containerType;
    }

    public String getSample() {
        return _sample;
    }

    @Override
    public Operation getOperation() {
        return Operation.inoculate;
    }

   /* public ContainerType getOutputContainerType() {
        return _containerType;
    }*/

    public Antibiotic getAntibiotic() {
        return _antibiotic;
    }

    @Override
    public String getProduct() {
        return _product;
    }


}