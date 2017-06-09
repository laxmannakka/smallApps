package com.next.firebasepushnotification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by next on 1/4/17.
 */
public class MyFireBaseInstanceId extends FirebaseInstanceIdService
{

    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("Token", "onTokenRefresh: "+token);
    }

}
