package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.AGE;
import static com.sd.spartan.vrc.controller.AppConstants.ANAL;
import static com.sd.spartan.vrc.controller.AppConstants.BODY;
import static com.sd.spartan.vrc.controller.AppConstants.DATA;
import static com.sd.spartan.vrc.controller.AppConstants.DATE;
import static com.sd.spartan.vrc.controller.AppConstants.DATE_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.DORSAL;
import static com.sd.spartan.vrc.controller.AppConstants.ERROR;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE_STR;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.HEAD;
import static com.sd.spartan.vrc.controller.AppConstants.INFO_TYPE;
import static com.sd.spartan.vrc.controller.AppConstants.LAT;
import static com.sd.spartan.vrc.controller.AppConstants.LONG;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_USERNAME;
import static com.sd.spartan.vrc.controller.AppConstants.MSG;
import static com.sd.spartan.vrc.controller.AppConstants.NOTES;
import static com.sd.spartan.vrc.controller.AppConstants.NOT_FILL_FIELDS;
import static com.sd.spartan.vrc.controller.AppConstants.OCC_DATA;
import static com.sd.spartan.vrc.controller.AppConstants.OCC_SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.OCC_WORK;
import static com.sd.spartan.vrc.controller.AppConstants.PLZ_TURN_LOC;
import static com.sd.spartan.vrc.controller.AppConstants.PLZ_WAIT;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.SUB_CAUDAL;
import static com.sd.spartan.vrc.controller.AppConstants.TAIL;
import static com.sd.spartan.vrc.controller.AppConstants.TIME;
import static com.sd.spartan.vrc.controller.AppConstants.TOTAL;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;
import static com.sd.spartan.vrc.controller.AppConstants.UPDATE;
import static com.sd.spartan.vrc.controller.AppConstants.VENTRAL;
import static com.sd.spartan.vrc.controller.AppConstants.WANT_INSERT_DATA;
import static com.sd.spartan.vrc.controller.AppConstants.WANT_UPDATE_DATA;
import static com.sd.spartan.vrc.controller.AppConstants.WEIGHT_GM;
import static com.sd.spartan.vrc.controller.AppConstants.YYYY_MM_DD;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.PopupSureCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class OccasionalActivity extends AppCompatActivity {
    private RelativeLayout lenRL, scalesRL;
    private ImageView lenUpImg, lenDownImg, scaleUpImg, scaleDownImg ;
    private TextView mGroupNameTextView;
    private TextView mSnakeNameTextView;
    private TextInputEditText mAgeMonthInputEdit, mWeightInputEdit, mHeadInputEdit, mBodyInputEdit,mTailInputEdit,
            mTotalInputEdit, mDorsalInputEdit, mVentralInputEdit, mAnalInputEdit, mSubCaudalInputEdit, mNotesInputEdit;
    private Button mSavedBtn ;
    private Spinner ageSpinner ;

    private String mOccSnakeId;
    private String mSnakeId;
    private String mAgeMonth, mWeight, mHead, mBody, mTail, mTotal, mDorsal, mVentral, mAnal, mSubCaudal, mNotes;


    private double headNumb, bodyNumb, tailNumb ;

    LocationController locationController ;

    //=====   floating button
    FloatingActionButton mFabMain, mFabBtn1, mFabBtn2;
    LinearLayout mLinearFab1, mLinearFab2;
    boolean isFABOpen = false, insertBtn = true;
    private int year, month, day ;
    private String  dateSelect ="" ;

    private ProgressDialog loading ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occasional);
        SimpleClass.CheckInActive(TRUE);

        Initialize() ;
        Toolbar toolbar = findViewById(R.id.appbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        setTitle(OCC_WORK) ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String mGroupName = getIntent().getStringExtra(GROUP_NAME);
        mSnakeId = getIntent().getStringExtra(SNAKE_ID);
        String mSnakeName = getIntent().getStringExtra(SNAKE_NAME);

        mGroupNameTextView.setText(mGroupName);
        mSnakeNameTextView.setText(mSnakeName);

        Calculation() ;

        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    mAgeMonth = (String) parent.getItemAtPosition(position) ;
                }else{
                    mAgeMonth = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        scaleUpImg.setOnClickListener(view -> UpDownShow(scalesRL, scaleUpImg, scaleDownImg, true));
        scaleDownImg.setOnClickListener(view -> UpDownShow(scalesRL, scaleUpImg, scaleDownImg, false));

        lenDownImg.setOnClickListener(view -> UpDownShow(lenRL, lenUpImg, lenDownImg, false));
        lenUpImg.setOnClickListener(view -> UpDownShow(lenRL, lenUpImg, lenDownImg, true));
        mSavedBtn.setOnClickListener(view -> {
            closeFABMenu();
            getAllTextInput() ;
            locationController.getLastLocation();

            if(mWeight.equalsIgnoreCase("") ||mHead.equalsIgnoreCase("") ||mBody.equalsIgnoreCase("") ||
                    mTail.equalsIgnoreCase("") ||mTotal.equalsIgnoreCase("") ||mDorsal.equalsIgnoreCase("") ||mVentral.equalsIgnoreCase("") ||
                    mAnal.equalsIgnoreCase("") ||mSubCaudal.equalsIgnoreCase("") || mNotes.equalsIgnoreCase("") ){
                Toast.makeText(OccasionalActivity.this, NOT_FILL_FIELDS, Toast.LENGTH_SHORT).show();

            }

            if( LocationController.latitude==0 || LocationController.longitude==0 ){
                locationController.getLastLocation();
                Toast.makeText(OccasionalActivity.this, PLZ_TURN_LOC, Toast.LENGTH_SHORT).show();
            } else{
                PopupSureCheck.setTitle(OCC_DATA);
                if(insertBtn){
                    PopupSureCheck.setDetail(WANT_INSERT_DATA);
                }else{
                    PopupSureCheck.setDetail(WANT_UPDATE_DATA);
                }
                PopupSureCheck.CreatePopup(OccasionalActivity.this, view12 -> {
                    PopupSureCheck.ll.setVisibility(View.GONE);
                    PopupSureCheck.progress.setVisibility(View.VISIBLE);
                    if(insertBtn){
                        InsertOccasionalData(Config.INSERT_OCCASIONAL_DATA) ;
                    }else{
                        InsertOccasionalData(Config.UPDATE_OCCASIONAL_DATA) ;
                    }
                }, view1 -> PopupSureCheck.dialog.cancel());

            }
        });
    }

    private void Calculation() {
        mBodyInputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equalsIgnoreCase("")){
                    bodyNumb = Double.parseDouble(String.valueOf(s)) ;
                    Total(headNumb, bodyNumb, tailNumb);
                }else{
                    bodyNumb = 0 ;
                    Total(headNumb, bodyNumb, tailNumb);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTailInputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equalsIgnoreCase("")){
                    tailNumb = Double.parseDouble(String.valueOf(s)) ;
                    Total(headNumb, bodyNumb, tailNumb);
                }else{
                    tailNumb = 0 ;
                    Total(headNumb, bodyNumb, tailNumb);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    @SuppressLint("DefaultLocale")
    private void Total(double head, double body, double tail){
        double result = head + body+ tail ;
        mTotalInputEdit.setText(String.format("%.2f", result));
    }

    private void InsertOccasionalData(String URL) {
        AppController.getAppController().getAppNetworkController().makeRequest(URL, response -> {
            PopupSureCheck.dialog.cancel();
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                    onBackPressed();
                }
                AppController.getAppController().getInAppNotifier().showToast(object.getString(MSG));


            } catch (JSONException e) {
                PopupSureCheck.dialog.cancel();
            }
        }, error -> PopupSureCheck.dialog.cancel(), hashmap() );
    }
    private HashMap<String, String> hashmap(){
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(SNAKE_ID, mSnakeId) ;
        map.put(AGE, mAgeMonth) ;
        map.put(WEIGHT_GM, mWeight) ;
        map.put(HEAD, mHead) ;
        map.put(BODY, mBody) ;
        map.put(TAIL, mTail) ;
        map.put(TOTAL, mTotal) ;
        map.put(DORSAL, mDorsal) ;
        map.put(VENTRAL, mVentral) ;
        map.put(ANAL, mAnal) ;
        map.put(SUB_CAUDAL, mSubCaudal) ;
        map.put(NOTES, mNotes) ;

        map.put(MEM_ID, AppController.mem_id) ;
        map.put(MEM_USERNAME, AppController.mem_username) ;
        map.put(LAT, LocationController.latitude+"") ;
        map.put(LONG, LocationController.longitude+"" ) ;
        map.put(DATE, SimpleClass.GetCurrentDate() ) ;
        map.put(TIME, SimpleClass.GetCurrentTime() ) ;
        map.put(DATE_TIME, SimpleClass.GetCurrentDateTime() ) ;

        if(!insertBtn){
            map.put(OCC_SNAKE_ID, mOccSnakeId) ;
        }

        return  map;
    }

    private void ClearAllTextInput(){
        mAgeMonthInputEdit.setText("");
        mWeightInputEdit.setText("");
        mHeadInputEdit.setText("");
        mBodyInputEdit.setText("");
        mTailInputEdit.setText("");
        mTotalInputEdit.setText("");
        mDorsalInputEdit.setText("");
        mVentralInputEdit.setText("");
        mAnalInputEdit.setText("");
        mSubCaudalInputEdit.setText("");
        mNotesInputEdit.setText("");

        String[] ageArray = getResources().getStringArray(R.array.age) ;
        getSpinnerValueCheck( ageArray[0], R.array.age, ageSpinner);
    }


    private void Initialize() {
        lenRL = findViewById(R.id.relative_len);
        scalesRL = findViewById(R.id.relative_scales);
        lenDownImg = findViewById(R.id.image_down_len);
        lenUpImg = findViewById(R.id.image_up_len);
        scaleDownImg = findViewById(R.id.image_down_scales);
        scaleUpImg = findViewById(R.id.image_up_scale);

        mGroupNameTextView = findViewById(R.id.text_group_name_monthly);
        mSnakeNameTextView = findViewById(R.id.text_snake_name_monthly);
        mAgeMonthInputEdit = findViewById(R.id.edit_age_monthly);
        mWeightInputEdit = findViewById(R.id.edit_weight_monthly);
        mHeadInputEdit = findViewById(R.id.edit_head);
        mBodyInputEdit = findViewById(R.id.edit_body);
        mTailInputEdit = findViewById(R.id.edit_tail);
        mTotalInputEdit = findViewById(R.id.edit_total);
        mDorsalInputEdit = findViewById(R.id.edit_dorsal);
        mVentralInputEdit = findViewById(R.id.edit_ventral);
        mAnalInputEdit = findViewById(R.id.edit_anal);
        mSubCaudalInputEdit = findViewById(R.id.edit_sub_caudal);
        mNotesInputEdit = findViewById(R.id.edit_remarks);
        mSavedBtn = findViewById(R.id.btn_monthly_saved);
        ageSpinner = findViewById(R.id.spinner_age);


        mLinearFab1 =  findViewById(R.id.linear_fab_1);
        mLinearFab2 =  findViewById(R.id.linear_fab_2);
        mFabMain =  findViewById(R.id.fab_main);
        mFabBtn1 =  findViewById(R.id.fab_btn_1);
        mFabBtn2 =  findViewById(R.id.fab_btn_2);

        locationController = new LocationController(this) ;

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        loading = new ProgressDialog(this);

    }




    private void getAllTextInput() {
        mWeight = Objects.requireNonNull(mWeightInputEdit.getText()).toString().trim() ;
        mHead = Objects.requireNonNull(mHeadInputEdit.getText()).toString().trim() ;
        mBody = Objects.requireNonNull(mBodyInputEdit.getText()).toString().trim() ;
        mTail = Objects.requireNonNull(mTailInputEdit.getText()).toString().trim() ;
        mTotal = Objects.requireNonNull(mTotalInputEdit.getText()).toString().trim() ;
        mDorsal = Objects.requireNonNull(mDorsalInputEdit.getText()).toString().trim() ;
        mVentral = Objects.requireNonNull(mVentralInputEdit.getText()).toString().trim() ;
        mAnal = Objects.requireNonNull(mAnalInputEdit.getText()).toString().trim() ;
        mSubCaudal = Objects.requireNonNull(mSubCaudalInputEdit.getText()).toString().trim() ;
        mNotes = Objects.requireNonNull(mNotesInputEdit.getText()).toString().trim() ;
    }


    //fab button click
    public void OnFabMainClick(View view) {
        if (!isFABOpen) {
            showFABMenu();
        } else {
            closeFABMenu();
        }
    }

    private void showFABMenu() {
        isFABOpen = true;
        mLinearFab1.setVisibility(View.VISIBLE);
        mLinearFab2.setVisibility(View.VISIBLE);
        mFabMain.animate().rotationBy(90);
        mLinearFab1.animate().translationY(-getResources().getDimension(R.dimen.dimen_20));
        mLinearFab2.animate().translationY(-getResources().getDimension(R.dimen.dimen_70));
    }
    private void closeFABMenu() {
        isFABOpen = false;
        mFabMain.animate().rotation(0);
        mLinearFab1.animate().translationY(0);
        mLinearFab2.animate().translationY(0);
        mLinearFab1.setVisibility(View.GONE);
        mLinearFab2.setVisibility(View.GONE);
    }
    public void OnUpdateFabClick(View view) {
        OnDateDialog(true) ;

    }
    public void OnAccessFabClick(View view) {
        OnDateDialog(false) ;

    }
    private void OnDateDialog(boolean updateBtn) {
        long maxTime = System.currentTimeMillis();
        long minDay = 30L *24*60*60*1000 ;
        long minTime = System.currentTimeMillis() - (minDay) ;
        DatePickerDialog datepick = new DatePickerDialog(OccasionalActivity.this, (view, year, month, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD, Locale.ENGLISH);
            dateSelect = simpleDateFormat.format(calendar.getTime());

            if(updateBtn){
                RetriveData(21);
            }else{
                RetriveData(22);
            }
        },year,month,day);
        if(updateBtn){
            datepick.getDatePicker().setMinDate(minTime);
        }
        datepick.getDatePicker().setMaxDate(maxTime);

        datepick.show();
    }
    private void RetriveData(int infoType) {
        ClearAllTextInput();
        loading.show();
        loading.setMessage(PLZ_WAIT);
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(SNAKE_ID, mSnakeId) ;
        map.put(MEM_ID, AppController.mem_id) ;
        map.put(DATE, dateSelect) ;
        map.put(INFO_TYPE, infoType+"") ;


        AppController.getAppController().getAppNetworkController()
                .makeRequest(Config.RETRIVE_DATA_BY_DATE, response -> {
                    loading.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                            closeFABMenu();
                            mSavedBtn.setText(UPDATE);
                            insertBtn = false ;
                            JSONObject dataObj = object.getJSONObject(DATA);
                            mOccSnakeId = dataObj.getString(OCC_SNAKE_ID) ;
                            mAgeMonth = dataObj.getString(AGE) ;
                            mWeight = dataObj.getString(WEIGHT_GM) ;
                            mHead = dataObj.getString(HEAD) ;
                            mBody = dataObj.getString(BODY) ;
                            mTail = dataObj.getString(TAIL) ;
                            mTotal = dataObj.getString(TOTAL) ;
                            mDorsal = dataObj.getString(DORSAL) ;
                            mVentral = dataObj.getString(VENTRAL) ;
                            mAnal = dataObj.getString(ANAL) ;
                            mSubCaudal = dataObj.getString(SUB_CAUDAL) ;
                            mNotes = dataObj.getString(NOTES) ;

                            getSpinnerValueCheck(mAgeMonth, R.array.age, ageSpinner);
                            NullCheckEdittext(mWeightInputEdit, mWeight);
                            NullCheckEdittext(mHeadInputEdit, mHead);
                            NullCheckEdittext(mBodyInputEdit, mBody);
                            NullCheckEdittext(mTailInputEdit, mTail);
                            NullCheckEdittext(mTotalInputEdit, mTotal);
                            NullCheckEdittext(mVentralInputEdit, mVentral);
                            NullCheckEdittext(mSubCaudalInputEdit, mSubCaudal);
                            NullCheckEdittext(mNotesInputEdit, mNotes);

                            mDorsalInputEdit.setText(mDorsal);
                            mAnalInputEdit.setText(mAnal);
                        }else{
                            onBackPressed();
                            AppController.getAppController().getInAppNotifier().showToast(object.getString(MSG));
                        }
                    } catch (JSONException ignored) {
                    }
                }, error -> {
                }, map );
    }

    private void NullCheckEdittext(TextInputEditText editText, String value) {
        if(!value.equalsIgnoreCase("0")){
            editText.setText(value);
        }
    }

    private void getSpinnerValueCheck(String name, int array, Spinner spinner) {
        if(name.equalsIgnoreCase("")){
            spinner.setSelection(0);
        }else{
            String[] values = getResources().getStringArray(array);
            for (int i=0; i< values.length; i++){
                if(name.equalsIgnoreCase(values[i])){
                    spinner.setSelection(i);
                    return;
                }else if(i == values.length-1){
                    spinner.setSelection(0);
                }
            }
        }
    }





    private void UpDownShow(RelativeLayout rl, ImageView upImg, ImageView downImg, boolean prior){
        if(prior){
            rl.setVisibility(View.GONE);
            upImg.setVisibility(View.GONE);
            downImg.setVisibility(View.VISIBLE);
        }else{
            rl.setVisibility(View.VISIBLE);
            upImg.setVisibility(View.VISIBLE);
            downImg.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        closeFABMenu();

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
    protected void onDestroy() {
        super.onDestroy();
        closeFABMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SimpleClass.CheckInActive(TRUE);
    }


}