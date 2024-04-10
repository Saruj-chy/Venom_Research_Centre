package com.sd.spartan.vrc.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.sd.spartan.vrc.R;

public class PopupSureCheck {
    public static AlertDialog.Builder dialogBuilder  ;
    public static AlertDialog dialog;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout ll ;
    @SuppressLint("StaticFieldLeak")
    public  static  ProgressBar progress ;
    public static String t, d ;


    public static void CreatePopup(Activity context, View.OnClickListener yesListener, View.OnClickListener noListener) {
        View view = context.getLayoutInflater().inflate(R.layout.popup_sure_check, null);
        dialogBuilder = new AlertDialog.Builder(context);

        ll = view.findViewById(R.id.linear_yes_no) ;
        progress = view.findViewById(R.id.progress) ;
        TextView titleTV = view.findViewById(R.id.text_sure_title);
        TextView confirmTV = view.findViewById(R.id.text_confirm);
        TextView yesBtn = view.findViewById(R.id.text_yes) ;
        TextView noBtn = view.findViewById(R.id.text_no) ;

        titleTV.setText(t);
        confirmTV.setText(d);

        yesBtn.setOnClickListener(v -> {
            if(yesListener != null){
                yesListener.onClick(v);
            }
        });

        noBtn.setOnClickListener(v -> {
            if(noListener != null){
                noListener.onClick(v);
            }
        });

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static void setTitle(String title){
        t= title ;
    }
    public static void setDetail(String detail){
        d= detail ;
    }


}
