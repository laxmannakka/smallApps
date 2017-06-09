package com.merchantapp.testkit;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ebs.android.sdk.Config.Encryption;
import com.ebs.android.sdk.Config.Mode;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;
import java.util.ArrayList;
import java.util.HashMap;


public class BuyProduct extends Activity implements OnClickListener{

	Button btn_buy;
	Double amount;
	EditText ed_quantity, ed_totalamount;

	private static String HOST_NAME = "";


	ArrayList<HashMap<String, String>> custom_post_parameters;

	private static final int ACC_ID = 24225;// Provided by EBS
	private static final String SECRET_KEY = "c2421d2bb8728827ce9ce60322a2e7ca";// Provided by EBS

	private static final double PER_UNIT_PRICE = 1.00;
	double totalamount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_buy_product);

		HOST_NAME = getResources().getString(R.string.hostname);
		initview();
		setOnClickListener();
	}

	protected void initview() {
		btn_buy = (Button) findViewById(R.id.btn_buy);
		ed_quantity = (EditText) findViewById(R.id.ed_quantity);
		ed_totalamount = (EditText) findViewById(R.id.ed_totalamount);
	}


	protected void setOnClickListener() {
		btn_buy.setOnClickListener(this);

		ed_quantity.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

				calculateTotalAmount();
				ed_totalamount.setText(String.format("%.2f", totalamount));
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

	}

	private void calculateTotalAmount() {

		if (ed_quantity.getText().toString().trim().length() > 0) {
			int quantity = Integer.parseInt(ed_quantity.getText().toString());
			totalamount = quantity * PER_UNIT_PRICE;

		} else {
			totalamount = 0.00;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_buy) {
			if (ed_quantity.getText().toString().trim().length() <= 0
					|| Integer
					.parseInt(ed_quantity.getText().toString().trim()) == 0) {
				Toast.makeText(getApplicationContext(),"Please Enter Quantity", Toast.LENGTH_LONG).show();
			} else {
				callEbsKit(this);
			}
		}
	}

	private void callEbsKit(BuyProduct buyProduct) {
		/**
		 * Set Parameters Before Initializing the EBS Gateway, All mandatory
		 * values must be provided
		 */

		/** Payment Amount Details */
		// Total Amount

		PaymentRequest.getInstance().setTransactionAmount(String.format("%.2f", totalamount));

		/** Mandatory */

		PaymentRequest.getInstance().setAccountId(ACC_ID);
		PaymentRequest.getInstance().setSecureKey(SECRET_KEY);

		// Reference No
		PaymentRequest.getInstance().setReferenceNo("223");
		/** Mandatory */

		// Email Id
		//PaymentRequest.getInstance().setBillingEmail("test_tag@testmail.com");

		PaymentRequest.getInstance().setBillingEmail("test@testmail.com");
		/** Mandatory */

		PaymentRequest.getInstance().setFailureid("1");

		// PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));
		// System.out.println("FAILURE MESSAGE"+getResources().getString(R.string.payment_failure_message));

		/** Mandatory */

		// Currency
		PaymentRequest.getInstance().setCurrency("INR");
		/** Mandatory */

		/** Optional */
		// Your Reference No or Order Id for this transaction
		PaymentRequest.getInstance().setTransactionDescription(
				"Test Transaction");

		/** Billing Details */
		PaymentRequest.getInstance().setBillingName("Test_Name");
		/** Optional */
		PaymentRequest.getInstance().setBillingAddress("North Mada Street");
		/** Optional */
		PaymentRequest.getInstance().setBillingCity("Chennai");
		/** Optional */
		PaymentRequest.getInstance().setBillingPostalCode("600019");
		/** Optional */
		PaymentRequest.getInstance().setBillingState("Tamilnadu");
		/** Optional */
		PaymentRequest.getInstance().setBillingCountry("IND");
		/** Optional */
		PaymentRequest.getInstance().setBillingPhone("01234567890");
		/** Optional */

		/** Shipping Details */
		PaymentRequest.getInstance().setShippingName("Test_Name");
		/** Optional */
		PaymentRequest.getInstance().setShippingAddress("North Mada Street");
		/** Optional */
		PaymentRequest.getInstance().setShippingCity("Chennai");
		/** Optional */
		PaymentRequest.getInstance().setShippingPostalCode("600019");
		/** Optional */
		PaymentRequest.getInstance().setShippingState("Tamilnadu");
		/** Optional */
		PaymentRequest.getInstance().setShippingCountry("IND");
		/** Optional */
		PaymentRequest.getInstance().setShippingEmail("test@testmail.com");
		/** Optional */
		PaymentRequest.getInstance().setShippingPhone("01234567890");
		/** Optional */

		PaymentRequest.getInstance().setLogEnabled("1");


		/**
		 * Payment option configuration
		 */

		/** Optional */
		PaymentRequest.getInstance().setHidePaymentOption(false);

		/** Optional */
		PaymentRequest.getInstance().setHideCashCardOption(false);

		/** Optional */
		PaymentRequest.getInstance().setHideCreditCardOption(false);

		/** Optional */
		PaymentRequest.getInstance().setHideDebitCardOption(false);

		/** Optional */
		PaymentRequest.getInstance().setHideNetBankingOption(false);

		/** Optional */
		PaymentRequest.getInstance().setHideStoredCardOption(false);

		/**
		 * Initialise parameters for dyanmic values sending from merchant
		 */

		custom_post_parameters = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashpostvalues = new HashMap<String, String>();
		hashpostvalues.put("account_details", "saving");
		hashpostvalues.put("merchant_type", "gold");
		hashpostvalues.put("secure_hash","c2421d2bb8728827ce9ce60322a2e7ca");
		custom_post_parameters.add(hashpostvalues);

		PaymentRequest.getInstance()
		.setCustomPostValues(custom_post_parameters);
		/** Optional-Set dyanamic values */

		// PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));

		EBSPayment.getInstance().init(buyProduct, ACC_ID, SECRET_KEY,
				Mode.ENV_LIVE, Encryption.ALGORITHM_MD5, HOST_NAME);


	}
}