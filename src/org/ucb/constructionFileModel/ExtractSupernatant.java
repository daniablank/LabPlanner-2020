package org.ucb.constructionFileModel;

/**
 * ExtractSamples <Samples>

 * @author lucaskampman
 *
 * NOTE: changed to ExtractLysate and ExtractSupernatant
 */
public class ExtractSupernatant extends Step {
    private final String _sample;
    private final String product;

    public ExtractSupernatant(String sample, String product) {
        this._sample = sample;
        this.product = product;
    }

    public String getSamples() {
        return _sample;
    }

    @Override
    public Operation getOperation() {
        return Operation.extractSupernatant;
    }

    @Override
    public String getProduct() {
        return product;
    }
}
