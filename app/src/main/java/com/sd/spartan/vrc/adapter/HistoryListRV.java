package com.sd.spartan.vrc.adapter;

import static com.sd.spartan.vrc.controller.AppConstants.HASHMAP;
import static com.sd.spartan.vrc.controller.AppConstants.LOG_ENTRY_DATETIME;
import static com.sd.spartan.vrc.controller.AppConstants.LOG_STS_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.activity.ViewHistoryActivity;
import com.sd.spartan.vrc.model.BuilderModel;

import java.util.HashMap;
import java.util.List;

public class HistoryListRV extends RecyclerView.Adapter<HistoryListRV.HistoryListVH>
{
    private final Context mCtx;
    private final List<BuilderModel> mGrpNameList;

    public HistoryListRV(Context mCtx, List<BuilderModel> mGrpNameList) {
        this.mCtx = mCtx;
        this.mGrpNameList = mGrpNameList;
    }

    @NonNull
    @Override
    public HistoryListVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_history, null);
        return new HistoryListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryListVH holder, final int position) {
        BuilderModel mList = mGrpNameList.get(position);
        holder.bind(mList);
    }

    @Override
    public int getItemCount() {
        return mGrpNameList.size();
    }


    class HistoryListVH extends RecyclerView.ViewHolder
    {
        TextView snakeIdTV, logNameTV, logMsgTV, latTV, longTV, dateTV;
        public HistoryListVH(@NonNull View itemView) {
            super(itemView);

            snakeIdTV = itemView.findViewById(R.id.text_id_value);
            logNameTV = itemView.findViewById(R.id.text_log_name);
            logMsgTV = itemView.findViewById(R.id.text_log_msg);
            latTV = itemView.findViewById(R.id.text_lat_value);
            longTV = itemView.findViewById(R.id.text_long_value);
            dateTV = itemView.findViewById(R.id.text_entry_value);
        }

        public void bind(BuilderModel mList) {
            snakeIdTV.setText(mList.getSnake_name());
            logNameTV.setText(mList.getLog_name());
            logMsgTV.setText(mList.getLog_msg());
            latTV.setText(mList.getLatitute());
            longTV.setText(mList.getLongitute());
            dateTV.setText(mList.getLog_entry_datetime());

            HashMap<String, String> map = new HashMap<>() ;
            map.put(SNAKE_ID, mList.getSnake_id()) ;
            map.put(LOG_STS_ID, mList.getLog_sts_id()) ;
            map.put(LOG_ENTRY_DATETIME, mList.getLog_entry_datetime()) ;

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mCtx, ViewHistoryActivity.class);
                intent.putExtra(HASHMAP, map.toString());
                mCtx.startActivity(intent);
            });
        }
    }
}