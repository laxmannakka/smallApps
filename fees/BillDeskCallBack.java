package com.fees;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.billdesk.sdk.LibraryPaymentStatusProtocol;
import com.helper.BaseFragment;
import com.helper.CustomLogger;
import com.helper.OnNavigationListener;
import com.home.HomeActivity;
import com.resources.erp.R;
import com.utils.ERPModel;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class BillDeskCallBack implements LibraryPaymentStatusProtocol, Parcelable {
	String TAG = "CallBackActivity Callback ::: > ";
	private OnNavigationListener mNavigationListener;
	private AppCompatActivity _activity;

	public BillDeskCallBack() {
		Log.v(TAG, "CallBackActivity CallBack()....");
	}

	public BillDeskCallBack(Parcel in) {
		Log.v(TAG, "CallBackActivity CallBack(Parcel in)....");
	}

	@Override
	public void paymentStatus(String status, Activity context) {
	//	Toast.makeText(context, "CallBackActivity PG Response:: " + status, Toast.LENGTH_LONG).show();

		Map<String, Object> jsonFeeDueStructure = new HashMap<String, Object>();
		jsonFeeDueStructure.put("paymentGatewayName", ERPModel.currentTransactionDetails.getParam06()); //Unique Identifier for payment gateway name
		jsonFeeDueStructure.put("responseMessage", status);

		final OutputStream out = new ByteArrayOutputStream();
		final ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(out, jsonFeeDueStructure);
		} catch (JsonGenerationException e) {

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final byte[] data1 = ((ByteArrayOutputStream) out).toByteArray();
		CustomLogger.i("", new String(data1));
		/*PaymentSummaryFragment newFragment = new PaymentSummaryFragment();
		Bundle args = new Bundle();
		args.putString("payment_json", new String(data1));
		newFragment.setArguments(args);
        _activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, newFragment).commit();*/
		Intent intent = new Intent(context, FeeStatusActivity.class);
		intent.putExtra("payment_json", new String(data1));
		intent.putExtra("showPaymentSummary", true);
		context.startActivity(intent);
		context.finish();


//		if(mBillDeskPaymentListener != null) {
//			mBillDeskPaymentListener.onPaymentComplete(new String(data1));
//		}

		/*PaymentSummaryFragment newFragment = new PaymentSummaryFragment();
		// PaymentSummaryFragment1 newFragment = new PaymentSummaryFragment1();
		Bundle args = new Bundle();
		args.putString("payment_json", new String(data1));
		newFragment.setArguments(args);
		BaseFragment._onNavigationListener.onShowFragment(newFragment, PaymentSummaryFragment.class.getName(), false, false, false);*/
		/*getSupportActionBar().setTitle("Payment Details");*/
	}

	@Override
	public int describeContents() {
		Log.v(TAG, "CallBackActivity describeContents()....");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.v(TAG, "CallBackActivity writeToParcel(Parcel dest, int flags)....");
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		String TAG = "CallBackActivity --- Parcelable.Creator ::: > ";

		@Override
		public BillDeskCallBack createFromParcel(Parcel in) {
			Log.v(TAG, "CallBackActivity createFromParcel(Parcel in)....");
			return new BillDeskCallBack(in);
		}

		@Override
		public Object[] newArray(int size) {
			Log.v(TAG, "CallBackActivity Object[] newArray(int size)....");
			return new BillDeskCallBack[size];
		}
	};
}
