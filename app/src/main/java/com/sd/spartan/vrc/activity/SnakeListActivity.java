package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.DATA;
import static com.sd.spartan.vrc.controller.AppConstants.DATE_OF_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.ENTRY_DATE_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.ERROR;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE_STR;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_ID;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_IMAGE;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.HEALTH_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.NO_DATA_AVAILABLE;
import static com.sd.spartan.vrc.controller.AppConstants.REG_DATE;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.STATE;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;
import static com.sd.spartan.vrc.controller.AppConstants.USER_CREATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.adapter.SnakeNameListRV;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.BuilderClass;
import com.sd.spartan.vrc.model.SnakeNameListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SnakeListActivity extends AppCompatActivity {

    private TextView refreshTV ;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private ProgressBar progressBar ;
    private RecyclerView mSnakeListRecyclerView;
    private SnakeNameListRV mSnakeListAdapter;
    List<SnakeNameListModel> mSnakeList;

    public static String group_id, group_image, group_name ;

    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 2000 ;

    LocationController locationController ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_list);
        SimpleClass.CheckInActive(TRUE);

        group_id = getIntent().getStringExtra(GROUP_ID).trim() ;
        group_image = getIntent().getStringExtra(GROUP_IMAGE).trim() ;
        group_name = getIntent().getStringExtra(GROUP_NAME).trim() ;



        Toolbar toolbar = findViewById(R.id.appbar_snakelist) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle(group_name) ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        initialize() ;

        mSnakeList = new ArrayList<>() ;

        mSnakeListAdapter = new SnakeNameListRV(this, mSnakeList, group_image);
        mSnakeListRecyclerView.setAdapter(mSnakeListAdapter);
        GridLayoutManager mGridManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mSnakeListRecyclerView.setLayoutManager(mGridManager);


        locationController = new LocationController(this) ;
        locationController.getLastLocation();

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.DKGRAY);
        swipeRefreshLayout.setOnRefreshListener(this::setAutoRefresh);
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
                    mSnakeListRecyclerView.setVisibility(View.GONE);
//                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {

                    loadSnakeListName();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.create_menu, menu);

        MenuItem mailMenuItem = menu.findItem(R.id.user_mail);
        mailMenuItem.setVisible(false) ;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.user_create){
            Intent intent = new Intent(getApplicationContext(), CreateVenomActivity.class) ;
            intent.putExtra(GROUP_NAME, group_name );
            intent.putExtra(STATE, USER_CREATE );
            startActivity(intent);
        } else if (item.getItemId() == R.id.google_map){
            Intent intent = new Intent(getApplicationContext(), GoogleMapActivity.class) ;
            intent.putExtra(GROUP_NAME, group_name );
            startActivity(intent);
        }

        return true;
    }
    private void initialize() {
        mSnakeListRecyclerView = findViewById(R.id.recyclerview_snake_list) ;
        refreshTV = findViewById(R.id.text_refresh) ;
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_main) ;
        progressBar = findViewById(R.id.progress_main) ;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadSnakeListName() {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(GROUP_ID, group_id) ;

        AppController.getAppController().getAppNetworkController().makeRequest(Config.GET_ALL_SNAKE, response -> {
            swipeRefreshLayout.setRefreshing(false);
            cancelAutoRefresh() ;
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                    mSnakeList.clear();
                    mSnakeListRecyclerView.setVisibility(View.VISIBLE);
                    refreshTV.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    JSONArray snakeListArray = object.getJSONArray(DATA) ;
                    for(int i=0; i<snakeListArray.length(); i++){
                        JSONObject mSnakeListObject = snakeListArray.getJSONObject(i);

                        mSnakeList.add(
                                new BuilderClass()
                                        .setSnake_id(mSnakeListObject.getString(SNAKE_ID))
                                        .setGroup_id(mSnakeListObject.getString(GROUP_ID))
                                        .setSnake_name(mSnakeListObject.getString(SNAKE_NAME))
                                        .setGroup_name(mSnakeListObject.getString(GROUP_NAME))
                                        .setDate_of_collection(mSnakeListObject.getString(DATE_OF_COLLECTION))
                                        .setEntry_date_time(mSnakeListObject.getString(ENTRY_DATE_TIME))
                                        .setGroup_image(mSnakeListObject.getString(GROUP_IMAGE))
                                        .setHealth_status(mSnakeListObject.getString(HEALTH_STATUS))
                                        .setReg_date(mSnakeListObject.getString(REG_DATE))
                                        .buildSnakeNameList()
                        ) ;
                    }

                    mSnakeListAdapter.notifyDataSetChanged();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SnakeListActivity.this, NO_DATA_AVAILABLE, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                mSnakeListRecyclerView.setVisibility(View.GONE);
                refreshTV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SnakeListActivity.this, Config.NET_CONN, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            mSnakeListRecyclerView.setVisibility(View.GONE);
            refreshTV.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(SnakeListActivity.this, Config.NET_CONN, Toast.LENGTH_SHORT).show();
        }, map );
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadSnakeListName() ;
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
}