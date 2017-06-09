package com.fees.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties (ignoreUnknown = true)
public class PaymentStatus {

	private String transId;

	private String amount;

	private String msg;

	private String code;

	private long feeCollectionId;

	public long getFeeCollectionId() {
		return feeCollectionId;
	}

	public void setFeeCollectionId(long feeCollectionId) {
		this.feeCollectionId = feeCollectionId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode()
	{
		return code;
	}
}