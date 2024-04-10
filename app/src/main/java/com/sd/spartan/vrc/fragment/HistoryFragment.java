package com.sd.spartan.vrc.fragment;

import static com.sd.spartan.vrc.controller.AppConstants.TODAYS_DATA;
import static com.sd.spartan.vrc.controller.AppConstants.YYYY_MM_DD;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.adapter.HistoryListRV;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.BuilderClass;
import com.sd.spartan.vrc.model.BuilderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class HistoryFragment extends Fragment {
    private ConstraintLayout historyConst;
    private TextView refreshTV, dateNameTV, errorMsgTV ;
    private RecyclerView mHistoryRV;
    private ProgressBar progressBar ;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private CalendarView calendarView;

    private HistoryListRV historyListRV;
    private List<BuilderModel> historyModelList;

    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 2000 ;
    private String currentDate, todayDate ;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD, Locale.ENGLISH);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initialize(view) ;

        historyModelList = new ArrayList<>() ;

        historyListRV = new HistoryListRV(getContext(), historyModelList );
        mHistoryRV.setAdapter(historyListRV);

        GridLayoutManager mGridManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        mHistoryRV.setLayoutManager(mGridManager);

        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        LoadHistoryDetails(todayDate);

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.DKGRAY);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            historyModelList.clear();
            setAutoRefresh();
        });

        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            String dateTo = year+"-"+(month + 1)+"-"+ day;
            try{
                Date c_date = simpleDateFormat.parse(dateTo);
                assert c_date != null;
                currentDate = simpleDateFormat.format(c_date) ;
            } catch (ParseException ignored){
            }
            LoadHistoryDetails(currentDate);
            if(todayDate.equals(currentDate)){
                dateNameTV.setText(TODAYS_DATA) ;
            } else{
                dateNameTV.setText(String.format("%s Data", currentDate)) ;
            }
        });
        return view ;
    }

    private void setAutoRefresh(){
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
                    mHistoryRV.setVisibility(View.GONE);
                    historyConst.setVisibility(View.GONE);
                    LoadHistoryDetails(currentDate);
                    if(todayDate.equalsIgnoreCase(currentDate)){
                        dateNameTV.setText(TODAYS_DATA) ;
                    }else{
                        dateNameTV.setText(String.format("%s Data", currentDate)) ;
                    }
                }

                @Override
                public void onFinish() {
                    swipeRefreshLayout.setRefreshing(false);
                    historyConst.setVisibility(View.VISIBLE);
                    cancelAutoRefresh() ;

                }
            };

            countDownTimer.start() ;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void LoadHistoryDetails(String date) {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put("log_entry_datetime", date) ;
        map.put("snake_id", "") ;

        progressBar.setVisibility(View.VISIBLE);
        AppController.getAppController().getAppNetworkController().makeRequest(Config.HISTORY_ALL, response -> {
            try {
                historyModelList.clear();
                progressBar.setVisibility(View.GONE);
                refreshTV.setVisibility(View.GONE);
                JSONObject object = new JSONObject(response);
                if(object.getString("error").equalsIgnoreCase("false")){
                    mHistoryRV.setVisibility(View.VISIBLE);
                    errorMsgTV.setVisibility(View.GONE);

                    JSONArray grpArray = object.getJSONArray("data") ;
                    for(int i=0; i<grpArray.length(); i++){
                        JSONObject mGrpObject = grpArray.getJSONObject(i);
                        historyModelList.add(new BuilderClass()
                                .setMem_id(mGrpObject.getString("mem_id"))
                                .setLog_id(mGrpObject.getString("log_id"))
                                .setSnake_id(mGrpObject.getString("snake_id"))
                                .setSnake_name(mGrpObject.getString("snake_name"))
                                .setLog_msg(mGrpObject.getString("log_msg"))
                                .setLatitute(mGrpObject.getString("lat"))
                                .setLongitute(mGrpObject.getString("long"))
                                .setLog_sts_id(mGrpObject.getString("log_sts_id"))
                                .setLog_name(mGrpObject.getString("log_name"))
                                .setLog_entry_datetime(mGrpObject.getString("log_entry_datetime"))
                                .build());
                    }
                }else{
                    errorMsgTV.setVisibility(View.VISIBLE);
                    mHistoryRV.setVisibility(View.GONE);
                }
                historyListRV.notifyDataSetChanged();

            } catch (JSONException e) {
                mHistoryRV.setVisibility(View.GONE);
                refreshTV.setVisibility(View.VISIBLE);
                errorMsgTV.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), Config.NET_CONN, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            mHistoryRV.setVisibility(View.GONE);
            refreshTV.setVisibility(View.VISIBLE);
            errorMsgTV.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), Config.NET_CONN, Toast.LENGTH_SHORT).show();
        }, map );
    }

    private void cancelAutoRefresh(){
        if(countDownTimer!= null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }

    private void initialize(View view) {
        historyConst = view.findViewById(R.id.const_history) ;
        mHistoryRV = view.findViewById(R.id.recyclerview_history_list) ;
        refreshTV = view.findViewById(R.id.text_refresh) ;
        dateNameTV = view.findViewById(R.id.text_date_name) ;
        errorMsgTV = view.findViewById(R.id.text_error_msg) ;
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_history) ;
        calendarView = view.findViewById(R.id.calender) ;

        progressBar = view.findViewById(R.id.progress) ;
    }
}