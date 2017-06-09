package com.fees.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by saisasank on 12/28/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Concession {

    private double Amount;

    private String concessionName;

    public String getConcessionName() {
        return concessionName;
    }

    public void setConcessionName(String concessionName) {
        this.concessionName = concessionName;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }


}

