package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.DATA;
import static com.sd.spartan.vrc.controller.AppConstants.DATE_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.ERROR;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE_STR;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.HOUR;
import static com.sd.spartan.vrc.controller.AppConstants.ID;
import static com.sd.spartan.vrc.controller.AppConstants.MSG;
import static com.sd.spartan.vrc.controller.AppConstants.NOTICE_SEC_ID;
import static com.sd.spartan.vrc.controller.AppConstants.NOTICE_STS_ID;
import static com.sd.spartan.vrc.controller.AppConstants.NO_DATA_AVAILABLE;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.adapter.NoticeRV;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.BuilderClass;
import com.sd.spartan.vrc.model.BuilderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {
    private TextView noticeTV, alertTV, completeTV;
    private TextView refreshTV ;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private ProgressBar progressBar ;
    private RecyclerView mNoticeRV;
    private List<BuilderModel> mNoticeList;

    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 2000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        SimpleClass.CheckInActive(TRUE);

        Toolbar toolbar = findViewById(R.id.appbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle("Notice") ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Initialize();

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.DKGRAY);
        swipeRefreshLayout.setOnRefreshListener(this::setAutoRefresh);
    }
    private void loadNoticeList() {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.STATE, "1") ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(DATE_TIME, SimpleClass.GetCurrentDateTime()) ;

        AppController.getAppController().getAppNetworkController().makeRequest(Config.NOTICE, response -> {
            swipeRefreshLayout.setRefreshing(false);
            cancelAutoRefresh() ;
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                    mNoticeList.clear();
                    mNoticeRV.setVisibility(View.VISIBLE);
                    refreshTV.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    JSONArray noticeArray = object.getJSONArray(DATA) ;
                    for(int i=0; i<noticeArray.length(); i++){
                        JSONObject jsonObject = noticeArray.getJSONObject(i);

                        mNoticeList.add(
                                new BuilderClass()
                                        .setId(jsonObject.getString(ID))
                                        .setSnake_id(jsonObject.getString(SNAKE_ID))
                                        .setSnake_name(jsonObject.getString(SNAKE_NAME))
                                        .setGroup_name(jsonObject.getString(GROUP_NAME))
                                        .setNotice_sec_id(jsonObject.getString(NOTICE_SEC_ID))
                                        .setNotice_sts_id(jsonObject.getString(NOTICE_STS_ID))
                                        .setMsg(jsonObject.getString(MSG))
                                        .setDate_time(jsonObject.getString(DATE_TIME))
                                        .setHour(jsonObject.getString(HOUR))
                                        .build()
                        ) ;
                    }
                    LoadAdapter("1");

                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(NoticeActivity.this, NO_DATA_AVAILABLE, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                mNoticeRV.setVisibility(View.GONE);
                refreshTV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NoticeActivity.this, Config.NET_CONN, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            mNoticeRV.setVisibility(View.GONE);
            refreshTV.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(NoticeActivity.this, Config.NET_CONN, Toast.LENGTH_SHORT).show();
        }, map );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void LoadAdapter(String notice_sts_id) {
        NoticeRV mNoticeAdapter = new NoticeRV(NoticeActivity.this, mNoticeList, notice_sts_id);
        mNoticeRV.setAdapter(mNoticeAdapter);
        GridLayoutManager mGridManager = new GridLayoutManager(NoticeActivity.this, 1, GridLayoutManager.VERTICAL, false);
        mNoticeRV.setLayoutManager(mGridManager);
        mNoticeAdapter.notifyDataSetChanged();
    }


    public void setAutoRefresh(){
        if(remainingRefreshTime<=0){
            if(countDownTimer!= null){
                countDownTimer.cancel();
            }
            return;
        }
        if(countDownTimer == null){
            countDownTimer = new CountDownTimer(remainingRefreshTime, 2500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mNoticeRV.setVisibility(View.GONE);
                }

                @Override
                public void onFinish() {

                    loadNoticeList();

                }
            };

            countDownTimer.start() ;
        }
    }
    private void cancelAutoRefresh(){
        if(countDownTimer!= null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }


    private void Initialize() {
        noticeTV = findViewById(R.id.text_notice) ;
        alertTV = findViewById(R.id.text_alert) ;
        completeTV = findViewById(R.id.text_complete) ;

        mNoticeRV = findViewById(R.id.notice_recycler) ;
        refreshTV = findViewById(R.id.text_refresh) ;
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_main) ;
        progressBar = findViewById(R.id.progress_main) ;
        mNoticeList = new ArrayList<>() ;
    }


    @Override
    protected void onStart() {
        super.onStart();
        NoticeBtnCheck(1);
        loadNoticeList() ;
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
        AppController.checkAuthourity = false ;
    }

    public void OnNoticeClick(View view) {
        NoticeBtnCheck(1);
        LoadAdapter("1");
    }

    public void OnAlertClick(View view) {
        NoticeBtnCheck(2);
        LoadAdapter( "2");
    }

    public void OnCompleteClick(View view) {
       NoticeBtnCheck(3);
        NoticeBtnCheck(3);
        LoadAdapter( "3");
    }

    private void NoticeBtnCheck(int pos) {
        if(pos==1){
            noticeTV.setTextSize(25);
            alertTV.setTextSize(18);
            completeTV.setTextSize(18);

        } else if(pos==2){
            noticeTV.setTextSize(18);
            alertTV.setTextSize(25);
            completeTV.setTextSize(18);
        }else if(pos == 3){
            noticeTV.setTextSize(18);
            alertTV.setTextSize(18);
            completeTV.setTextSize(25);
        }
    }

}