package org.ucb.constructionFileModel;

import java.util.List;

/**
 *
 * @author J. Christopher Anderson
 */
public class Ligation extends Step {
    private final List<String> fragments;
    private final String product;

    public Ligation(List<String> fragments, String product) {
        this.fragments = fragments;
        this.setSubstrates(fragments);
        this.product = product;
    }

    public List<String> getFragments() {
        return this.fragments;
    }

    @Override
    public Operation getOperation() {
        return Operation.ligate;
    }

    @Override
    public String getProduct() {
        return product;
    }
}
