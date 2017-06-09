package com.fees.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeDue {
	private String feeType;

	private long feeTypeId;
	
	private long feeDueId;
	
	private long subTypeId;

	private double total;

	private double concession;

	private String instalType;

	private double fineAmount;

	private double amount;

	private Date dueDate;

	private boolean isChecked;
	
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@JsonProperty("subTypeId")
	public long getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(long subTypeId) {
		this.subTypeId = subTypeId;
	}

	@JsonProperty("feeDueId")
	public long getFeeDueId() {
		return feeDueId;
	}

	public void setFeeDueId(long feeDueId) {
		this.feeDueId = feeDueId;
	}

	@JsonProperty("feeTypeId")
	public long getFeeTypeId() {
		return feeTypeId;
	}

	public void setFeeTypeId(long feeTypeId) {
		this.feeTypeId = feeTypeId;
	}

	@JsonProperty("feeTypeName")
	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	@JsonProperty("total")
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@JsonProperty("concessionAmount")
	public double getConcession() {
		return concession;
	}

	public void setConcession(double concession) {
		this.concession = concession;
	}

	@JsonProperty("installmentName")
	public String getInstalType() {
		return instalType;
	}

	public void setInstalType(String instalType) {
		this.instalType = instalType;
	}

	@JsonProperty("fineAmount")
	public double getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(double fineAmount) {
		this.fineAmount = fineAmount;
	}

	@JsonProperty("amount")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

}
