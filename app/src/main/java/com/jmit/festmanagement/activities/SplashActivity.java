package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.Utils;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by arpitkh96 on 3/10/16.
 */

public class SplashActivity extends BaseActivity {
    String uid;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("isStudent", true)) {
            if ((uid = sharedPreferences.getString("uid", null)) != null) {

                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("roll_no", uid);
                hashMap.put("device_id", Utils.getdeviceId(this));
                VolleyHelper.postRequestVolley(this, URL_API.LOGIN_API, hashMap, RequestCodes.LOGIN);
                return;
            } else new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startHome();
                }
            }, 2000);
        } else {

        }
    }

    void startHome() {
        startActivity((Home.class));
    }


    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        try {
            response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
            JSONObject jsonObject = new JSONObject(response);
            int i = jsonObject.getInt("success");
            if (i == 1) {
                Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("uid", uid).putBoolean("isStudent", true).commit();
                startActivity( MainActivity.class);
            } else startHome();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
        startHome();
        }
        }
}