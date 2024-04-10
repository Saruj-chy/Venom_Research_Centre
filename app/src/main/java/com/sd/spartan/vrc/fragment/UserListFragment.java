package com.sd.spartan.vrc.fragment;

import static com.sd.spartan.vrc.controller.AppConstants.DATA;
import static com.sd.spartan.vrc.controller.AppConstants.ERROR;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE_STR;
import static com.sd.spartan.vrc.controller.AppConstants.INPUT_TYPE;
import static com.sd.spartan.vrc.controller.AppConstants.MEMBER_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_PASSWORD;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_PHN_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_STS_ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_USERNAME;
import static com.sd.spartan.vrc.controller.AppConstants.MSG;
import static com.sd.spartan.vrc.controller.AppConstants.REMOVE_DATA;
import static com.sd.spartan.vrc.controller.AppConstants.STS_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.SURE_DLT_MEM;
import static com.sd.spartan.vrc.controller.AppConstants.UPDATE_MEMBER_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.USER_PHN_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.WANT_UPDATE_MEM_STS;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sd.spartan.vrc.Interfaces.UserIF;
import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.adapter.UserListRV;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.BuilderClass;
import com.sd.spartan.vrc.model.BuilderModel;
import com.sd.spartan.vrc.model.PopupSureCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserListFragment extends Fragment implements UserIF {
    private TextView refreshTV ;
    private RecyclerView mGrpRecyclerView ;
    private ProgressBar progressBar ;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private UserListRV userListRV;
    private List<BuilderModel> userModelList;

    private CountDownTimer countDownTimer;
    long remainingRefreshTime = 2000 ;

    ListView listView;
    Button savedLayoutBtn;

    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        initialize(view) ;


        userModelList = new ArrayList<>() ;

        userListRV = new UserListRV(getContext(), userModelList, this);
        mGrpRecyclerView.setAdapter(userListRV);

        GridLayoutManager mGridManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        mGrpRecyclerView.setLayoutManager(mGridManager);


        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.DKGRAY);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            userModelList.clear();
            setAutoRefresh();
        });




        return view ;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadGroupName() {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(INPUT_TYPE, "1") ;

        progressBar.setVisibility(View.VISIBLE);
        AppController.getAppController().getAppNetworkController().makeRequest(Config.USER_ACT_ALL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                    userModelList.clear();
                    mGrpRecyclerView.setVisibility(View.VISIBLE); //change
                    refreshTV.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    JSONArray grpArray = object.getJSONArray(DATA) ;
                    for(int i=0; i<grpArray.length(); i++){
                        JSONObject mGrpObject = grpArray.getJSONObject(i);

                        userModelList.add(new BuilderClass()
                                .setMem_id(mGrpObject.getString(MEM_ID))
                                .setMem_username(mGrpObject.getString(MEM_USERNAME))
                                .setMem_password(mGrpObject.getString(MEM_PASSWORD))
                                .setMem_phn_num(mGrpObject.getString(MEM_PHN_NUM))
                                .setMem_sts_id(mGrpObject.getString(MEM_STS_ID))
                                .setSts_name(mGrpObject.getString(STS_NAME))
                                .build()
                        ) ;
                    }
                    userListRV.notifyDataSetChanged();
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
            countDownTimer = new CountDownTimer(remainingRefreshTime, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mGrpRecyclerView.setVisibility(View.GONE);
                }

                @Override
                public void onFinish() {
                    swipeRefreshLayout.setRefreshing(false);
                    loadGroupName();
                    cancelAutoRefresh() ;
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
        mGrpRecyclerView = view.findViewById(R.id.recyclerview_user_list) ;
        refreshTV = view.findViewById(R.id.text_refresh) ;
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_main) ;

        progressBar = view.findViewById(R.id.progress_main) ;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadGroupName();

    }

    @Override
    public void OnDelete(String memId, String phnNum) {

        PopupSureCheck.setTitle(REMOVE_DATA);
        PopupSureCheck.setDetail(SURE_DLT_MEM);

        PopupSureCheck.CreatePopup(getActivity(), view -> { //yes button
            PopupSureCheck.ll.setVisibility(View.GONE);
            PopupSureCheck.progress.setVisibility(View.VISIBLE);

            HashMap<String, String> map = new HashMap<>() ;
            map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
            map.put(MEM_PHN_NUM, AppController.mem_phn_num) ;
            map.put(MEM_ID, memId) ;
            map.put(USER_PHN_NUM, phnNum) ;
            map.put(INPUT_TYPE, "3") ;
            OnDeleteMember(map);


        }, view -> { // no button
            PopupSureCheck.dialog.cancel();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void OnDeleteMember(HashMap<String, String> map) {
        AppController.getAppController().getAppNetworkController().makeRequest(Config.USER_ACT_ALL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString(ERROR).equalsIgnoreCase(FALSE_STR)){
                    loadGroupName();
                    userListRV.notifyDataSetChanged();
                }
                Toast.makeText(getContext(), object.getString(MSG), Toast.LENGTH_SHORT).show();
                PopupSureCheck.dialog.cancel();


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

    @Override
    public void OnAccess(String memId, String stsId, String phnNum) {
        View view = getLayoutInflater().inflate(R.layout.layout_popup, null);
        TextView titleTV = view.findViewById(R.id.text_title) ;
        listView = view.findViewById(R.id.listview_popup);
        savedLayoutBtn = view.findViewById(R.id.btn_saved_layout);
        savedLayoutBtn.setVisibility(View.GONE);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (300 + (listView.getDividerHeight()));
        listView.setLayoutParams(params);

        titleTV.setText(MEMBER_STATUS);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.mem_sts_id));
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setItemChecked(Integer.parseInt(stsId)-1, true);

        listView.setOnItemClickListener((parent, view12, position, id) -> {
            int pos = position +1 ;
            PopupSureCheck.setTitle(UPDATE_MEMBER_STATUS);
            PopupSureCheck.setDetail(WANT_UPDATE_MEM_STS);

            PopupSureCheck.CreatePopup(getActivity(), view1 -> { //yes button
                PopupSureCheck.ll.setVisibility(View.GONE);
                PopupSureCheck.progress.setVisibility(View.VISIBLE);

                HashMap<String, String> map = new HashMap<>() ;
                map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
                map.put(MEM_PHN_NUM, AppController.mem_phn_num) ;
                map.put(MEM_ID, memId) ;
                map.put(USER_PHN_NUM, phnNum) ;
                map.put(INPUT_TYPE, "2") ;
                map.put(MEM_STS_ID, String.valueOf(pos)) ;
                OnDeleteMember(map);
                dialog.cancel();
            }, view1 -> {  //no button
                PopupSureCheck.dialog.cancel();
                dialog.cancel();
            });
        });
    }
}