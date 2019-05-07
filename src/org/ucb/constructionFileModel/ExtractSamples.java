package org.ucb.constructionFileModel;

/**
 * ExtractSamples <Samples>

 * @author lucaskampman
 *
 * NOTE: changed to ExtractLysate and ExtractSupernatant
 */
public class ExtractSamples extends Step {
    private final String _sample;
    private final String product;

    public ExtractSamples(String sample, String product) {
        this._sample = sample;
        this.product = product;
    }

    public String getSamples() {
        return _sample;
    }

    @Override
    public Operation getOperation() {
        return Operation.extractSamples;
    }

    @Override
    public String getProduct() {
        return product;
    }
}
