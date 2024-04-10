package com.sd.spartan.vrc.adapter;

import static com.sd.spartan.vrc.controller.AppConstants.NO_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.TEL_;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sd.spartan.vrc.Interfaces.UserIF;
import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.model.BuilderModel;

import java.util.List;

public class UserListRV extends RecyclerView.Adapter<UserListRV.UserListVH>
{
    private final Context mCtx;
    private final List<BuilderModel> mGrpNameList;
    private final UserIF userIF ;

    public UserListRV(Context mCtx, List<BuilderModel> mGrpNameList, UserIF userIF) {
        this.mCtx = mCtx;
        this.mGrpNameList = mGrpNameList;
        this.userIF = userIF;
    }

    @NonNull
    @Override
    public UserListVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_user_details, null);
        return new UserListVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListVH holder, final int position) {
        BuilderModel mList = mGrpNameList.get(position);
        holder.bind(mList);
    }



    @Override
    public int getItemCount() {
        return mGrpNameList.size();
    }


    class UserListVH extends RecyclerView.ViewHolder
    {
        TextView userNameTV, phnNumTV, stsTV;
        ImageButton delImgBtn, accessImgBtn ;
        public UserListVH(@NonNull View itemView) {
            super(itemView);

            userNameTV = itemView.findViewById(R.id.text_user_name);
            phnNumTV = itemView.findViewById(R.id.text_user_phn) ;
            stsTV = itemView.findViewById(R.id.text_user_sts) ;
            delImgBtn = itemView.findViewById(R.id.img_del) ;
            accessImgBtn = itemView.findViewById(R.id.img_access) ;
        }

        @SuppressLint("SetTextI18n")
        public void bind(BuilderModel mList) {
            if(mList.getMem_username().equalsIgnoreCase("")){
                userNameTV.setText(NO_NAME);
            }else{
                userNameTV.setText(mList.getMem_username());
            }

            phnNumTV.setText(String.format("+880%s", mList.getMem_phn_num()));
            stsTV.setText(mList.getSts_name());

            stsTV.setOnClickListener(view -> {
            });
            phnNumTV.setOnClickListener(view ->{
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse( TEL_ + mList.getMem_phn_num()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                mCtx.startActivity(intent);
            });

            delImgBtn.setOnClickListener(view -> userIF.OnDelete(mList.getMem_id(), mList.getMem_phn_num()));

            accessImgBtn.setOnClickListener(view -> userIF.OnAccess(mList.getMem_id(), mList.getMem_sts_id(), mList.getMem_phn_num()));
        }
    }
}