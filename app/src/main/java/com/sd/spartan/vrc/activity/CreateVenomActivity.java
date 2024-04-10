package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.AGE_DURING_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.CHIN_SHEILD_A;
import static com.sd.spartan.vrc.controller.AppConstants.CHIN_SHEILD_B;
import static com.sd.spartan.vrc.controller.AppConstants.DATE;
import static com.sd.spartan.vrc.controller.AppConstants.DATE_OF_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.DATE_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.ERROR;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE_STR;
import static com.sd.spartan.vrc.controller.AppConstants.FRONTAL;
import static com.sd.spartan.vrc.controller.AppConstants.GOOGLE_MAP;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_ID;
import static com.sd.spartan.vrc.controller.AppConstants.INTERNASAL;
import static com.sd.spartan.vrc.controller.AppConstants.LAT;
import static com.sd.spartan.vrc.controller.AppConstants.LONG;
import static com.sd.spartan.vrc.controller.AppConstants.LOREAL;
import static com.sd.spartan.vrc.controller.AppConstants.LOWER_LABIAL;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_USERNAME;
import static com.sd.spartan.vrc.controller.AppConstants.MENTAL;
import static com.sd.spartan.vrc.controller.AppConstants.NASAL;
import static com.sd.spartan.vrc.controller.AppConstants.NEW_MORPHOMETRY;
import static com.sd.spartan.vrc.controller.AppConstants.NOTES;
import static com.sd.spartan.vrc.controller.AppConstants.PARIETAL;
import static com.sd.spartan.vrc.controller.AppConstants.PLACE_OF_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.POST_OCULAR;
import static com.sd.spartan.vrc.controller.AppConstants.PREFRONTAL;
import static com.sd.spartan.vrc.controller.AppConstants.PRE_OCULAR;
import static com.sd.spartan.vrc.controller.AppConstants.ROSTRAL;
import static com.sd.spartan.vrc.controller.AppConstants.SEX;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.STATE;
import static com.sd.spartan.vrc.controller.AppConstants.SUB_OCULAR;
import static com.sd.spartan.vrc.controller.AppConstants.SUPRA_OCULAR;
import static com.sd.spartan.vrc.controller.AppConstants.TEMPORAL_F;
import static com.sd.spartan.vrc.controller.AppConstants.TEMPORAL_S;
import static com.sd.spartan.vrc.controller.AppConstants.TIME;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;
import static com.sd.spartan.vrc.controller.AppConstants.UPPER_LABIAL;
import static com.sd.spartan.vrc.controller.SimpleClass.dateTimeIntCheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.textfield.TextInputEditText;
import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.PopupSureCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CreateVenomActivity extends AppCompatActivity {
    private RelativeLayout mMorphometryBelowRL, mHeadscalationBelowRL ;
    private ImageView mMorphomtryDownImage, mMorphomtryUpImage, mHeadscalationUpImage, mHeadscalationDownImage;
    private TextView mGrpNameTextView ;
    private Spinner mSexSpin;
    private TextInputEditText mSnakeNameInputEdit, mAgeInputEdit, mDateInputEdit, mPlaceInputEdit, mNoteInputEdit;
    private TextInputEditText mChinSheildAInputEdit, mChinSheildBInputEdit, mParietalInputEdit, mFrontalInputEdit, mPrefrontalInputEdit, mLorcalInputEdit;
    private TextInputEditText mMentalInputEdit, mNasalInputEdit, mInterNasalInputEdit, mRostralInputEdit, mTempFInputEdit, mTempSInputEdit ;
    private TextInputEditText mUpperLabialInputEdit, mLowerLabialInputEdit, mSupraOcularInputEdit, mSubOcularInputEdit, mPreOcularInputEdit, mPostOcularInputEdit;

    private String mSnakeName;
    private String mSex;
    private String mAge;
    private String mDate;
    private String mPlace;
    private String mNote;
    private String mChinSheildA;
    private String mChinSheildB;
    private String mParietal;
    private String mFrontal;
    private String mPrefrontal;
    private String mLorcal;
    private String mMental;
    private String mNasal;
    private String mInternasal;
    private String mRostral;
    private String mTempF;
    private String mTempS;
    private String mUpperLabial;
    private String mLowerLabial;
    private String mSupraOcular;
    private String mSubOcular;
    private String mPreOcular;
    private String mPostOcular ;
    private boolean mDateError = false ;


    ArrayList<String> arrayList ;
    LocationController locationController ;
    public static String placeName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_venom);
        SimpleClass.CheckInActive(TRUE);

        Toolbar toolbar = findViewById(R.id.appbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle(NEW_MORPHOMETRY) ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        initialize() ;
        String state = getIntent().getStringExtra(STATE).trim();
        if(!state.equalsIgnoreCase(GOOGLE_MAP)){
            locationController = new LocationController(this) ;
            locationController.getLastLocation();
        }

        arrayList = new ArrayList<>();
        mGrpNameTextView.setText(SnakeListActivity.group_name);
    }

    private void insertCreateVenom() {
        AppController.getAppController().getAppNetworkController()
                .makeRequest(Config.INSERT_NEW_SNAKE_ENTRY, response -> {
                    PopupSureCheck.dialog.cancel();
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                            ClearAllTextInput();
                            onBackPressed();
                        }
                        AppController.getAppController().getInAppNotifier().showToast(object.getString(STATE));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                }, hashmap() );
    }
    private HashMap<String, String> hashmap(){
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(MEM_ID, AppController.mem_id) ;
        map.put(MEM_USERNAME, AppController.mem_username) ;
        map.put(GROUP_ID, SnakeListActivity.group_id) ;
        map.put(SNAKE_NAME, mSnakeName) ;
        map.put(SEX, mSex) ;
        map.put(AGE_DURING_COLLECTION, mAge) ;
        map.put(DATE_OF_COLLECTION, mDate) ;
        map.put(PLACE_OF_COLLECTION, mPlace) ;
        map.put(NOTES, mNote) ;
        map.put(CHIN_SHEILD_A, mChinSheildA) ;
        map.put(CHIN_SHEILD_B, mChinSheildB) ;
        map.put(PARIETAL, mParietal) ;
        map.put(FRONTAL, mFrontal) ;
        map.put(PREFRONTAL, mPrefrontal) ;
        map.put(LOREAL, mLorcal) ;
        map.put(MENTAL, mMental) ;
        map.put(NASAL, mNasal) ;
        map.put(INTERNASAL, mInternasal) ;
        map.put(ROSTRAL, mRostral) ;
        map.put(TEMPORAL_F, mTempF) ;
        map.put(TEMPORAL_S, mTempS) ;
        map.put(UPPER_LABIAL, mUpperLabial) ;
        map.put(LOWER_LABIAL, mLowerLabial) ;
        map.put(SUPRA_OCULAR, mSupraOcular) ;
        map.put(SUB_OCULAR, mSubOcular) ;
        map.put(PRE_OCULAR, mPreOcular) ;
        map.put(POST_OCULAR, mPostOcular) ;
        map.put(LAT, LocationController.latitude+"") ;
        map.put(LONG, LocationController.longitude+"") ;
        map.put(DATE, SimpleClass.GetCurrentDate() ) ;
        map.put(TIME, SimpleClass.GetCurrentTime() ) ;
        map.put(DATE_TIME, SimpleClass.GetCurrentDateTime() ) ;
        return  map;
    }
    private void ClearAllTextInput(){
        mSnakeNameInputEdit.setText("");
        mAgeInputEdit.setText("");
        mDateInputEdit.setText("");
        mPlaceInputEdit.setText("");
        mNoteInputEdit.setText("");
        mChinSheildAInputEdit.setText("");
        mChinSheildBInputEdit.setText("");
        mParietalInputEdit.setText("");
        mFrontalInputEdit.setText("");
        mPrefrontalInputEdit.setText("");
        mLorcalInputEdit.setText("");
        mMentalInputEdit.setText("");
        mNasalInputEdit.setText("");
        mInterNasalInputEdit.setText("");
        mRostralInputEdit.setText("");
        mTempFInputEdit.setText("");
        mTempSInputEdit.setText("");
        mUpperLabialInputEdit.setText("");
        mLowerLabialInputEdit.setText("");
        mSupraOcularInputEdit.setText("");
        mSubOcularInputEdit.setText("");
        mPreOcularInputEdit.setText("");
        mPostOcularInputEdit.setText("");
    }


    private void initialize() {
        mMorphometryBelowRL = findViewById(R.id.relative_morphometry_below);
        mHeadscalationBelowRL = findViewById(R.id.relative_headscalation_below);
        mMorphomtryUpImage = findViewById(R.id.image_arrow_up_morphometry);
        mMorphomtryDownImage = findViewById(R.id.image_arrow_down_morphometry);
        mHeadscalationUpImage = findViewById(R.id.image_arrow_up_headscalation);
        mHeadscalationDownImage = findViewById(R.id.image_arrow_down_headscalation);

        mGrpNameTextView = findViewById(R.id.text_group_name);
        mSexSpin = findViewById(R.id.spinner_sex);
        mSnakeNameInputEdit = findViewById(R.id.edit_snake_name);
        mAgeInputEdit = findViewById(R.id.edit_age_collection);
        mDateInputEdit = findViewById(R.id.edit_date_collection);
        mPlaceInputEdit = findViewById(R.id.edit_place_collection);
        mNoteInputEdit = findViewById(R.id.edit_notes);
        mChinSheildAInputEdit = findViewById(R.id.edit_chin_sheid_A);
        mChinSheildBInputEdit = findViewById(R.id.edit_chin_sheid_B);
        mParietalInputEdit = findViewById(R.id.edit_parietal);
        mFrontalInputEdit = findViewById(R.id.edit_frontal);
        mPrefrontalInputEdit = findViewById(R.id.edit_prefrontal);
        mLorcalInputEdit = findViewById(R.id.edit_lorcal);
        mMentalInputEdit = findViewById(R.id.edit_mental);
        mNasalInputEdit = findViewById(R.id.edit_nasal);
        mInterNasalInputEdit = findViewById(R.id.edit_internasal);
        mRostralInputEdit = findViewById(R.id.edit_rostral);
        mTempFInputEdit = findViewById(R.id.edit_temporal_f);
        mTempSInputEdit = findViewById(R.id.edit_temporal_s);
        mUpperLabialInputEdit = findViewById(R.id.edit_upper_labial);
        mLowerLabialInputEdit = findViewById(R.id.edit_lower_labial);
        mSupraOcularInputEdit = findViewById(R.id.edit_supra_ocular);
        mSubOcularInputEdit = findViewById(R.id.edit_sub_ocular);
        mPreOcularInputEdit = findViewById(R.id.edit_pre_ocular);
        mPostOcularInputEdit = findViewById(R.id.edit_post_ocular);
    }

    public void onDownMorphoClick(View v){
        mMorphometryBelowRL.setVisibility(View.GONE);
        mMorphomtryDownImage.setVisibility(View.GONE);
        mMorphomtryUpImage.setVisibility(View.VISIBLE);
    }
    public void onUpMorphoClick(View v){
        mMorphometryBelowRL.setVisibility(View.VISIBLE);
        mMorphomtryUpImage.setVisibility(View.GONE);
        mMorphomtryDownImage.setVisibility(View.VISIBLE);
    }
    public void onDownHeadClick(View v){
        mHeadscalationBelowRL.setVisibility(View.GONE);
        mHeadscalationDownImage.setVisibility(View.GONE);
        mHeadscalationUpImage.setVisibility(View.VISIBLE);
    }
    public void onUpHeadClick(View v){
        mHeadscalationBelowRL.setVisibility(View.VISIBLE);
        mHeadscalationUpImage.setVisibility(View.GONE);
        mHeadscalationDownImage.setVisibility(View.VISIBLE);
    }

    public void onNewSnakeCreate(View view) {
        getAllTextInput() ;
        if(mSnakeName.equalsIgnoreCase("") || mSex.equalsIgnoreCase("") || mAge.equalsIgnoreCase("") ||
                mDate.equalsIgnoreCase("") || mPlace.equalsIgnoreCase("")  ){
            Toast.makeText(this, "Please fill all feilds", Toast.LENGTH_SHORT).show();
        }else if(!LocationController.locationState ){
            Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show();
        }else if(!(LocationController.latitude!=0 && LocationController.longitude!=0)){
            locationController.getLastLocation();
            Toast.makeText(this, "Please click again", Toast.LENGTH_SHORT).show();
        }else if(mDateError){
            Toast.makeText(this, "Please correct your date ", Toast.LENGTH_SHORT).show();
        } else{
            PopupSureCheck.setTitle("New Snake Data Entry");
            PopupSureCheck.setDetail("Are you sure you want to entry all data?");
            PopupSureCheck.CreatePopup(this, view1 -> {
                PopupSureCheck.ll.setVisibility(View.GONE);
                PopupSureCheck.progress.setVisibility(View.VISIBLE);
                insertCreateVenom() ;
            }, error -> PopupSureCheck.dialog.cancel());
        }

    }
    private void getAllTextInput() {
        mSnakeName = Objects.requireNonNull(mSnakeNameInputEdit.getText()).toString().trim() ;
        mSex = mSexSpin.getSelectedItem().toString() ;
        mAge = Objects.requireNonNull(mAgeInputEdit.getText()).toString().trim() ;
        mDate = Objects.requireNonNull(mDateInputEdit.getText()).toString().trim() ;
        mPlace = Objects.requireNonNull(mPlaceInputEdit.getText()).toString().trim() ;
        mNote = Objects.requireNonNull(mNoteInputEdit.getText()).toString().trim() ;
        mChinSheildA = Objects.requireNonNull(mChinSheildAInputEdit.getText()).toString().trim() ;
        mChinSheildB = Objects.requireNonNull(mChinSheildBInputEdit.getText()).toString().trim() ;
        mParietal = Objects.requireNonNull(mParietalInputEdit.getText()).toString().trim() ;
        mFrontal = Objects.requireNonNull(mFrontalInputEdit.getText()).toString().trim() ;
        mPrefrontal = Objects.requireNonNull(mPrefrontalInputEdit.getText()).toString().trim() ;
        mLorcal = Objects.requireNonNull(mLorcalInputEdit.getText()).toString().trim() ;
        mMental = Objects.requireNonNull(mMentalInputEdit.getText()).toString().trim() ;
        mNasal = Objects.requireNonNull(mNasalInputEdit.getText()).toString().trim() ;
        mInternasal = Objects.requireNonNull(mInterNasalInputEdit.getText()).toString().trim() ;
        mRostral = Objects.requireNonNull(mRostralInputEdit.getText()).toString().trim() ;
        mTempF = Objects.requireNonNull(mTempFInputEdit.getText()).toString().trim() ;
        mTempS = Objects.requireNonNull(mTempSInputEdit.getText()).toString().trim() ;
        mUpperLabial = Objects.requireNonNull(mUpperLabialInputEdit.getText()).toString().trim() ;
        mLowerLabial = Objects.requireNonNull(mLowerLabialInputEdit.getText()).toString().trim() ;
        mSupraOcular = Objects.requireNonNull(mSupraOcularInputEdit.getText()).toString().trim() ;
        mSubOcular = Objects.requireNonNull(mSubOcularInputEdit.getText()).toString().trim() ;
        mPreOcular = Objects.requireNonNull(mPreOcularInputEdit.getText()).toString().trim() ;
        mPostOcular = Objects.requireNonNull(mPostOcularInputEdit.getText()).toString().trim() ;

        String[] dateSplit = mDate.split("-");
        mDateError = true;
        if(dateSplit.length==3){
            if(dateSplit[0].length()==4 && (dateTimeIntCheck(dateSplit[1])>0 && dateTimeIntCheck(dateSplit[1])<13) &&  (dateTimeIntCheck(dateSplit[2])>0 && dateTimeIntCheck(dateSplit[2])<32) ){
                mDateError = false;
            }
        }

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

    public void OnPressMap(View view) {
        Intent intent = new Intent();
        intent.setClass(CreateVenomActivity.this, GoogleMapActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 6969);
        setResult(60);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPlaceInputEdit.setText(placeName);

        Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
    }

}