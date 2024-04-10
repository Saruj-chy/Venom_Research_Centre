package com.sd.spartan.vrc.adapter;

import static com.sd.spartan.vrc.controller.AppConstants.DATE_TIME_;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEDICATION;
import static com.sd.spartan.vrc.controller.AppConstants.MSG;
import static com.sd.spartan.vrc.controller.AppConstants.NOTICE;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.SURE_DELETE_DATA;
import static com.sd.spartan.vrc.controller.AppConstants.SURE_TASK_COMPLETE;
import static com.sd.spartan.vrc.controller.AppConstants.WAITING_HOUR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.activity.NoticeActivity;
import com.sd.spartan.vrc.activity.RegularActivity;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.BuilderModel;
import com.sd.spartan.vrc.model.PopupSureCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NoticeRV extends RecyclerView.Adapter<NoticeRV.NoticeVH>
{
    private final Context mCtx;
    private final List<BuilderModel> mNoticeList;
    private final String notice_sts_id;

    public NoticeRV(Context mCtx, List<BuilderModel> mNoticeList, String notice_sts_id) {
        this.mCtx = mCtx;
        this.mNoticeList = mNoticeList;
        this.notice_sts_id = notice_sts_id;
    }

    @NonNull
    @Override
    public NoticeVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_notice, null);
        return new NoticeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoticeVH holder, final int position) {
        BuilderModel mList = mNoticeList.get(position) ;
        if(Objects.equals(mList.getNotice_sts_id(), notice_sts_id)){
            holder.bind(mList);
        }else{
            holder.cardView.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mNoticeList.size();
    }


    class NoticeVH extends RecyclerView.ViewHolder
    {
        CardView cardView;
        ConstraintLayout noticeCons;
        TextView nameTV, msgTV, datetimeTV, titleTV, snakeNameTV, groupNameTV;
        public NoticeVH(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardview);
            noticeCons = itemView.findViewById(R.id.const_notice);
            nameTV = itemView.findViewById(R.id.text_notice_name);
            msgTV = itemView.findViewById(R.id.text_notice_msg);
            datetimeTV = itemView.findViewById(R.id.text_datetime);
            titleTV = itemView.findViewById(R.id.text_datetime_title);
            snakeNameTV = itemView.findViewById(R.id.text_snake_name);
            groupNameTV = itemView.findViewById(R.id.text_group_name);

        }

        public void bind(BuilderModel mList) {
            String secName;

            if(mList.getNotice_sec_id().equals("1")){
                noticeCons.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.dark_green));
                secName=MEDICATION;
                nameTV.setText(secName);
            }else if(mList.getNotice_sec_id().equals("2")){
                noticeCons.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.mix_primary));
                secName =FOOD_STATUS;
                nameTV.setText(secName);
            }
            if(mList.getNotice_sts_id().equals("1") && mList.getNotice_sec_id().equals("1")){
                titleTV.setText(WAITING_HOUR);
                datetimeTV.setText(mList.getHour());
            } else{
                titleTV.setText(DATE_TIME_);
                datetimeTV.setText(mList.getDate_time());
            }

            snakeNameTV.setText(mList.getSnake_name());
            groupNameTV.setText(mList.getGroup_name());
            msgTV.setText(mList.getMsg());

            cardView.setOnClickListener(view -> {
                if(!Objects.equals(mList.getNotice_sts_id(), "3")){
                    Intent intent = new Intent(mCtx, RegularActivity.class);
                    intent.putExtra(SNAKE_ID, mList.getSnake_id() );
                    intent.putExtra(SNAKE_NAME, mList.getSnake_name() );
                    intent.putExtra(GROUP_NAME, mList.getGroup_name() );
                    mCtx.startActivity(intent);
                }
            });

            cardView.setOnLongClickListener(view -> {
                if(Objects.equals(mList.getNotice_sts_id(), "3")){
                    PopupSureCheck.setTitle(NOTICE);
                    PopupSureCheck.setDetail(SURE_DELETE_DATA);

                    PopupSureCheck.CreatePopup((NoticeActivity) mCtx, view1 -> { //yes button
                        PopupSureCheck.ll.setVisibility(View.GONE);
                        PopupSureCheck.progress.setVisibility(View.VISIBLE);
                        UpdateDeleteNotice(mList.getId(), "3");
                    }, view12 -> { // no button
                        PopupSureCheck.dialog.cancel();
                    });
                }  else if(Objects.equals(mList.getNotice_sec_id(), "1") && !Objects.equals(mList.getNotice_sts_id(), "3")){
                    PopupSureCheck.setTitle(NOTICE);
                    PopupSureCheck.setDetail(SURE_TASK_COMPLETE);

                    PopupSureCheck.CreatePopup((NoticeActivity) mCtx, view1 -> { //yes button
                        PopupSureCheck.ll.setVisibility(View.GONE);
                        PopupSureCheck.progress.setVisibility(View.VISIBLE);
                        UpdateDeleteNotice(mList.getId(), "2");
                    }, view12 -> { // no button
                        PopupSureCheck.dialog.cancel();
                    });
                }
                return false;
            });
        }
        private void UpdateDeleteNotice(String id, String stateId) {
            HashMap<String, String> map = new HashMap<>() ;
            map.put(AppConstants.STATE, stateId) ;
            map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
            map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
            map.put(ID, id) ;

            AppController.getAppController().getAppNetworkController().makeRequest(Config.NOTICE, response -> {
                try {
                    JSONObject object = new JSONObject(response);
                    Toast.makeText(mCtx, object.getString(MSG), Toast.LENGTH_SHORT).show();
                    PopupSureCheck.dialog.cancel();
                    ((NoticeActivity) mCtx).setAutoRefresh();
                } catch (JSONException e) {
                    Toast.makeText(mCtx, Config.NET_CONN, Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(mCtx, Config.NET_CONN, Toast.LENGTH_SHORT).show(), map );
        }
    }
}