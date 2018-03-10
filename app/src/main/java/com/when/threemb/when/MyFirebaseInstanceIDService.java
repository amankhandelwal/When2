package com.when.threemb.when;

import android.app.Service;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by User on 1/21/2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static String TAG = "Registration";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.

        //System.out.println("Registration.onTokenRefresh TOKEN: " + refreshedToken );
    }
}
