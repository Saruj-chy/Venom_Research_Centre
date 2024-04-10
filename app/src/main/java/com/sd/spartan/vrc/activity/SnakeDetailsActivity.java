package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.DATETIME;
import static com.sd.spartan.vrc.controller.AppConstants.DATE_OF_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.ENTRY_DATETIME;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_IMAGE;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.HEALTH_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.REG_DATE;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sd.spartan.vrc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SnakeDetailsActivity extends AppCompatActivity {
    private LinearLayout mBtnLayoutLinear ;
    private TextView mGroupNameTextView, mSnakeNameTextView, mDurationTextView ;
    private ImageView imageView;
    private Button mDailyBtn, mMonthlyBtn ;

    private String mSnakeId;
    private String mSnakeName;
    private String mGroupName;
    private String mEntryDateTime;
    private String mGroupImage;
    int timeNumber;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
    long elapsedYears, elapsedMonths, elapsedDays, elapsedHours ;

    FloatingActionButton fab_main, fab_btn_1, fab_btn_2 ;
    LinearLayout linear_fab_1, linear_fab_2;
    boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_details);
        SimpleClass.CheckInActive(TRUE) ;

        initialize() ;

        Toolbar toolbar = findViewById(R.id.appbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle(R.string.app_name) ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        mSnakeId = getIntent().getStringExtra(SNAKE_ID) ;
        mSnakeName = getIntent().getStringExtra(SNAKE_NAME) ;
        mGroupName = getIntent().getStringExtra(GROUP_NAME) ;
        String mDateOfCollection = getIntent().getStringExtra(DATE_OF_COLLECTION);
        mEntryDateTime = getIntent().getStringExtra(ENTRY_DATETIME) ;
        mGroupImage = getIntent().getStringExtra(GROUP_IMAGE) ;
        String mHealthStatus = getIntent().getStringExtra(HEALTH_STATUS);
        String mRegDate = getIntent().getStringExtra(REG_DATE);
        if(mHealthStatus.equals("Dead")){
            setUpDateTime(mDateOfCollection, mRegDate, SimpleClass.GetCurrentTime(), "");
            mBtnLayoutLinear.setVisibility(View.GONE);
        }else{
            setUpDateTime(mDateOfCollection, SimpleClass.GetCurrentDate(), SimpleClass.GetCurrentTime(), "ago");
            mBtnLayoutLinear.setVisibility(View.VISIBLE);

        }

        mGroupNameTextView.setText(mGroupName);
        mSnakeNameTextView.setText(mSnakeName);
        AppImageLoader.loadImageInView(mGroupImage, R.drawable.logo_vrc, (ImageView)imageView);


        mDailyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegularActivity.class);
            intent.putExtra(SNAKE_ID, mSnakeId );
            intent.putExtra(SNAKE_NAME, mSnakeName );
            intent.putExtra(GROUP_NAME, mGroupName );
            startActivity(intent);
        });

        mMonthlyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OccasionalActivity.class);
            intent.putExtra(SNAKE_ID, mSnakeId );
            intent.putExtra(SNAKE_NAME, mSnakeName );
            intent.putExtra(GROUP_NAME, mGroupName );
            intent.putExtra(GROUP_IMAGE, mGroupImage );
            intent.putExtra(DATETIME, mEntryDateTime );
            startActivity(intent);
        });
    }


    @SuppressLint("DefaultLocale")
    public void setUpDateTime(String cartdatetime, String currentDate, String currentTime, String ago) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = simpleDateFormat.parse(cartdatetime);
            endDate = simpleDateTimeFormat.parse(currentDate+" "+currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert startDate != null;
        assert endDate != null;
        CalculateTimer(startDate, endDate);

        switch (timeNumber){
            case 0:
                mDurationTextView.setText(String.format("Few hours "+ ago));
                break;
            case 1:
                mDurationTextView.setText(String.format(elapsedDays+" days "+ago));
                break;
            case 2:
                mDurationTextView.setText(String.format(elapsedMonths +" months "+ elapsedDays+" days "+ ago));
                break;
            case 3:
                mDurationTextView.setText(String.format("few minutes "+ ago));
                break;
            case 4:
                mDurationTextView.setText(String.format(elapsedYears + " years "+ elapsedMonths +" months "+ elapsedDays + " days "+ ago));
                break;
            default:
                break;
        }
    }
    public void CalculateTimer ( Date startDate, Date endDate){
        long different = endDate.getTime() - startDate.getTime();
        long hoursInMilli = 1000 * 60 * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthInMilli = daysInMilli * 30;
        long yearsInMilli = monthInMilli * 12;

        if(different>yearsInMilli){
            timeNumber = 4;
        }else if(different>monthInMilli){
            timeNumber = 2;
        }
        else if(different>daysInMilli){
            timeNumber = 1;
        }else if(different>hoursInMilli){
            timeNumber = 0 ;
        } else{
            timeNumber = 3 ;
        }

        elapsedYears = different / yearsInMilli;
        different = different % yearsInMilli;

        elapsedMonths = different / monthInMilli;
        different = different % monthInMilli;

        elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        elapsedHours =(different / hoursInMilli);
    }
    public void OnFabMainClick(View view) {
        if (!isFABOpen) {
            showFABMenu();
        } else {
            closeFABMenu();
        }
    }
    private void showFABMenu() {
        isFABOpen = true;
        linear_fab_1.setVisibility(View.VISIBLE);
        linear_fab_2.setVisibility(View.VISIBLE);
        fab_main.animate().rotationBy(90);
        linear_fab_1.animate().translationY(-getResources().getDimension(R.dimen.dimen_20));
        linear_fab_2.animate().translationY(-getResources().getDimension(R.dimen.dimen_70));
    }
    private void closeFABMenu() {
        isFABOpen = false;
        fab_main.animate().rotation(0);
        linear_fab_1.animate().translationY(0);
        linear_fab_2.animate().translationY(0);
        linear_fab_1.setVisibility(View.GONE);
        linear_fab_2.setVisibility(View.GONE);
    }

    private void initialize() {
        mBtnLayoutLinear = findViewById(R.id.linear_btn_layout) ;
        mGroupNameTextView = findViewById(R.id.text_group_name_details) ;
        mSnakeNameTextView = findViewById(R.id.text_snake_name_details) ;
        mDurationTextView = findViewById(R.id.text_snake_duration_details) ;
        imageView = findViewById(R.id.image_details) ;
        mDailyBtn = findViewById(R.id.btn_daily) ;
        mMonthlyBtn = findViewById(R.id.btn_monthly) ;

        linear_fab_1 =  findViewById(R.id.linear_fab_1);
        linear_fab_2 =  findViewById(R.id.linear_fab_2);
        fab_main =  findViewById(R.id.fab_main);
        fab_btn_1 =  findViewById(R.id.fab_btn_1);
        fab_btn_2 =  findViewById(R.id.fab_btn_2);
    }
    public void OnRegFabClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RegularPrintActivity.class);
        intent.putExtra("snake_id", mSnakeId );
        intent.putExtra("snake_name", mSnakeName );
        intent.putExtra("group_name", mGroupName );
        startActivity(intent);
    }

    public void OnOccFabClick(View view) {
        Intent intent = new Intent(getApplicationContext(), OccPrintActivity.class);
        intent.putExtra("snake_id", mSnakeId );
        intent.putExtra("snake_name", mSnakeName );
        intent.putExtra("group_name", mGroupName );
        intent.putExtra("group_image", mGroupImage );
        intent.putExtra("datetime", mEntryDateTime );
        startActivity(intent);
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