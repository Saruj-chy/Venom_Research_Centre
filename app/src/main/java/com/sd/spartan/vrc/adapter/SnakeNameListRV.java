package com.sd.spartan.vrc.adapter;

import static com.sd.spartan.vrc.controller.AppConstants.DATE_OF_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.ENTRY_DATETIME;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_IMAGE;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.HEALTH_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.REG_DATE;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;

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
import com.sd.spartan.vrc.activity.SnakeDetailsActivity;
import com.sd.spartan.vrc.model.SnakeNameListModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SnakeNameListRV extends RecyclerView.Adapter<SnakeNameListRV.MainPageViewHolder>
{
    private final Context mCtx;
    private final List<SnakeNameListModel> mSnakeNameList;
    private final String mGrpImage ;

    public SnakeNameListRV(Context mCtx, List<SnakeNameListModel> mSnakeNameList, String mGrpImage) {
        this.mCtx = mCtx;
        this.mSnakeNameList = mSnakeNameList;
        this.mGrpImage = mGrpImage;
    }

    @NonNull
    @Override
    public MainPageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_snake_name_list, null);
        return new MainPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainPageViewHolder holder, final int position) {
        SnakeNameListModel mList = mSnakeNameList.get(position);
        (holder).bind(mList);
    }



    @Override
    public int getItemCount() {
        return mSnakeNameList.size();
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

        public void bind(SnakeNameListModel mList) {

            textViewName.setText(mList.getSnake_name() );
            AppImageLoader.loadImageInView(mGrpImage, R.drawable.logo_vrc, (ImageView)imageView);


            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mCtx, SnakeDetailsActivity.class) ;
                intent.putExtra(SNAKE_ID, mList.getSnake_id());
                intent.putExtra(SNAKE_NAME, mList.getSnake_name());
                intent.putExtra(GROUP_NAME, mList.getGroup_name());
                intent.putExtra(DATE_OF_COLLECTION, mList.getDate_of_collection());
                intent.putExtra(ENTRY_DATETIME, mList.getEntry_datetime());
                intent.putExtra(GROUP_IMAGE, mList.getGroup_image());
                intent.putExtra(HEALTH_STATUS, mList.getHealth_status());
                intent.putExtra(REG_DATE, mList.getReg_date());
                mCtx.startActivity(intent);
            });
        }
    }




}