package com.jmit.festmanagement.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.FLog;
import com.jmit.festmanagement.utils.VolleyInterface;

/**
 * Created by arpitkh96 on 10/10/16.
 */

public class BaseFragment extends Fragment implements VolleyInterface {
    @Override
    public void requestStarted(int requestCode) {

    }

    @Override
    public void requestCompleted(int requestCode, String response) {
    FLog.d("Response",response);
    }
    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        FLog.d(error.getMessage());
        String msg = "";
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
            msg = getResources().getString(R.string.network_error);
        } else if (error instanceof ParseError) {
            msg = "Response Parse Error";
        } else if (error instanceof AuthFailureError) {
            msg = "Authentication Failure Error";
        } else if (error instanceof ServerError) {
            msg = "Server Error";
        }
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}
