package com.fees.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties (ignoreUnknown = true)
public class  TransactionDetails {

	private String param01; //transactionId

	private String param02; //refNo

	private String param03; //merchantCode

	private String param04; //currencyCode

	private String param05; //amount

	public String getParam06() {
		return param06;
	}

	public void setParam06(String param06) {
		this.param06 = param06;
	}

	public String getParam07() {
		return param07;
	}

	public void setParam07(String param07) {
		this.param07 = param07;
	}

	private String param06; //BILLDESK or TECHPROCESS
	private String param07;// msg String
	private String param08;//email

	public String getParam09() {
		return param09;
	}

	public void setParam09(String param09) {
		this.param09 = param09;
	}

	public String getParam08() {
		return param08;
	}

	public void setParam08(String param08) {
		this.param08 = param08;
	}

	private String param09;// phonr no

	public String getParam01() {
		return param01;
	}

	public void setParam01(String param01) {
		this.param01 = param01;
	}

	public String getParam02() {
		return param02;
	}

	public void setParam02(String param02) {
		this.param02 = param02;
	}

	public String getParam03() {
		return param03;
	}

	public void setParam03(String param03) {
		this.param03 = param03;
	}

	public String getParam04() {
		return param04;
	}

	public void setParam04(String param04) {
		this.param04 = param04;
	}

	public String getParam05() {
		return param05;
	}

	public void setParam05(String param05) {
		this.param05 = param05;
	}



	//private String encryptionKey;
	//private String encryptionIV;
}
