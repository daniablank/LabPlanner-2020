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
 */
public class Miniprep extends Step {
    private final String product;
    private final String culture; //culture inoculum miniprepping from
            
    public Miniprep(String culture, String product){
        this.product = product;
        this.culture = culture;
        List<String> substrates = new ArrayList<>();
        substrates.add(culture);
        this.setSubstrates(substrates);
    }
    
    public String getCulture() {
        return culture;
    }
            
            
   @Override
    public Operation getOperation() {
        return Operation.miniprep;
    }

    @Override
    public String getProduct() {
        return product;
    }
}
