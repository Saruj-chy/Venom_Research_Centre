package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.AUTH_TOKEN;
import static com.sd.spartan.vrc.controller.AppConstants.CONFIRM_PASS_NOT_MATCH;
import static com.sd.spartan.vrc.controller.AppConstants.DATA;
import static com.sd.spartan.vrc.controller.AppConstants.ERROR;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE_STR;
import static com.sd.spartan.vrc.controller.AppConstants.LAT;
import static com.sd.spartan.vrc.controller.AppConstants.LOGIN;
import static com.sd.spartan.vrc.controller.AppConstants.LOGIN_CONTINUE;
import static com.sd.spartan.vrc.controller.AppConstants.LOG_ID;
import static com.sd.spartan.vrc.controller.AppConstants.LONG;
import static com.sd.spartan.vrc.controller.AppConstants.MEMBER;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_PASS;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_PASSWORD;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_PHN_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_STS_ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_USERNAME;
import static com.sd.spartan.vrc.controller.AppConstants.MSG;
import static com.sd.spartan.vrc.controller.AppConstants.NEW;
import static com.sd.spartan.vrc.controller.AppConstants.NEW_ACC;
import static com.sd.spartan.vrc.controller.AppConstants.PASS_LEAST_8_CHARR;
import static com.sd.spartan.vrc.controller.AppConstants.PLZ_CONTACT_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.PLZ_USERNAME;
import static com.sd.spartan.vrc.controller.AppConstants.PLZ_WAIT;
import static com.sd.spartan.vrc.controller.AppConstants.REGISTRATION_CONTINUE;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sd.spartan.vrc.MainActivity;
import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout mNameTIL, mPassTIL,mConfirmPassTIL, mPhnTIL;
    private TextInputEditText mNameTET, mPassTET,mConfirmPassTET, mPhnTET;
    private TextView regTitleTV, regTV, regBtnTV, loginTextTV, loginBtnTV;

    private String name, password, confirmPass, phn;
    private ProgressDialog loading ;

    private LocationController locationController ;
    private AlertDialog backPressDialog ;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initialize() ;

        locationController = new LocationController(this) ;
        locationController.getLastLocation();

        OnClickView();
    }

    private void OnClickView() {
        regTV.setOnClickListener(view -> OnExistAccData());
        regBtnTV.setOnClickListener(view -> {
            getAllInputText() ;

            if(password.length() <8){
                Toast.makeText(LoginActivity.this, PASS_LEAST_8_CHARR, Toast.LENGTH_SHORT).show();
            }else if(!password.equalsIgnoreCase(confirmPass)){
                Toast.makeText(LoginActivity.this, CONFIRM_PASS_NOT_MATCH, Toast.LENGTH_SHORT).show();
            }else if(phn.equalsIgnoreCase("") || phn.length() <10 || phn.length() >12 ){
                Toast.makeText(LoginActivity.this, PLZ_CONTACT_NUM, Toast.LENGTH_SHORT).show();
            }else if(name.equalsIgnoreCase("")){
                Toast.makeText(LoginActivity.this, PLZ_USERNAME, Toast.LENGTH_SHORT).show();
            } else{
                RegMemberdata() ;
            }
        });
        loginTextTV.setOnClickListener(view -> {
            ClearTextInput();
            regTitleTV.setText(REGISTRATION_CONTINUE);
            visibility("new");
        });
        loginBtnTV.setOnClickListener(view -> {
            getAllInputText() ;
            locationController.getLastLocation();
            if(phn.equalsIgnoreCase("") || phn.length() <10 || phn.length() >12 ){
                Toast.makeText(LoginActivity.this, PLZ_CONTACT_NUM, Toast.LENGTH_SHORT).show();
            } else{
                LoginMemberdata() ;
            }
        });
    }

    private void initialize() {
        mNameTIL = findViewById(R.id.text_name_layout) ;
        mNameTET = findViewById(R.id.edit_user_name) ;
        mPassTIL = findViewById(R.id.text_password_layout) ;
        mPassTET = findViewById(R.id.edit_pass) ;
        mConfirmPassTIL = findViewById(R.id.text_confirm_password_layout) ;
        mConfirmPassTET = findViewById(R.id.edit_confirm_pass) ;
        mPhnTIL = findViewById(R.id.text_phn_layout) ;
        mPhnTET = findViewById(R.id.edit_phn) ;
        regTitleTV = findViewById(R.id.text_reg_title) ;
        regTV = findViewById(R.id.text_reg) ;
        regBtnTV = findViewById(R.id.text_reg_btn) ;
        loginTextTV = findViewById(R.id.text_login) ;
        loginBtnTV = findViewById(R.id.text_login_btn) ;

        loading = new ProgressDialog(this);
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this, R.style.MaterialAlertDialog_rounded) ;

    }



    private void OnExistAccData(){
        ClearTextInput();
        regTitleTV.setText(LOGIN_CONTINUE);
        visibility("");
    }

    private void visibility(String name){
        if(name.equalsIgnoreCase(NEW)){
            mNameTIL.setVisibility(View.VISIBLE);
            mConfirmPassTIL.setVisibility(View.VISIBLE);
            regTV.setVisibility(View.VISIBLE);
            regBtnTV.setVisibility(View.VISIBLE);
            loginTextTV.setVisibility(View.GONE);
            loginBtnTV.setVisibility(View.GONE);
        }else{
            mNameTIL.setVisibility(View.GONE);
            mConfirmPassTIL.setVisibility(View.GONE);
            regTV.setVisibility(View.GONE);
            regBtnTV.setVisibility(View.GONE);
            loginTextTV.setVisibility(View.VISIBLE);
            loginBtnTV.setVisibility(View.VISIBLE);

        }

        mPhnTIL.setVisibility(View.VISIBLE);
        mPassTIL.setVisibility(View.VISIBLE);
    }


    private void RegMemberdata() {
        loading.setTitle(NEW_ACC);
        loading.setMessage(PLZ_WAIT);
        loading.show();

        AppController.getAppController().getAppNetworkController()
                .makeRequest(Config.VRC_MEMBER_REG, response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        AppController.getAppController().getInAppNotifier().showToast(object.getString(MSG));

                        OnExistAccData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loading.dismiss();
                }, error -> loading.dismiss(), hashmap(1+"") );
    }

    private void intentMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class) ;
        startActivity(intent);
    }

    private void getAllInputText() {
        name = Objects.requireNonNull(mNameTET.getText()).toString() ;
        password = Objects.requireNonNull(mPassTET.getText()).toString() ;
        confirmPass = Objects.requireNonNull(mConfirmPassTET.getText()).toString() ;
        phn = Objects.requireNonNull(mPhnTET.getText()).toString() ;
    }



    private HashMap<String, String> hashmap(String logId){
        HashMap<String, String> map = new HashMap<>() ;
        map.put(LOG_ID, logId) ;
        map.put(MEM_USERNAME, name) ;
        map.put(MEM_PHN_NUM, phn) ;
        map.put(MEM_PASSWORD, password) ;
        map.put(LAT, LocationController.latitude+"") ;
        map.put(LONG, LocationController.longitude+"" ) ;
        return map;
    }

    private void LoginMemberdata() {
        loading.setTitle(LOGIN);
        loading.setMessage(PLZ_WAIT);
        loading.show();

        AppController.getAppController().getAppNetworkController()
                .makeRequest(Config.VRC_MEMBER_REG, response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                            SimpleClass.CheckInActive(TRUE);
                            intentMainActivity();

                            getSharedPreferences(MEMBER, MODE_PRIVATE).edit().putString(AUTH_TOKEN, object.getString(AUTH_TOKEN)).apply();
                            JSONObject JO = object.getJSONObject(DATA);
                            getSharedPreferences(MEMBER, MODE_PRIVATE).edit().putString(MEM_ID, JO.getString(MEM_ID) ).apply();
                            getSharedPreferences(MEMBER, MODE_PRIVATE).edit().putString(MEM_USERNAME, JO.getString(MEM_USERNAME) ).apply();
                            getSharedPreferences(MEMBER, MODE_PRIVATE).edit().putString(MEM_PHN_NUM, JO.getString(MEM_PHN_NUM) ).apply();
                            getSharedPreferences(MEMBER, MODE_PRIVATE).edit().putString(MEM_PASS, JO.getString(MEM_PASSWORD) ).apply();

                            AppController.mem_sts_id=JO.getString(MEM_STS_ID) ;
                            AppController.checkAuthourity = true ;
                        }

                        AppController.getAppController().getInAppNotifier().showToast(object.getString(MSG));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loading.dismiss();
                }, error -> loading.dismiss(), hashmap(2+"") );
    }

    private void CreateBackpressDialog() {
        View view = getLayoutInflater().inflate(R.layout.popup_backpress,  null);
        TextView textNo = view.findViewById(R.id.text_no) ;
        TextView textYes = view.findViewById(R.id.text_yes) ;

        textNo.setOnClickListener(v -> backPressDialog.cancel());

        textYes.setOnClickListener(v -> {
            backPressDialog.cancel();
            moveTaskToBack(true) ;
        });

        materialAlertDialogBuilder.setView(view);
        backPressDialog = materialAlertDialogBuilder.create();
        backPressDialog.show();
        backPressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void ClearTextInput(){
        mPhnTET.setText("");
        mPassTET.setText("");
        mConfirmPassTET.setText("");
        mNameTET.setText("");
    }

    @Override
    public void onBackPressed() {
        CreateBackpressDialog() ;    }
}