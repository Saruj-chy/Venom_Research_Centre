package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.AGE;
import static com.sd.spartan.vrc.controller.AppConstants.ANAL;
import static com.sd.spartan.vrc.controller.AppConstants.AUTH_TOKEN;
import static com.sd.spartan.vrc.controller.AppConstants.BODY;
import static com.sd.spartan.vrc.controller.AppConstants.DORSAL;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.HEAD;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_PHN_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.MSG;
import static com.sd.spartan.vrc.controller.AppConstants.NOTES;
import static com.sd.spartan.vrc.controller.AppConstants.OCC_DATA;
import static com.sd.spartan.vrc.controller.AppConstants.OCC_DATA_DISPLAY;
import static com.sd.spartan.vrc.controller.AppConstants.OCC_DATE;
import static com.sd.spartan.vrc.controller.AppConstants.OCC_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.PLZ_START_END;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.STATE;
import static com.sd.spartan.vrc.controller.AppConstants.SUB_CAUDAL;
import static com.sd.spartan.vrc.controller.AppConstants.SURE_DISPLAY_DATA;
import static com.sd.spartan.vrc.controller.AppConstants.TAIL;
import static com.sd.spartan.vrc.controller.AppConstants.TOTAL;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;
import static com.sd.spartan.vrc.controller.AppConstants.VENTRAL;
import static com.sd.spartan.vrc.controller.AppConstants.WEIGHT_GM;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.BuilderClass;
import com.sd.spartan.vrc.model.BuilderModel;
import com.sd.spartan.vrc.model.PopupSureCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OccPrintActivity extends AppCompatActivity {
    private ConstraintLayout mOccConst;
    private RelativeLayout lenRL, scalesRL;
    private ImageView lenUpImg, lenDownImg, scaleUpImg, scaleDownImg ;
    private TextView mGroupNameTextView, mSnakeNameTextView;
    private TextView mAgeTV, mWeightTV, mHeadTV, mBodyTV, mTailTV, mTotalTV, mDorsalTV, mVentralTV, mAnalTV, mSubCaudalTV, mNotesTV, mStartTV, mEndTV;
    private WebView webView;
    private String mSelectItems;

    private String mSnakeId;
    private String mStartDate="";
    private String mEndDate="";
    private List<BuilderModel> occList;
    private HashMap<String, String> hashMap;


    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occ_print);

        SimpleClass.CheckInActive(TRUE);

        Initialize() ;
        Toolbar toolbar = findViewById(R.id.appbar_print) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        setTitle(OCC_DATA_DISPLAY) ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        GetStringExtra();
        OnClickItem(mAgeTV, hashMap, AGE, AGE);
        OnClickItem(mWeightTV, hashMap, WEIGHT_GM, WEIGHT_GM);
        OnClickItem(mHeadTV, hashMap, HEAD, HEAD);
        OnClickItem(mBodyTV, hashMap, BODY, BODY);
        OnClickItem(mTailTV, hashMap, TAIL, TAIL);
        OnClickItem(mTotalTV, hashMap, TOTAL, TOTAL);
        OnClickItem(mDorsalTV, hashMap, DORSAL, DORSAL);
        OnClickItem(mVentralTV, hashMap, VENTRAL, VENTRAL);
        OnClickItem(mAnalTV, hashMap, ANAL, ANAL);
        OnClickItem(mSubCaudalTV, hashMap, SUB_CAUDAL, SUB_CAUDAL);
        OnClickItem(mNotesTV, hashMap, NOTES, NOTES);
    }
    private void GetStringExtra() {
        String mGroupName = getIntent().getStringExtra(GROUP_NAME);
        mSnakeId = getIntent().getStringExtra(SNAKE_ID);
        String mSnakeName = getIntent().getStringExtra(SNAKE_NAME);
        mGroupNameTextView.setText(mGroupName);
        mSnakeNameTextView.setText(mSnakeName);
    }

    private void OnClickItem(TextView textView, HashMap<String, String> hashMap, String text1, String text2) {
        textView.setOnClickListener(view -> {
            Map<String, String> filteredMap = new HashMap<>();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                filteredMap = hashMap.entrySet()
                        .stream()
                        .filter(entry -> !Objects.equals(entry.getValue(), ""))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            if(filteredMap.size()>4 && Objects.equals(hashMap.get(text1), "")){
                AppController.getAppController().getInAppNotifier().showToast(AppConstants.ALLOWED_MSG);
            }else if((filteredMap.size()>4 && !Objects.equals(hashMap.get(text1), "")) || (filteredMap.size()<5 && !Objects.equals(hashMap.get(text1), ""))){
                hashMap.put(text1, "");
                textView.setBackground(ContextCompat.getDrawable(OccPrintActivity.this, R.drawable.stroke_solid));
            }else{
                hashMap.put(text1, text2);
                textView.setBackground(ContextCompat.getDrawable(OccPrintActivity.this, R.drawable.stroke_solid_hologreen));
            }
        });
    }


    private void Initialize() {
        lenRL = findViewById(R.id.relative_len);
        scalesRL = findViewById(R.id.relative_scales);
        lenDownImg = findViewById(R.id.image_down_len);
        lenUpImg = findViewById(R.id.image_up_len);
        scaleDownImg = findViewById(R.id.image_down_scales);
        scaleUpImg = findViewById(R.id.image_up_scale);

        mOccConst = findViewById(R.id.const_occ_main);
        webView = findViewById(R.id.webview_print) ;
        mGroupNameTextView = findViewById(R.id.text_group_name_monthly);
        mSnakeNameTextView = findViewById(R.id.text_snake_name_monthly);
        mAgeTV = findViewById(R.id.text_age) ;
        mWeightTV = findViewById(R.id.text_weight) ;
        mHeadTV = findViewById(R.id.text_head) ;
        mBodyTV = findViewById(R.id.text_body) ;
        mTailTV = findViewById(R.id.text_tail) ;
        mTotalTV = findViewById(R.id.text_total) ;
        mDorsalTV = findViewById(R.id.text_dorsal) ;
        mVentralTV = findViewById(R.id.text_ventral) ;
        mAnalTV = findViewById(R.id.text_anal) ;
        mSubCaudalTV = findViewById(R.id.text_sub_caudal) ;
        mNotesTV = findViewById(R.id.text_notes) ;
        mStartTV = findViewById(R.id.text_start) ;
        mEndTV = findViewById(R.id.text_end) ;

        occList = new ArrayList<>() ;

        hashMap = new LinkedHashMap<>();
        hashMap.put(AGE,"");
        hashMap.put(HEAD,"");
        hashMap.put(BODY,"");
        hashMap.put(TAIL,"");
        hashMap.put(TOTAL,"");
        hashMap.put(DORSAL,"");
        hashMap.put(VENTRAL,"");
        hashMap.put(ANAL,"");
        hashMap.put(SUB_CAUDAL,"");
        hashMap.put(NOTES,"");
        hashMap.put(WEIGHT_GM,"");
        hashMap.put(OCC_DATE,"");
        hashMap.put(OCC_TIME,"");

    }



    public void OnPickStartDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, day) -> {
                    mStartDate  = year + "-" + (month+1) + "-" + day ;
                    mStartTV.setText(mStartDate);

                }, year, month, dayOfMonth);

        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void OnPickEndDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, day) -> {
                    mEndDate  = year + "-" + (month+1) + "-" + day ;
                    mEndTV.setText(mEndDate);

                }, year, month, dayOfMonth);

        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void onOccPrintClick(View view) {
        if(!Objects.equals(mStartDate, "") && !Objects.equals(mEndDate, "")){
            mSelectItems = getAllInputData();
            if(!mSelectItems.equals("")){
                PopupSureCheck.setTitle(OCC_DATA);
                PopupSureCheck.setDetail(SURE_DISPLAY_DATA);
                PopupSureCheck.CreatePopup(this, view1 -> { //yes button
                    PopupSureCheck.ll.setVisibility(View.GONE);
                    PopupSureCheck.progress.setVisibility(View.VISIBLE);
                    LoadOccPrintData();
                }, view12 -> { // no button
                    PopupSureCheck.dialog.cancel();
                });
            }

        }else{
            AppController.getAppController().getInAppNotifier().showToast(PLZ_START_END);
        }
    }
    private String getAllInputData() {
        Map<String, String> filteredMap = new HashMap<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            filteredMap = hashMap.entrySet()
                    .stream()
                    .filter(entry -> !Objects.equals(entry.getValue(), ""))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        StringBuilder sqlValue= new StringBuilder();
        if(filteredMap.size()>0){
            for (Map.Entry<String, String> entry : filteredMap.entrySet()) {
                sqlValue.append("`").append(entry.getValue()).append("`, ");
            }
            sqlValue.append(" `occ_date`").append(", `occ_time`");
            return sqlValue.toString();
        }else{
            AppController.getAppController().getInAppNotifier().showToast(AppConstants.SELECT_ONE_ITEM);
        }

        return sqlValue.toString();
    }

    private void LoadOccPrintData() {
        AppController.getAppController().getAppNetworkController().makeRequest(Config.PUT_USER_PRINT, response -> {
            PopupSureCheck.dialog.cancel();
            PopupSureCheck.progress.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("error").equalsIgnoreCase("false")){
                    WebLayoutDisplay();
                    JSONArray grpArray = object.getJSONArray("data") ;
                    for(int i=0; i<grpArray.length(); i++){
                        JSONObject mGrpObject = grpArray.getJSONObject(i);

                        BuilderClass builderClass = new BuilderClass();
                        for (Iterator<String> it = mGrpObject.keys(); it.hasNext(); ){
                            String key = it.next();
                            switch (key) {
                                case AGE:
                                    builderClass.setAge(mGrpObject.getString(AGE));
                                    break;
                                case WEIGHT_GM:
                                    builderClass.setWeight_gm(mGrpObject.getString(WEIGHT_GM));
                                    break;
                                case HEAD:
                                    builderClass.setHead(mGrpObject.getString(HEAD));
                                    break;
                                case BODY:
                                    builderClass.setBody(mGrpObject.getString(BODY));
                                    break;
                                case TAIL:
                                    builderClass.setTail(mGrpObject.getString(TAIL));
                                    break;
                                case TOTAL:
                                    builderClass.setTotal(mGrpObject.getString(TOTAL));
                                    break;
                                case DORSAL:
                                    builderClass.setDorsal(mGrpObject.getString(DORSAL));
                                    break;
                                case VENTRAL:
                                    builderClass.setVentral(mGrpObject.getString(VENTRAL));
                                    break;
                                case ANAL:
                                    builderClass.setAnal(mGrpObject.getString(ANAL));
                                    break;
                                case SUB_CAUDAL:
                                    builderClass.setSub_caudal(mGrpObject.getString(SUB_CAUDAL));
                                    break;
                                case NOTES:
                                    builderClass.setNotes(mGrpObject.getString(NOTES));
                                    break;
                                case OCC_DATE:
                                    builderClass.setOcc_date(mGrpObject.getString(OCC_DATE));
                                    break;
                                case OCC_TIME:
                                    builderClass.setOcc_time(mGrpObject.getString(OCC_TIME));
                                    break;
                            }
                        }
                        occList.add(builderClass.build());

                    }
                    webView.loadDataWithBaseURL(null,StringOccDefault( occList),"text/html","utf-8",null);
                }else{
                    Toast.makeText(OccPrintActivity.this, object.getString(MSG), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException ignored) {
            }
        }, error -> {
        }, putHashmap() );
    }
    private String StringOccDefault( List<BuilderModel> occList){
        String head = HtmlHeadOccDefault() ;

        String htmlBody = "  <body>\n" +
                "    <div class=\"div-1\"></div>\n" +
                "    <div class=\"div-header\">\n" +
                "      <div class=\"div-title\">\n" +
                "        <h3>Venom Research Centre</h3>\n" +
                "        <h3>Morphometry of Snakes</h3>\n" +
                "        <h3>Data Sheet v6.0</h3>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" ;

        String footerTitle = FooterTitleOcc() ;

        String htmlHead = head+ htmlBody+
                footerTitle;
        String footerData = GetFooterData(occList) ;
        return String.format("%s%s", htmlHead, footerData);
    }
    private String HtmlHeadOccDefault() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta\n" +
                "      name=\"\\viewport\\\"\n" +
                "      content=\"\\width\"\n" +
                "      =\"device-width,\"\n" +
                "      initial-scale=\"1.0\\\"\n" +
                "    />\n" +
                "    <style>\n" +
                "      h3,\n" +
                "      p {\n" +
                "        margin: 0px;\n" +
                "        padding: 0px;\n" +
                "      }\n" +
                "\n" +
                "      .title {\n" +
                "        color: black;\n" +
                "        font-style: normal;\n" +
                "        font-size: 20px;\n" +
                "      }\n" +
                "      .title-sub {\n" +
                "        color: black;\n" +
                "        font-style: normal;\n" +
                "        font-size: 16px;\n" +
                "      }\n" +
                "\n" +
                "      table,\n" +
                "      td,\n" +
                "      th {\n" +
                "        border: 1px solid;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "\n" +
                "      table {\n" +
                "        width: 100%;\n" +
                "        border-collapse: collapse;\n" +
                "      }\n" +
                "\n" +
                "      tr {\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "      .half-width {\n" +
                "        width: 50%;\n" +
                "        text-align: start;\n" +
                "        padding-left: 20px;\n" +
                "      }\n" +
                "th, tr {\n" +
                "        font-size: 11px;\n" +
                "      }"+
                "      img {\n" +
                "        margin: 50px 0px 0px 0px;\n" +
                "      }\n" +
                "      .div-header {\n" +
                "        display: inline-flex;\n" +
                "        width: 100%;\n" +
                "        justify-content: center;\n" +
                "        text-align: center;\n" +
                "        margin: 0px;\n" +
                "        padding: 0px;\n" +
                "      }\n" +
                "      .div-title {\n" +
                "        padding: 50px 0px 0px 0px;\n" +
                "      }\n" +
                "      .div-page {\n" +
                "        margin: 60px 0px 0px 0px;\n" +
                "      }\n" +
                "      .page-num,\n" +
                "      .page-date {\n" +
                "        font-size: 14px;\n" +
                "      }\n" +
                "      .page-num b {\n" +
                "        font-size: 18px;\n" +
                "      }\n" +
                "      .page-id b {\n" +
                "        font-size: 18px;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>";
    }
    private String FooterTitleOcc() {
        StringBuilder tableTitle = new StringBuilder();
        tableTitle.append("      <table>\n" +
                "        <tr>\n" +
                "          <th>Date</th>\n" +
                "          <th>Time</th>\n" );
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if(entry.getKey().equals(AGE) && !entry.getValue().equals("")){
                tableTitle.append("          <th>Age</th>\n");
            }else if(entry.getKey().equals(HEAD)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Head</th>\n");
            }else if(entry.getKey().equals(BODY)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Body</th>\n");
            }else if(entry.getKey().equals(TAIL)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Tail</th>\n");
            }else if(entry.getKey().equals(TOTAL)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Total</th>\n");
            }else if(entry.getKey().equals(DORSAL)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Dorsal</th>\n");
            }else if(entry.getKey().equals(VENTRAL)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Ventral</th>\n");
            }else if(entry.getKey().equals(ANAL)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Anal</th>\n");
            }else if(entry.getKey().equals(SUB_CAUDAL)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Sub-Caudal</th>\n");
            }else if(entry.getKey().equals(NOTES)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Notes</th>\n" );
            }else if(entry.getKey().equals(WEIGHT_GM)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Weight(gm)</th>\n");
            }
        }
        tableTitle.append("        </tr>");
        return tableTitle.toString();
    }

    private String GetFooterData(List<BuilderModel> occList) {
        String htmlBody = "" ;
        for(int i =0; i< occList.size(); i++){
            BuilderModel dataModel = occList.get(i);
            StringBuilder tableView = new StringBuilder();
            tableView.append("<tr>\n" + "          <td>").append(dataModel.getOcc_date()).append("</td>\n").append("          <td>").append(dataModel.getOcc_time()).append("</td>\n");
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                if(entry.getKey().equals(AGE) && !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getAge()).append("</td>\n");
                }else if(entry.getKey().equals(HEAD)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getHead()).append("</td>\n");
                }else if(entry.getKey().equals(BODY)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getBody()).append("</td>\n");
                }else if(entry.getKey().equals(TAIL)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getTail()).append("</td>\n");
                }else if(entry.getKey().equals(TOTAL)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getTotal()).append("</td>\n");
                }else if(entry.getKey().equals(DORSAL)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getDorsal()).append("</td>\n");
                }else if(entry.getKey().equals(VENTRAL)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getVentral()).append("</td>\n");
                }else if(entry.getKey().equals(ANAL)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getAnal()).append("</td>\n");
                }else if(entry.getKey().equals(SUB_CAUDAL)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getSub_caudal()).append("</td>\n");
                }else if(entry.getKey().equals(NOTES)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getNotes()).append("</td>\n");
                }else if(entry.getKey().equals(WEIGHT_GM)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getWeight_gm()).append("</td>\n");
                }
            }
            tableView.append("        </tr>");

            htmlBody = String.format("%s%s", htmlBody, tableView);
        }
        String htmlFooter = "</table>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
        return String.format("%s%s", htmlBody, htmlFooter);
    }


    private HashMap<String, String> putHashmap(){
        HashMap<String, String> map = new HashMap<>() ;
        map.put(STATE, "2") ;
        map.put(AUTH_TOKEN, AppController.auth_token) ;
        map.put(MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(SNAKE_ID, mSnakeId) ;
        map.put("select_items", mSelectItems) ;
        map.put("start_date", mStartDate) ;
        map.put("end_date", mEndDate) ;
        return  map;
    }

    private void WebLayoutDisplay() {
        mOccConst.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }

    public void OnLengthUpImg(View view) {
        UpDownShow(lenRL, lenUpImg, lenDownImg, true);
    }
    public void OnLengthDownImg(View view) {
        UpDownShow(lenRL, lenUpImg, lenDownImg, false);
    }
    public void OnScaleUpImg(View view) {
        UpDownShow(scalesRL, scaleUpImg, scaleDownImg, true);
    }
    public void OnScaleDownImg(View view) {
        UpDownShow(scalesRL, scaleUpImg, scaleDownImg, false);
    }
    private void UpDownShow(RelativeLayout rl, ImageView upImg, ImageView downImg, boolean prior){
        if(prior){
            rl.setVisibility(View.GONE);
            upImg.setVisibility(View.GONE);
            downImg.setVisibility(View.VISIBLE);
        }else{
            rl.setVisibility(View.VISIBLE);
            upImg.setVisibility(View.VISIBLE);
            downImg.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!SimpleClass.CheckActive){
            SimpleClass.IntentLogin(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SimpleClass.CheckInActive(FALSE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SimpleClass.CheckInActive(TRUE);
    }


}