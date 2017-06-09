package com.fees.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class ERPCartObject {

	private String feeTypeName;

	private long feeDueId;

	private long feeTypeId;

	private double total;

	private long subTypeId;

	private double concessionAmount;

	private String installmentName;

	private double fineAmount;

	private double amount;

	private Date dueDate;

	public double getConcessionAmount() {
		return concessionAmount;
	}

	public void setConcessionAmount(double concession) {
		this.concessionAmount = concession;
	}

	public String getInstallmentName() {
		return installmentName;
	}

	public void setInstallmentName(String instalType) {
		this.installmentName = instalType;
	}

	public double getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(double fineAmount) {
		this.fineAmount = fineAmount;
	}

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

	public long getFeeTypeId() {
		return feeTypeId;
	}

	public void setFeeTypeId(long feeTypeId) {
		this.feeTypeId = feeTypeId;
	}

	public long getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(long subTypeId) {
		this.subTypeId = subTypeId;
	}

	public long getFeeDueId() {
		return feeDueId;
	}

	public void setFeeDueId(long feeDueId) {
		this.feeDueId = feeDueId;
	}

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeType) {
		this.feeTypeName = feeType;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

}
