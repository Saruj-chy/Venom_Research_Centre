package com.sd.spartan.vrc.adapter;

import static com.sd.spartan.vrc.controller.AppConstants.GROUP_ID;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_IMAGE;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.activity.SnakeListActivity;
import com.sd.spartan.vrc.model.GroupNameModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupNameRV extends RecyclerView.Adapter<GroupNameRV.MainPageViewHolder>
{
    private final Context mCtx;
    private final List<GroupNameModel> mGrpNameList;

    public GroupNameRV(Context mCtx, List<GroupNameModel> mGrpNameList) {
        this.mCtx = mCtx;
        this.mGrpNameList = mGrpNameList;
    }

    @NonNull
    @Override
    public MainPageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.card_group_list, null);
        return new MainPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainPageViewHolder holder, final int position) {

        GroupNameModel mList = mGrpNameList.get(position);
        ((MainPageViewHolder) holder).bind(mList);
    }



    @Override
    public int getItemCount() {
        return mGrpNameList.size();
    }


    class MainPageViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewName;
        CircleImageView imageView;
        public MainPageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_group_name);
            imageView = itemView.findViewById(R.id.image_group) ;
        }

        public void bind(GroupNameModel mList) {
            textViewName.setText(mList.getGroup_name());
            AppImageLoader.loadImageInView(mList.getGroup_image(), R.drawable.logo_vrc, (ImageView)imageView);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mCtx, SnakeListActivity.class) ;
                intent.putExtra(GROUP_ID, mList.getGroup_id());
                intent.putExtra(GROUP_IMAGE, mList.getGroup_image());
                intent.putExtra(GROUP_NAME, mList.getGroup_name());
                mCtx.startActivity(intent);
            });
        }
    }
}