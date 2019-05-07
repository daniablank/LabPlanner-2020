/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ucb.constructionFileModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Connor Tou
 * @author lucaskampman
 */
public class Sequencing extends Step {
    private final String product;
    private final String oligo; //oligo used for sequencing reaction
            
    public Sequencing(String substrate, String product, String oligo){
        this.product = product;
        this.oligo= oligo;
        List<String> substrates = new ArrayList<>();
        substrates.add(substrate);
        this.setSubstrates(substrates);
    }
    public String getOligo() {
        return oligo;
    }

   @Override
    public Operation getOperation() {
        return Operation.sequence;
    }

    @Override
    public String getProduct() {
        return product;
    }
}
