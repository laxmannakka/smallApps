package com.fees.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by saisasank on 12/28/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeDueDetail {

    private double fineAmount;

    private List<FeeDue> feeDueList;

    private List<Concession> concessionList;
    private boolean installmentBasedFine;
    private boolean installBasedConcession;
    private boolean disableFeeTypeInParentLogin;

    public boolean isInstallBasedConcession() {
        return installBasedConcession;
    }

    public void setInstallBasedConcession(boolean installBasedConcession) {
        this.installBasedConcession = installBasedConcession;
    }

    public boolean isInstallmentBasedFine() {
        return installmentBasedFine;
    }

    public void setInstallmentBasedFine(boolean installmentBasedFine) {
        this.installmentBasedFine = installmentBasedFine;
    }

    public boolean isDisableFeeTypeInParentLogin() {
        return disableFeeTypeInParentLogin;
    }

    public void setDisableFeeTypeInParentLogin(boolean disableFeeTypeInParentLogin) {
        this.disableFeeTypeInParentLogin = disableFeeTypeInParentLogin;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public List<FeeDue> getFeeDueList() {
        return feeDueList;
    }

    public void setFeeDueList(List<FeeDue> feeDueList) {
        this.feeDueList = feeDueList;
    }

    public List<Concession> getConcessionList() {
        return concessionList;
    }
    private boolean feeChallan;
    public boolean isFeeChallan() {
        return feeChallan;
    }

    public void setFeeChallan(boolean feeChallan) {
        this.feeChallan = feeChallan;
    }

    public void setConcessionList(List<Concession> concessionList) {
        this.concessionList = concessionList;
    }
}