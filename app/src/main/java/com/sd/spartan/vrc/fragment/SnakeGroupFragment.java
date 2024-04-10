package com.sd.spartan.vrc.fragment;

import static com.sd.spartan.vrc.controller.AppConstants.DATA;
import static com.sd.spartan.vrc.controller.AppConstants.ERROR;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE_STR;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_ID;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_IMAGE;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.TYPE_ID;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.adapter.GroupNameRV;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.BuilderClass;
import com.sd.spartan.vrc.model.GroupNameModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SnakeGroupFragment extends Fragment {
    private TextView refreshTV ;
    private RecyclerView mGrpRecyclerView ;
    private ProgressBar progressBar ;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private GroupNameRV mGrpAdapter;
    private List<GroupNameModel> mainGroupList;
    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 2000 ;

    LocationController locationController ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snake_group, container, false);

        initialize(view) ;


        mainGroupList = new ArrayList<>() ;

        mGrpAdapter = new GroupNameRV(getContext(), mainGroupList);
        mGrpRecyclerView.setAdapter(mGrpAdapter);

        GridLayoutManager mGridManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mGrpRecyclerView.setLayoutManager(mGridManager);


        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.DKGRAY);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            mainGroupList.clear();
            setAutoRefresh();
        });

        locationController = new LocationController(getContext()) ;
        locationController.getLastLocation();

        return view ;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadGroupName() {

        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(TYPE_ID, "4") ;

        AppController.getAppController().getAppNetworkController().makeRequest(Config.GET_ALL_GROUP_NAME, response -> {
            swipeRefreshLayout.setRefreshing(false);
            cancelAutoRefresh() ;
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                    mainGroupList.clear();
                    mGrpRecyclerView.setVisibility(View.VISIBLE);
                    refreshTV.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    JSONArray grpArray = object.getJSONArray(DATA) ;
                    for(int i=0; i<grpArray.length(); i++){
                        JSONObject mGrpObject = grpArray.getJSONObject(i);

                        mainGroupList.add(new BuilderClass()
                                .setGroup_id(mGrpObject.getString(GROUP_ID))
                                .setGroup_name(mGrpObject.getString(GROUP_NAME))
                                .setGroup_image(mGrpObject.getString(GROUP_IMAGE))
                                .buildGrp()
                        ) ;
                    }
                    mGrpAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                mGrpRecyclerView.setVisibility(View.GONE);
                refreshTV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), Config.NET_CONN, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            mGrpRecyclerView.setVisibility(View.GONE);
            refreshTV.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), Config.NET_CONN, Toast.LENGTH_SHORT).show();
        }, map );


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
                    mGrpRecyclerView.setVisibility(View.GONE);

                }

                @Override
                public void onFinish() {
                    loadGroupName();



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

    private void initialize(View view) {
        mGrpRecyclerView = view.findViewById(R.id.recyclerview_group_name) ;
        refreshTV = view.findViewById(R.id.text_refresh) ;
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_main) ;

        progressBar = view.findViewById(R.id.progress_main) ;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadGroupName();
    }
}