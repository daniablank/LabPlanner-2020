package org.ucb.constructionFileModel;

/**
 *
 * @author J. Christopher Anderson
 */
public class Acquisition extends Step {
    private final String dnaName;
    
    public Acquisition(String dnaName) {
        this.dnaName = dnaName;
    }

    @Override
    public Operation getOperation() {
        return Operation.acquire;
    }

    @Override
    public String getProduct() {
        return dnaName;
    }
}
