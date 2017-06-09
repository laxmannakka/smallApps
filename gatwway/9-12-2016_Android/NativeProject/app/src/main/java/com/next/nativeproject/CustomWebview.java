package com.next.nativeproject;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class CustomWebview  extends WebView{

	MainActivity mainActivity;
	
	
	
	@SuppressLint("SetJavaScriptEnabled")
	public CustomWebview(MainActivity mainActivity) {
		super(mainActivity);
		this.mainActivity = mainActivity;
		
		WebSettings webSettings = this.getSettings();
		webSettings.setJavaScriptEnabled(true);
		/**
		 * the important
		 */
		this.addJavascriptInterface(new JavaAndJavascriptBridge(),"JavaAndJavascriptBridge");
		
		this.loadUrl("file:///android_asset/ll.htmlcode");
		
		
	}
	
	int x = 0;
	/**
	 * 
	 * @author lau
	 *
	 */
	 public class JavaAndJavascriptBridge{
		 
		 
		 public void showData(final String dataFromJavascript){
			 
		 
			 
			mainActivity.textView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					 mainActivity.textView.setText(dataFromJavascript+" "+x);
					 Log.e("data",dataFromJavascript+"..");
					 x++;
				}
			}, 100);
			
			 
			 Toast.makeText(mainActivity, dataFromJavascript, Toast.LENGTH_SHORT).show();
			 
		 }
	 }
}
