package com.sd.spartan.vrc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.sd.spartan.vrc.MainActivity;
import com.sd.spartan.vrc.R;

public class AdminViewFragment extends Fragment {
    private ConstraintLayout mSnkDetailsCons, chkMemCons, historyCons, downloadPdfCons ;
    MainActivity mainActivity ;


    public AdminViewFragment(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_admin_view, container, false);
        Initialize(view);

        mSnkDetailsCons.setOnClickListener(view14 -> mainActivity.CheckUserSts(4, false));
        chkMemCons.setOnClickListener(view13 -> mainActivity.CheckUserSts(5, false));
        historyCons.setOnClickListener(view12 -> mainActivity.CheckUserSts(6, false));
        downloadPdfCons.setOnClickListener(view1 -> mainActivity.CheckUserSts(7, false));
        return view ;
    }

    private void Initialize(View view) {
        mSnkDetailsCons = view.findViewById(R.id.const_snk_details) ;
        chkMemCons = view.findViewById(R.id.const_chk_mem) ;
        historyCons = view.findViewById(R.id.constraint_history) ;
        downloadPdfCons = view.findViewById(R.id.const_download_pdf) ;
    }
}