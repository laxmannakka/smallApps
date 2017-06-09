package com.fees.interfaces;


import com.fees.model.TransactionDetails;

public interface PaymentListener {
	public void createTechProcessCheckoutObject(TransactionDetails currentTransactionDetails);
	public void openPaymentGateway();
	public void sendSelectedFeeMap(boolean b);
}
