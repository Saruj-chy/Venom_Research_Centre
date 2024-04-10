package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.AUTHORITY_PERMISSION;
import static com.sd.spartan.vrc.controller.AppConstants.CHECK_NET_CONNECT;
import static com.sd.spartan.vrc.controller.AppConstants.EMAIL_BODY;
import static com.sd.spartan.vrc.controller.AppConstants.EMAIL_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.EMAIL_SUB;
import static com.sd.spartan.vrc.controller.AppConstants.ERROR;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE_STR;
import static com.sd.spartan.vrc.controller.AppConstants.MAM_USERNAME;
import static com.sd.spartan.vrc.controller.AppConstants.MSG;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class EmailActivity extends AppCompatActivity {

    TextInputEditText mEmailNameTET, mSubjectTET, mBodyTET ;

    String  mEmailName, mSub, mBody ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        SimpleClass.CheckInActive(TRUE);

        Toolbar toolbar = findViewById(R.id.appbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle(AUTHORITY_PERMISSION) ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        mEmailNameTET = findViewById(R.id.edit_email_name) ;
        mSubjectTET = findViewById(R.id.edit_subject) ;
        mBodyTET = findViewById(R.id.edit_body) ;

    }

    public void onEmailSent(View view) {
        mEmailName = Objects.requireNonNull(mEmailNameTET.getText()).toString() ;
        mSub = Objects.requireNonNull(mSubjectTET.getText()).toString() ;
        mBody = Objects.requireNonNull(mBodyTET.getText()).toString() ;

        emailSent() ;
    }

    private void emailSent() {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(EMAIL_NAME, mEmailName) ;
        map.put(EMAIL_SUB, mSub) ;
        map.put(EMAIL_BODY, mBody) ;
        map.put(MAM_USERNAME, AppController.mem_username) ;

        AppController.getAppController().getAppNetworkController().makeRequest(Config.EMAIL_SENT_AUTHOR, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                    Toast.makeText(EmailActivity.this, object.getString(MSG), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EmailActivity.this, object.getString(MSG), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(EmailActivity.this, CHECK_NET_CONNECT, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(EmailActivity.this, CHECK_NET_CONNECT, Toast.LENGTH_SHORT).show(), map );

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!SimpleClass.CheckActive){
            SimpleClass.IntentLogin(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SimpleClass.CheckInActive(FALSE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SimpleClass.CheckInActive(TRUE);
    }
}