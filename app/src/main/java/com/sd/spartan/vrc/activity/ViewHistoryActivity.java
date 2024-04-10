package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.ACT_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.AGE_DURING_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.CHIN_SHEILD_A;
import static com.sd.spartan.vrc.controller.AppConstants.CHIN_SHEILD_B;
import static com.sd.spartan.vrc.controller.AppConstants.CLEANING;
import static com.sd.spartan.vrc.controller.AppConstants.DATE_OF_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.ENTRY_DATE;
import static com.sd.spartan.vrc.controller.AppConstants.ENTRY_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.EXC_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FD_STS_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_WEIGHT;
import static com.sd.spartan.vrc.controller.AppConstants.FRONTAL;
import static com.sd.spartan.vrc.controller.AppConstants.GROUP_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.HEALTH_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.INTERNASAL;
import static com.sd.spartan.vrc.controller.AppConstants.LOREAL;
import static com.sd.spartan.vrc.controller.AppConstants.LOWER_LABIAL;
import static com.sd.spartan.vrc.controller.AppConstants.MEDI_DETAILS;
import static com.sd.spartan.vrc.controller.AppConstants.MENTAL;
import static com.sd.spartan.vrc.controller.AppConstants.NASAL;
import static com.sd.spartan.vrc.controller.AppConstants.NOTES;
import static com.sd.spartan.vrc.controller.AppConstants.PARIETAL;
import static com.sd.spartan.vrc.controller.AppConstants.PLACE_OF_COLLECTION;
import static com.sd.spartan.vrc.controller.AppConstants.POST_OCULAR;
import static com.sd.spartan.vrc.controller.AppConstants.PREFRONTAL;
import static com.sd.spartan.vrc.controller.AppConstants.PRE_OCULAR;
import static com.sd.spartan.vrc.controller.AppConstants.REG_DATE;
import static com.sd.spartan.vrc.controller.AppConstants.REG_ID;
import static com.sd.spartan.vrc.controller.AppConstants.REG_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.ROSTRAL;
import static com.sd.spartan.vrc.controller.AppConstants.SEX;
import static com.sd.spartan.vrc.controller.AppConstants.SHED_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_NAME;
import static com.sd.spartan.vrc.controller.AppConstants.SUB_OCULAR;
import static com.sd.spartan.vrc.controller.AppConstants.SUPRA_OCULAR;
import static com.sd.spartan.vrc.controller.AppConstants.TEMPORAL_F;
import static com.sd.spartan.vrc.controller.AppConstants.TEMPORAL_S;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;
import static com.sd.spartan.vrc.controller.AppConstants.UPDATE_COUNT;
import static com.sd.spartan.vrc.controller.AppConstants.UPPER_LABIAL;
import static com.sd.spartan.vrc.controller.AppConstants.VENOM_COLLECTION;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ViewHistoryActivity extends AppCompatActivity {

    WebView webView ;
    String dateStr;
    Date date ;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        SimpleClass.CheckInActive(TRUE);

        Toolbar toolbar = findViewById(R.id.appbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle("View History") ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        webView = findViewById(R.id.webview);

        String mapData = getIntent().getStringExtra("hashmap") ;
        Map<String, String> map = Hashmapsplit(mapData);

        try {
            date = simpleDateFormat.parse(Objects.requireNonNull(map.get("log_entry_datetime")));
            assert date != null;
            dateStr = simpleDateFormat.format(date);
        } catch (ParseException ignored) {
        }

        getData(map.get("snake_id"), map.get("log_sts_id"), dateStr);
    }

    private void getData(String snake_id, String log_sts_id, String date) {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put("snake_id", snake_id) ;
        map.put("log_sts_id", log_sts_id) ;
        map.put("date", date) ;

        AppController.getAppController().getAppNetworkController().makeRequest(Config.VIEW_HISTORY_DATA, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("error").equalsIgnoreCase("false")){

                    JSONArray grpArray = object.getJSONArray("data") ;
                    for(int i=0; i<grpArray.length(); i++){
                        JSONObject mGrpObject = grpArray.getJSONObject(i);
                        switch (log_sts_id) {
                            case "2":
                                getAllRegularData(mGrpObject, "Regular Data Entry");
                                break;
                            case "4":
                                getAllRegularData(mGrpObject, "Regular Data Update");
                                break;
                            case "6":
                                getAllRegularData(mGrpObject, "Regular Data Accessible");
                                break;
                            case "3":
                                getAllOccasionalData(mGrpObject, "Occasional Data Entry");
                                break;
                            case "5":
                                getAllOccasionalData(mGrpObject, "Occasional Data Update");
                                break;
                            case "7":
                                getAllOccasionalData(mGrpObject, "Occasional Data Accessible");
                                break;
                            case "1":
                                getNewSnakeData(mGrpObject, "New Snake Entry");
                                break;
                            case "8":
                                getNewSnakeData(mGrpObject, "Dead Snake Details");
                                break;
                        }

                    }
                }

            } catch (JSONException ignored) {

            }
        }, error -> {},
                map );
    }

    private void getNewSnakeData(JSONObject mGrpObject, String title) {
        try {
            String snake_id = mGrpObject.getString(SNAKE_ID);
            String snake_name = mGrpObject.getString(SNAKE_NAME);
            String sex = mGrpObject.getString(SEX);
            String age_during_collection = mGrpObject.getString(AGE_DURING_COLLECTION);
            String date_of_collection = mGrpObject.getString(DATE_OF_COLLECTION);
            String place_of_collection = mGrpObject.getString(PLACE_OF_COLLECTION);
            String notes = mGrpObject.getString(NOTES);
            String chin_sheild_A = mGrpObject.getString(CHIN_SHEILD_A);
            String chin_sheild_B = mGrpObject.getString(CHIN_SHEILD_B);
            String parietal = mGrpObject.getString(PARIETAL);
            String frontal = mGrpObject.getString(FRONTAL);
            String prefrontal = mGrpObject.getString(PREFRONTAL);
            String loreal = mGrpObject.getString(LOREAL);
            String mental = mGrpObject.getString(MENTAL);
            String nasal = mGrpObject.getString(NASAL);
            String internasal = mGrpObject.getString(INTERNASAL);
            String rostral = mGrpObject.getString(ROSTRAL);
            String temporal_f = mGrpObject.getString(TEMPORAL_F);
            String temporal_s = mGrpObject.getString(TEMPORAL_S);
            String upper_labial = mGrpObject.getString(UPPER_LABIAL);
            String lower_labial = mGrpObject.getString(LOWER_LABIAL);
            String supra_ocular = mGrpObject.getString(SUPRA_OCULAR);
            String sub_ocular = mGrpObject.getString(SUB_OCULAR);
            String pre_ocular = mGrpObject.getString(PRE_OCULAR);
            String post_ocular = mGrpObject.getString(POST_OCULAR);
            String entry_date = mGrpObject.getString(ENTRY_DATE);
            String entry_time = mGrpObject.getString(ENTRY_TIME);
            String group_name = mGrpObject.getString(GROUP_NAME);


            String msg = "<!DOCTYPE html>\n" +
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
                    "        margin: 5px;\n" +
                    "      }\n" +
                    "      .div-title {\n" +
                    "        display: flex;\n" +
                    "        align-items: center;\n" +
                    "         border: 1px solid black;\n" +
                    "         margin-top: 10px;\n" +
                    "      }\n" +
                    "      .text-title {\n" +
                    "        text-align: center;\n" +
                    "      }\n" +
                    "      p {\n" +
                    "        padding-right: 15px;\n" +
                    "        font-size: 14px;\n" +
                    "      }\n" +
                    "    </style>\n" +
                    "  </head>\n" +
                    "\n" +
                    "  <body>\n" +
                    "    <div class=\"div-header\">\n" +
                    "      <h1 class=\"text-title\">"+title+"</h1>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>New Snake ID:</p>\n" +
                    "        <h3>"+snake_id+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Group Name:</p>\n" +
                    "        <h3>"+group_name+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Snake ID:</p>\n" +
                    "        <h3>"+snake_name+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Sex:</p>\n" +
                    "        <h3>"+sex+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Age During Collection:</p>\n" +
                    "        <h3>"+age_during_collection+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Date of Collection:</p>\n" +
                    "        <h3>"+date_of_collection+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Place of Collection:</p>\n" +
                    "        <h3>"+place_of_collection+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Notes:</p>\n" +
                    "        <h3>"+notes+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Chin-Sheild(A):</p>\n" +
                    "        <h3>"+chin_sheild_A+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Chin-Sheild(P):</p>\n" +
                    "        <h3>"+chin_sheild_B+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Parietal :</p>\n" +
                    "        <h3>"+parietal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Frontal :</p>\n" +
                    "        <h3>"+frontal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Prefrontal :</p>\n" +
                    "        <h3>"+prefrontal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Loreal :</p>\n" +
                    "        <h3>"+loreal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Mental:</p>\n" +
                    "        <h3>"+mental+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Nasal :</p>\n" +
                    "        <h3>"+nasal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Internasal :</p>\n" +
                    "        <h3>"+internasal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Rostral :</p>\n" +
                    "        <h3>"+rostral+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Temporal(f) :</p>\n" +
                    "        <h3>"+temporal_f+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Temporal(s) :</p>\n" +
                    "        <h3>"+temporal_s+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Upper Labial :</p>\n" +
                    "        <h3>"+upper_labial+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Lower Labial :</p>\n" +
                    "        <h3>"+lower_labial+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Supra Ocular :</p>\n" +
                    "        <h3>"+supra_ocular+"</h3>\n" +
                    "      </div>\n" +

                    "      <div class=\"div-title\">\n" +
                    "        <p>Sub Ocular :</p>\n" +
                    "        <h3>"+sub_ocular+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Pre Ocular :</p>\n" +
                    "        <h3>"+pre_ocular+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Post Ocular :</p>\n" +
                    "        <h3>"+post_ocular+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Entry Date and Time :</p>\n" +
                    "        <h3>"+entry_date + " "+ entry_time+"</h3>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>\n";

            webView.loadDataWithBaseURL(null, msg,"text/html","utf-8",null);
        } catch (JSONException ignored) {
        }
    }

    private void getAllOccasionalData(JSONObject mGrpObject, String title) {
        try {
            String occ_snake_id = mGrpObject.getString("occ_snake_id");
            String age = mGrpObject.getString("age");
            String weight_gm = mGrpObject.getString("weight_gm");
            String head = mGrpObject.getString("head");
            String body = mGrpObject.getString("body");
            String tail = mGrpObject.getString("tail");
            String total = mGrpObject.getString("total");
            String dorsal = mGrpObject.getString("dorsal");
            String ventral = mGrpObject.getString("ventral");
            String anal = mGrpObject.getString("anal");
            String sub_caudal = mGrpObject.getString("sub_caudal");
            String notes = mGrpObject.getString("notes");
            String occ_date = mGrpObject.getString("occ_date");
            String occ_time = mGrpObject.getString("occ_time");
            String update_count = mGrpObject.getString("update_count");
            String group_name = mGrpObject.getString("group_name");
            String snake_name = mGrpObject.getString("snake_name");


            String msg = "<!DOCTYPE html>\n" +
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
                    "        margin: 5px;\n" +
                    "      }\n" +
                    "      .div-title {\n" +
                    "        display: flex;\n" +
                    "        align-items: center;\n" +
                    "         border: 1px solid black;\n" +
                    "         margin-top: 10px;\n" +
                    "      }\n" +
                    "      .text-title {\n" +
                    "        text-align: center;\n" +
                    "      }\n" +
                    "      p {\n" +
                    "        padding-right: 15px;\n" +
                    "        font-size: 14px;\n" +
                    "      }\n" +
                    "    </style>\n" +
                    "  </head>\n" +
                    "\n" +
                    "  <body>\n" +
                    "    <div class=\"div-header\">\n" +
                    "      <h1 class=\"text-title\">"+title+"</h1>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Occ ID:</p>\n" +
                    "        <h3>"+occ_snake_id+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Group Name:</p>\n" +
                    "        <h3>"+group_name+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Snake ID:</p>\n" +
                    "        <h3>"+snake_name+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Age:</p>\n" +
                    "        <h3>"+age+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Weight(gm):</p>\n" +
                    "        <h3>"+weight_gm+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Head:</p>\n" +
                    "        <h3>"+head+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Body:</p>\n" +
                    "        <h3>"+body+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Tail:</p>\n" +
                    "        <h3>"+tail+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Total :</p>\n" +
                    "        <h3>"+total+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Dorsal :</p>\n" +
                    "        <h3>"+dorsal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Ventral :</p>\n" +
                    "        <h3>"+ventral+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Anal :</p>\n" +
                    "        <h3>"+anal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Sub-Caudal:</p>\n" +
                    "        <h3>"+sub_caudal+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Notes :</p>\n" +
                    "        <h3>"+notes+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Total Update :</p>\n" +
                    "        <h3>"+update_count+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Date and Time :</p>\n" +
                    "        <h3>"+occ_date + " "+ occ_time+"</h3>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>\n";

            webView.loadDataWithBaseURL(null, msg,"text/html","utf-8",null);
        } catch (JSONException ignored) {
        }
    }

    private void getAllRegularData(JSONObject mGrpObject, String title) {
        try {
            String reg_id = mGrpObject.getString(REG_ID);
            String food_weight = mGrpObject.getString(FOOD_WEIGHT);
            String food_num = mGrpObject.getString(FOOD_NUM);
            String food_names = mGrpObject.getString(FOOD_NAMES);
            String fd_sts_names = mGrpObject.getString(FD_STS_NAMES);
            String act_names = mGrpObject.getString(ACT_NAMES);
            String exc_names = mGrpObject.getString(EXC_NAMES);
            String health_status = mGrpObject.getString(HEALTH_STATUS);
            String cleaning = mGrpObject.getString(CLEANING);
            String venom_collection = mGrpObject.getString(VENOM_COLLECTION);
            String shed_names = mGrpObject.getString(SHED_NAMES);
            String medi_details = mGrpObject.getString(MEDI_DETAILS);
            String reg_date = mGrpObject.getString(REG_DATE);
            String reg_time = mGrpObject.getString(REG_TIME);
            String update_count = mGrpObject.getString(UPDATE_COUNT);
            String group_name = mGrpObject.getString(GROUP_NAME);
            String snake_name = mGrpObject.getString(SNAKE_NAME);


            String msg = "<!DOCTYPE html>\n" +
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
                    "        margin: 5px;\n" +
                    "      }\n" +
                    "      .div-title {\n" +
                    "        display: flex;\n" +
                    "        align-items: center;\n" +
                    "         border: 1px solid black;\n" +
                    "         margin-top: 10px;\n" +
                    "      }\n" +
                    "      .text-title {\n" +
                    "        text-align: center;\n" +
                    "      }\n" +
                    "      p {\n" +
                    "        padding-right: 15px;\n" +
                    "        font-size: 14px;\n" +
                    "      }\n" +
                    "    </style>\n" +
                    "  </head>\n" +
                    "\n" +
                    "  <body>\n" +
                    "    <div class=\"div-header\">\n" +
                    "      <h1 class=\"text-title\">"+title+"</h1>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Reg ID:</p>\n" +
                    "        <h3>"+reg_id+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Group Name:</p>\n" +
                    "        <h3>"+group_name+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Snake ID:</p>\n" +
                    "        <h3>"+snake_name+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Food Weight(gm):</p>\n" +
                    "        <h3>"+food_weight+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Food Number:</p>\n" +
                    "        <h3>"+food_num+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Food Items:</p>\n" +
                    "        <h3>"+food_names+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Food Status Names:</p>\n" +
                    "        <h3>"+fd_sts_names+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Activity During Check:</p>\n" +
                    "        <h3>"+act_names+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Excretion :</p>\n" +
                    "        <h3>"+exc_names+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Health Status :</p>\n" +
                    "        <h3>"+health_status+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Cleaning :</p>\n" +
                    "        <h3>"+cleaning+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Venom Collection :</p>\n" +
                    "        <h3>"+venom_collection+"</h3>\n" +
                    "      </div>\n" +
                    "\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Shedding Status:</p>\n" +
                    "        <h3>"+shed_names+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Medication :</p>\n" +
                    "        <h3>"+medi_details+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Total Update :</p>\n" +
                    "        <h3>"+update_count+"</h3>\n" +
                    "      </div>\n" +
                    "      <div class=\"div-title\">\n" +
                    "        <p>Date and Time :</p>\n" +
                    "        <h3>"+reg_date + " "+ reg_time+"</h3>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>\n";

            webView.loadDataWithBaseURL(null, msg,"text/html","utf-8",null);
        } catch (JSONException ignored) {
        }

    }


    public Map<String, String> Hashmapsplit(String s) {
        s = s.replace("{", "").replace("}", "");
        String[] arr = s.split(", ");

        Map<String, String> map = new HashMap<>();
        for (String str : arr) {
            String[] splited = str.split("=");
            map.put(splited[0], splited[1].trim());
        }
        return map;

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!SimpleClass.CheckActive){
            SimpleClass.IntentLogin(this);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SimpleClass.CheckInActive(TRUE);
    }
}