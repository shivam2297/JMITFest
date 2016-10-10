package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jmit.festmanagement.Main;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.Utils;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class StudentLogin extends BaseActivity {
    Toolbar toolbar;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stulogin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText Rollno = (EditText) findViewById(R.id.editText);
        Button stulogin = (Button) findViewById(R.id.stuLogin);
        TextView signUp = (TextView) findViewById(R.id.SignUp);
        SpannableString content = new SpannableString("Sign Up?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        signUp.setText(content);
        stulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Rollno.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "Fill All The Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                uid = Rollno.getText().toString();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("roll_no", uid);
                hashMap.put("device_id", Utils.getdeviceId(StudentLogin.this));
                VolleyHelper.postRequestVolley(StudentLogin.this, URL_API.LOGIN_API, hashMap, RequestCodes.LOGIN);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentLogin.this, SignUpActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(Home.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(( Home.class));
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        dismissDialog();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int i = jsonObject.getInt("success");
            if (i == 1) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                editor.putString("uid", uid).putBoolean("isStudent", true).commit();
                editor.putString("user", jsonObject.getString("user_name")).commit();
                editor.putString("email", jsonObject.getString("email")).commit();
                editor.putBoolean("isAdmin",false).commit();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else
                Snackbar.make(findViewById(R.id.main_frame), jsonObject.getString("message"), Snackbar.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        showDialog();
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        dismissDialog();
    }

}
