package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.ACT_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.AUTH_TOKEN;
import static com.sd.spartan.vrc.controller.AppConstants.CLEANING;
import static com.sd.spartan.vrc.controller.AppConstants.EXC_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.FD_STS_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_WEIGHT;
import static com.sd.spartan.vrc.controller.AppConstants.HEALTH_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.MEDI_DETAILS;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_PHN_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.REG_DATE;
import static com.sd.spartan.vrc.controller.AppConstants.REG_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.REMARKS;
import static com.sd.spartan.vrc.controller.AppConstants.SHED_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.STATE;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;
import static com.sd.spartan.vrc.controller.AppConstants.VENOM_COLLECTION;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
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

public class RegularPrintActivity extends AppCompatActivity {
    private ConstraintLayout mainViewConst;
    private TextView mGroupNameTextView;
    private TextView mSnakeNameTextView;
    private TextView fdWeightTV, fdQtyTV, foodItemTV, foodStatusTV, actCheckTV, excTV, cleaningTV, healthStsTV, venomCollectTV, shedTV, mediTV, remarkTV, mStartTV, mEndTV;
    private WebView webView;

    private String mSnakeId;

    private String mStartDate="", mEndDate="", mSelectItems;

    private HashMap<String, String> hashMap;

    private List<BuilderModel> reguList;


    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_print);
        SimpleClass.CheckInActive(TRUE);

        Initialize() ;
        Toolbar toolbar = findViewById(R.id.appbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle("Regular Data Display") ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        GetStringExtra() ;

        OnClickItem(fdWeightTV, hashMap, FOOD_WEIGHT, FOOD_WEIGHT);
        OnClickItem(fdQtyTV, hashMap, FOOD_NUM, FOOD_NUM);
        OnClickItem(foodItemTV, hashMap, FOOD_NAMES, FOOD_NAMES);
        OnClickItem(foodStatusTV, hashMap, FD_STS_NAMES, FD_STS_NAMES);
        OnClickItem(actCheckTV, hashMap, ACT_NAMES, ACT_NAMES);
        OnClickItem(excTV, hashMap, EXC_NAMES, EXC_NAMES);
        OnClickItem(healthStsTV, hashMap, HEALTH_STATUS, HEALTH_STATUS);
        OnClickItem(cleaningTV, hashMap, CLEANING, CLEANING);
        OnClickItem(venomCollectTV, hashMap, VENOM_COLLECTION, VENOM_COLLECTION);
        OnClickItem(mediTV, hashMap, MEDI_DETAILS, MEDI_DETAILS);
        OnClickItem(shedTV, hashMap, SHED_NAMES, SHED_NAMES);
        OnClickItem(remarkTV, hashMap, REMARKS, REMARKS);


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
                textView.setBackground(ContextCompat.getDrawable(RegularPrintActivity.this, R.drawable.stroke_solid));
            }else{
                hashMap.put(text1, text2);
                textView.setBackground(ContextCompat.getDrawable(RegularPrintActivity.this, R.drawable.stroke_solid_hologreen));
            }
        });
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

    private void GetStringExtra() {
        String mGroupName = getIntent().getStringExtra("group_name");
        mSnakeId = getIntent().getStringExtra("snake_id");
        String mSnakeName = getIntent().getStringExtra("snake_name");
        mGroupNameTextView.setText(mGroupName);
        mSnakeNameTextView.setText(mSnakeName);
    }


    private void Initialize() {
        mainViewConst = findViewById(R.id.const_main_view) ;
        webView = findViewById(R.id.webview_print) ;

        mGroupNameTextView = findViewById(R.id.text_group_name_daily) ;
        mSnakeNameTextView = findViewById(R.id.text_snake_name_daily) ;

        fdWeightTV = findViewById(R.id.text_food_weight) ;
        fdQtyTV = findViewById(R.id.text_food_qty) ;
        foodItemTV = findViewById(R.id.text_food_items) ;
        foodStatusTV = findViewById(R.id.text_food_status) ;
        actCheckTV = findViewById(R.id.text_act_during_check) ;
        excTV = findViewById(R.id.text_exc) ;
        healthStsTV = findViewById(R.id.text_health_sts) ;
        cleaningTV = findViewById(R.id.text_clean) ;
        venomCollectTV = findViewById(R.id.text_venom_collect) ;
        shedTV = findViewById(R.id.text_shed) ;
        mediTV = findViewById(R.id.text_medi) ;
        remarkTV = findViewById(R.id.text_remarks) ;
        mStartTV = findViewById(R.id.text_start) ;
        mEndTV = findViewById(R.id.text_end) ;

        reguList = new ArrayList<>() ;

        hashMap = new LinkedHashMap<>();
        hashMap.put(FOOD_WEIGHT,"");
        hashMap.put(FOOD_NUM,"");
        hashMap.put(FOOD_NAMES,"");
        hashMap.put(FD_STS_NAMES,"");
        hashMap.put(ACT_NAMES,"");
        hashMap.put(EXC_NAMES,"");
        hashMap.put(HEALTH_STATUS,"");
        hashMap.put(CLEANING,"");
        hashMap.put(VENOM_COLLECTION,"");
        hashMap.put(SHED_NAMES,"");
        hashMap.put(MEDI_DETAILS,"");
        hashMap.put(REMARKS,"");
        hashMap.put(REG_DATE,"");
        hashMap.put(REG_TIME,"");



    }

    public void OnRegPrintData(View view) {
        if(!Objects.equals(mStartDate, "") && !Objects.equals(mEndDate, "")){
            mSelectItems = getAllInputData();
            if(!mSelectItems.equals("")){
                PopupSureCheck.setTitle("Regular Data");
                PopupSureCheck.setDetail("Are you sure you want to display the data");
                PopupSureCheck.CreatePopup(this, view1 -> { //yes button
                    PopupSureCheck.ll.setVisibility(View.GONE);
                    PopupSureCheck.progress.setVisibility(View.VISIBLE);
                    LoadRegPrintData();
                }, view12 -> { // no button
                    PopupSureCheck.dialog.cancel();
                });
            }

        }else{
            AppController.getAppController().getInAppNotifier().showToast("Please select the start & end date");
        }

    }

    private void LoadRegPrintData() {
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
                                case "food_weight":
                                    builderClass.setFood_weight(mGrpObject.getString("food_weight"));
                                    break;
                                case "food_num":
                                    builderClass.setFood_num(mGrpObject.getString("food_num"));
                                    break;
                                case "fd_sts_names":
                                    builderClass.setFd_sts_name(mGrpObject.getString("fd_sts_names"));
                                    break;
                                case "food_names":
                                    builderClass.setFood_name(mGrpObject.getString("food_names"));
                                    break;
                                case "act_names":
                                    builderClass.setAct_name(mGrpObject.getString("act_names"));
                                    break;
                                case "exc_names":
                                    builderClass.setExc_name(mGrpObject.getString("exc_names"));
                                    break;
                                case "health_status":
                                    builderClass.setHealth_status(mGrpObject.getString("health_status"));
                                    break;
                                case "cleaning":
                                    builderClass.setCleaning(mGrpObject.getString("cleaning"));
                                    break;
                                case "venom_collection":
                                    builderClass.setVenom_collection(mGrpObject.getString("venom_collection"));
                                    break;
                                case "shed_names":
                                    builderClass.setShed_name(mGrpObject.getString("shed_names"));
                                    break;
                                case "medi_details":
                                    builderClass.setMedi_details(mGrpObject.getString("medi_details"));
                                    break;
                                case "remarks":
                                    builderClass.setRemarks(mGrpObject.getString("remarks"));
                                    break;
                                case "reg_date":
                                    builderClass.setReg_date(mGrpObject.getString("reg_date"));
                                    break;
                                case "reg_time":
                                    builderClass.setReg_time(mGrpObject.getString("reg_time"));
                                    break;
                            }
                        }
                        reguList.add(builderClass.build());
                    }
                    webView.loadDataWithBaseURL(null,StringRegDefault(reguList),"text/html","utf-8",null);

                }else{
                    Toast.makeText(RegularPrintActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException ignored) {
            }
        }, error -> {
        }, putHashmap() );
    }

    private String StringRegDefault(List<BuilderModel> reguList){
        String head = HtmlHeadRegDefault();
        String tableTitle = HtmlTableTitleReg();
        String htmlHead = head+
                "  <body>\n" +
                "    <div class=\"div-header\">\n" +
                "      <div class=\"div-title\">\n" +
                "        <h3>Venom Research Centre, Bangladesh</h3>\n" +
                "        <p class=\"title-sub\">Department of Medicine</p>\n" +
                "        <p class=\"title-sub\">Chittagong Medical College</p>\n" +
                "        <h3>Data Sheet v6.0</h3>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" +
                tableTitle;

        String htmlFooter = HtmlFooterReg(reguList);
        return String.format("%s%s", htmlHead, htmlFooter);
    }
    private String HtmlHeadRegDefault() {
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
                "      .div-1 {\n" +
                "        justify-content: center;\n" +
                "        text-align: center;\n" +
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
    private String HtmlTableTitleReg() {
        StringBuilder tableTitle = new StringBuilder();
        tableTitle.append("    <div>\n" +
                "      <table>\n" +
                "        <tr>\n" +
                "          <th>Date</th>\n" +
                "          <th>Time</th>\n");
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if(entry.getKey().equals(FOOD_WEIGHT) && !entry.getValue().equals("")){
                tableTitle.append("          <th>Food Weight(gm)</th>\n" );
            }else if(entry.getKey().equals(FOOD_NUM)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Food Qty.</th>\n");
            }else if(entry.getKey().equals(FOOD_NAMES)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Food Type</th>\n");
            }else if(entry.getKey().equals(FD_STS_NAMES)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Food Status</th>\n");
            }else if(entry.getKey().equals(ACT_NAMES)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Activity During Check</th>\n");
            }else if(entry.getKey().equals(EXC_NAMES)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Excretion</th>\n");
            }else if(entry.getKey().equals(HEALTH_STATUS)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Health Status</th>\n" );
            }else if(entry.getKey().equals(CLEANING)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Cleaning</th>\n");
            }else if(entry.getKey().equals(VENOM_COLLECTION)&& !entry.getValue().equals("")){
                tableTitle.append( "          <th>Venom Collection</th>\n" );
            }else if(entry.getKey().equals(SHED_NAMES)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Shedding Status</th>\n");
            }else if(entry.getKey().equals(MEDI_DETAILS)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Medication</th>\n" );
            }else if(entry.getKey().equals(REMARKS)&& !entry.getValue().equals("")){
                tableTitle.append("          <th>Remarks</th>\n" );
            }
        }
        tableTitle.append("        </tr>");
        return tableTitle.toString();
    }
    private String HtmlFooterReg(List<BuilderModel> reguList) {
        String htmlBody = "" ;
        for(int i =0; i< reguList.size(); i++){
            BuilderModel dataModel = reguList.get(i);
            StringBuilder tableView = new StringBuilder();
            tableView.append("<tr>\n" + "          <td>").append(dataModel.getReg_date()).append("</td>\n").append("          <td>").append(dataModel.getReg_time()).append("</td>\n");
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                if(entry.getKey().equals(FOOD_WEIGHT) && !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getFood_weight()).append("</td>\n");
                }else if(entry.getKey().equals(FOOD_NUM)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getFood_num()).append("</td>\n");
                }else if(entry.getKey().equals(FOOD_NAMES)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getFood_name()).append("</td>\n");
                }else if(entry.getKey().equals(FD_STS_NAMES)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getFd_sts_name()).append("</td>\n");
                }else if(entry.getKey().equals(ACT_NAMES)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getAct_name()).append("</td>\n");
                }else if(entry.getKey().equals(EXC_NAMES)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getExc_name()).append("</td>\n");
                }else if(entry.getKey().equals(HEALTH_STATUS)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getHealth_status()).append("</td>\n");
                }else if(entry.getKey().equals(CLEANING)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getCleaning()).append("</td>\n");
                }else if(entry.getKey().equals(VENOM_COLLECTION)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getVenom_collection()).append("</td>\n");
                }else if(entry.getKey().equals(SHED_NAMES)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getShed_name()).append("</td>\n");
                }else if(entry.getKey().equals(MEDI_DETAILS)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getMedi_details()).append("</td>\n");
                }else if(entry.getKey().equals(REMARKS)&& !entry.getValue().equals("")){
                    tableView.append("          <td>").append(dataModel.getRemarks()).append("</td>\n");
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

    private void WebLayoutDisplay() {
        mainViewConst.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }

    private HashMap<String, String> putHashmap(){
        HashMap<String, String> map = new HashMap<>() ;
        map.put(STATE, "1") ;
        map.put(AUTH_TOKEN, AppController.auth_token) ;
        map.put(MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(SNAKE_ID, mSnakeId) ;
        map.put("select_items", mSelectItems) ;
        map.put("start_date", mStartDate) ;
        map.put("end_date", mEndDate) ;
        return map;
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
            sqlValue.append(" `reg_date`").append(", `reg_time`");
            return sqlValue.toString();
        }else{
            AppController.getAppController().getInAppNotifier().showToast(AppConstants.SELECT_ONE_ITEM);
        }

        return sqlValue.toString();
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