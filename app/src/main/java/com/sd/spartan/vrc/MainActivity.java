package com.sd.spartan.vrc;

import static com.sd.spartan.vrc.controller.AppConstants.AUTH_TOKEN;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.MEMBER;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_PHN_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_USERNAME;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sd.spartan.vrc.activity.EmailActivity;
import com.sd.spartan.vrc.activity.NoticeActivity;
import com.sd.spartan.vrc.activity.PdfViewActivity;
import com.sd.spartan.vrc.fragment.AdminViewFragment;
import com.sd.spartan.vrc.fragment.HistoryFragment;
import com.sd.spartan.vrc.fragment.SnakeGroupFragment;
import com.sd.spartan.vrc.fragment.UserListFragment;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView mTitleTV;
    ImageButton mNoticeImgBtn;
    private AlertDialog backPressDialog ;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder ;
    LocationController locationController ;
    private boolean backPress = true ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleClass.CheckInActive(TRUE);

        Initialize() ;

        setSupportActionBar(toolbar) ;
        mTitleTV.setText(R.string.app_name);



        AppController.mem_id = getSharedPreferences(MEMBER, MODE_PRIVATE).getString(MEM_ID, "") ;
        AppController.mem_username = getSharedPreferences(MEMBER, MODE_PRIVATE).getString(MEM_USERNAME, "") ;

        locationController = new LocationController(this) ;
        locationController.getLastLocation();

        mNoticeImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NoticeActivity.class) ;
            startActivity(intent);
        });
    }

    private void Initialize() {
        toolbar = findViewById(R.id.toolbar) ;
        mTitleTV = findViewById(R.id.text_toolbar_title) ;
        mNoticeImgBtn = findViewById(R.id.img_btn_notify) ;
        toolbar = findViewById(R.id.toolbar) ;

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this, R.style.MaterialAlertDialog_rounded) ;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.create_menu, menu);

        MenuItem createMenuItem = menu.findItem(R.id.user_create);
        MenuItem googleMapItem = menu.findItem(R.id.google_map);
        createMenuItem.setVisible(false) ;
        googleMapItem.setVisible(false) ;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.user_mail){
            Intent intent = new Intent(MainActivity.this, EmailActivity.class) ;
            startActivity(intent);

        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(AppController.checkAuthourity){
            CheckUserSts(Integer.parseInt(AppController.mem_sts_id), true);
        }

        AppController.auth_token = getSharedPreferences(MEMBER, MODE_PRIVATE).getString(AUTH_TOKEN, "") ;
        AppController.mem_phn_num = getSharedPreferences(MEMBER, MODE_PRIVATE).getString(MEM_PHN_NUM, "") ;

        if(!SimpleClass.CheckActive){
            SimpleClass.IntentLogin(MainActivity.this);
        }

    }

    public void CheckUserSts(int sts_id, boolean press) {
        this.backPress = press ;
        switch (sts_id){
            case 1:
                SimpleClass.CheckInActive(FALSE);
                SimpleClass.IntentLogin(MainActivity.this);
                break;
            case 2:
                if(SimpleClass.isLocate(MainActivity.this)){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_main, new SnakeGroupFragment()).addToBackStack(null).commit() ;
                }else{
                    SimpleClass.CheckInActive(FALSE);
                    SimpleClass.IntentLogin(MainActivity.this);
                    AppController.getAppController().getInAppNotifier().showToast("You are out of office area");
                }
                break;
            case 3:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                AdminViewFragment adminFragment = new AdminViewFragment(this);
                transaction.replace(R.id.frame_main, adminFragment,"admin");
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main, new SnakeGroupFragment()).addToBackStack(null).commit() ;
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main, new UserListFragment()).addToBackStack(null).commit() ;
                break;
            case 6:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_main, new HistoryFragment()).addToBackStack(null).commit() ;
                break;
            case 7:
                Intent i = new Intent(getApplicationContext(), PdfViewActivity.class) ;
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SimpleClass.CheckInActive(FALSE);
    }





    @Override
    public void onBackPressed() {
        if(backPress){
            CreateBackpressDialog();
        }else{
            CheckUserSts(3, true) ;
        }



    }
    private void CreateBackpressDialog() {
        View view = getLayoutInflater().inflate(R.layout.popup_backpress,  null);
        TextView textNo = view.findViewById(R.id.text_no) ;
        TextView textYes = view.findViewById(R.id.text_yes) ;

        textNo.setOnClickListener(v -> backPressDialog.cancel());

        textYes.setOnClickListener(v -> {
            backPressDialog.cancel();
            moveTaskToBack(true) ;



        });

        materialAlertDialogBuilder.setView(view);
        backPressDialog = materialAlertDialogBuilder.create();
        backPressDialog.show();
        backPressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }


}