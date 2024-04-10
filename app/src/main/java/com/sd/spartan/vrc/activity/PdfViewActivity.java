package com.sd.spartan.vrc.activity;

import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.BuilderClass;
import com.sd.spartan.vrc.model.BuilderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PdfViewActivity extends AppCompatActivity {
    ConstraintLayout inputConstraint ;
    Button regularBtn, occBtn, backBtn, createBtn ;
    WebView webViewDefault, webView;

    TextInputEditText pageNoTIE, snakeIdTIE ;
    TextView mStartTV, mEndTV ;
    CheckBox checkBox ;
    String pageNo="", snakeId="", mStartDate="", mEndDate="" ;

    boolean printPress = FALSE ;

    private List<BuilderModel> reguList, occList;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        SimpleClass.CheckInActive(TRUE);

        Initialize() ;
        reguList = new ArrayList<>() ;
        occList = new ArrayList<>() ;

        Toolbar toolbar = findViewById(R.id.appbar_pdf) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle("DataSheet") ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

//        constraintLayout = findViewById(R.id.constraint) ;
        webViewDefault = findViewById(R.id.webview_default) ;
        webView = findViewById(R.id.webview) ;

    }

    private void Initialize() {
        regularBtn = findViewById(R.id.btn_view_regular) ;
        occBtn = findViewById(R.id.btn_view_occ) ;
        backBtn = findViewById(R.id.btn_view_back) ;
        createBtn = findViewById(R.id.btn_view_create) ;
        inputConstraint = findViewById(R.id.constraint_input) ;
        pageNoTIE = findViewById(R.id.edit_page_no) ;
        snakeIdTIE = findViewById(R.id.edit_snake_id) ;
        mStartTV = findViewById(R.id.text_start) ;
        mEndTV = findViewById(R.id.text_end) ;
        checkBox = findViewById(R.id.checkbox) ;
    }

    public void CreatePdf(View view){
        PrintManager printManager=(PrintManager)PdfViewActivity.this.getSystemService(PRINT_SERVICE);
        PrintDocumentAdapter adapter;
        adapter= webView.createPrintDocumentAdapter("datasheet-"+pageNo);
        String JobName=getString(R.string.app_name) +"Document";
        printManager.print(JobName,adapter,new PrintAttributes.Builder().build());
        printPress = TRUE;
    }
    public void OnViewBack(View view) {
        printPress = FALSE;
        backBtn.setVisibility(View.GONE);
        createBtn.setVisibility(View.GONE);
        regularBtn.setVisibility(View.VISIBLE);
        occBtn.setVisibility(View.VISIBLE);
        webViewDefault.setVisibility(View.GONE);
        inputConstraint.setVisibility(View.VISIBLE);
    }

    public void OnViewRegular(View view) {
        boolean dataEntry = getAllData() ;
        if(dataEntry){
            GenerateRegData(pageNo, snakeId, mStartDate, mEndDate);
        }
    }
    public void OnViewOccasional(View view) {
        boolean dataEntry = getAllData() ;
        if(dataEntry){
            GenerateOccData(pageNo, snakeId, mStartDate, mEndDate);
        }
    }

    private void WebLayoutDisplay() {
        backBtn.setVisibility(View.VISIBLE);
        createBtn.setVisibility(View.VISIBLE);
        regularBtn.setVisibility(View.GONE);
        occBtn.setVisibility(View.GONE);
        webViewDefault.setVisibility(View.VISIBLE);
        inputConstraint.setVisibility(View.GONE);
    }


    private boolean getAllData() {
        pageNo = Objects.requireNonNull(pageNoTIE.getText()).toString().trim() ;
        snakeId = Objects.requireNonNull(snakeIdTIE.getText()).toString().trim() ;

        if(pageNo.equals("") && snakeId.equals("") ){
            Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_SHORT).show();
            return false;
        }else if(mStartDate.equals("") && mEndDate.equals("") ){
            Toast.makeText(this, "Please select the date", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void GenerateRegData(String pageNo, String snakeName, String startDate, String endDate) {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put("snake_name", snakeName) ;
        map.put("start_date", startDate) ;
        map.put("end_date", endDate) ;
        AppController.getAppController().getAppNetworkController().makeRequest(Config.GENER_REG_DATA,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString("error").equalsIgnoreCase("false")){
                            WebLayoutDisplay();
                            JSONArray grpArray = object.getJSONArray("data") ;
                            for(int i=0; i<grpArray.length(); i++){
                                JSONObject mGrpObject = grpArray.getJSONObject(i);
                                reguList.add(new BuilderClass()
                                        .setFood_weight(mGrpObject.getString("food_weight"))
                                        .setFood_num(mGrpObject.getString("food_num"))
                                        .setFd_sts_name(mGrpObject.getString("fd_sts_names"))
                                        .setFood_name(mGrpObject.getString("food_names"))
                                        .setAct_name(mGrpObject.getString("act_names"))
                                        .setExc_name(mGrpObject.getString("exc_names"))
                                        .setHealth_status(mGrpObject.getString("health_status"))
                                        .setCleaning(mGrpObject.getString("cleaning"))
                                        .setVenom_collection(mGrpObject.getString("venom_collection"))
                                        .setShed_name(mGrpObject.getString("shed_names"))
                                        .setMedi_details(mGrpObject.getString("medi_details"))
                                        .setReg_date(mGrpObject.getString("reg_date"))
                                        .setReg_time(mGrpObject.getString("reg_time"))
                                        .build());
                            }
                            webViewDefault.loadDataWithBaseURL(null,StringRegDefault(reguList),"text/html","utf-8",null);
                            webView.loadDataWithBaseURL(null,StringReg(pageNo, snakeId, reguList),"text/html","utf-8",null);


                        }else{
                            Toast.makeText(PdfViewActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                }, map );


    }
    private void GenerateOccData(String pageNo, String snakeName, String startDate, String endDate) {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put("snake_name", snakeName) ;
        map.put("start_date", startDate) ;
        map.put("end_date", endDate) ;
        AppController.getAppController().getAppNetworkController().makeRequest(Config.GENER_OCC_DATA,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString("error").equalsIgnoreCase("false")){
                            WebLayoutDisplay();
                            JSONObject jsonObject = object.getJSONObject("detail");

                            JSONArray grpArray = object.getJSONArray("data") ;
                            for(int i=0; i<grpArray.length(); i++){
                                JSONObject mGrpObject = grpArray.getJSONObject(i);
                                occList.add(new BuilderClass()
                                        .setAge(mGrpObject.getString("age"))
                                        .setWeight_gm(mGrpObject.getString("weight_gm"))
                                        .setHead(mGrpObject.getString("head"))
                                        .setBody(mGrpObject.getString("body"))
                                        .setTail(mGrpObject.getString("tail"))
                                        .setTotal(mGrpObject.getString("total"))
                                        .setDorsal(mGrpObject.getString("dorsal"))
                                        .setVentral(mGrpObject.getString("ventral"))
                                        .setAnal(mGrpObject.getString("anal"))
                                        .setSub_caudal(mGrpObject.getString("sub_caudal"))
                                        .setNotes(mGrpObject.getString("notes"))
                                        .setOcc_date(mGrpObject.getString("occ_date"))
                                        .setOcc_time(mGrpObject.getString("occ_time"))
                                        .build());
                            }
                            webViewDefault.loadDataWithBaseURL(null,StringOccDefault(jsonObject,  occList),"text/html","utf-8",null);
                            webView.loadDataWithBaseURL(null,StringOcc(pageNo, snakeId, jsonObject, occList),"text/html","utf-8",null);
                        }else{
                            Toast.makeText(PdfViewActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {

                }, map );


    }

    private String StringReg(String pageNo, String snakeId, List<BuilderModel> reguList){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        String head = HtmlHeadReg();
        String tableTitle = HtmlTableTitleReg();
        String htmlHead = head+ "  <body>\n" +
                "    <div class=\"div-header\">\n" +
                "      <div>\n" +
                "        <img\n" +
                "          src=\"https://vrcbangladesh.org/static/media/logo_vrc.c1ca7ef7.png\"\n" +
                "          alt=\"VRC Logo\"\n" +
                "          width=\"70\"\n" +
                "        />\n" +
                "      </div>\n" +
                "      <div class=\"div-title\">\n" +
                "        <h3>Venom Research Centre, Bangladesh</h3>\n" +
                "        <p class=\"title-sub\">Department of Medicine</p>\n" +
                "        <p class=\"title-sub\">Chittagong Medical College</p>\n" +
                "        <h3>Data Sheet v6.0</h3>\n" +
                "      </div>\n" +
                "      <div class=\"div-page\">\n" +
                "        <p class=\"page-num\">Page No: <b>"+pageNo+"</b></p>\n" +
                "        <p class=\"page-id\"><b>"+snakeId+"</b></p>\n" +
                "        <p class=\"page-date\">Date: "+currentDate+"</p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" + tableTitle ;

        String htmlFooter = HtmlFooterReg(reguList);

        return String.format("%s%s", htmlHead, htmlFooter);
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

    private String HtmlFooterReg(List<BuilderModel> reguList) {
        String htmlBody = "" ;
        for(int i =0; i< reguList.size(); i++){
            BuilderModel dataModel = reguList.get(i);
            String tableView = "<tr>\n" +
                    "          <td>"+dataModel.getReg_date() +" " + dataModel.getReg_time() +"</td>\n" +
                    "          <td>"+dataModel.getFood_weight()+"</td>\n" +
                    "          <td>"+dataModel.getFood_num()+"</td>\n" +
                    "          <td>"+dataModel.getFood_name()+"</td>\n" +
                    "          <td>"+dataModel.getFd_sts_name()+"</td>\n" +
                    "          <td>"+dataModel.getAct_name()+"</td>\n" +
                    "          <td>"+dataModel.getExc_name()+"</td>\n" +
                    "          <td>"+dataModel.getHealth_status()+"</td>\n" +
                    "          <td>"+dataModel.getCleaning()+"</td>\n" +
                    "          <td>"+dataModel.getVenom_collection()+"</td>\n" +
                    "          <td>"+dataModel.getShed_name()+"</td>\n" +
                    "          <td>"+dataModel.getMedi_details()+"</td>\n" +
                    "        </tr>";
            htmlBody = String.format("%s%s", htmlBody, tableView);
        }
        String htmlFooter = "</table>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";

        return String.format("%s%s", htmlBody, htmlFooter);
    }
    private String HtmlTableTitleReg() {
        return "    <div>\n" +
                "      <table>\n" +
                "        <tr>\n" +
                "          <th>Date & Time</th>\n" +
                "          <th>Food Weight(gm)</th>\n" +
                "          <th>Food Qty.</th>\n" +
                "          <th>Food Type</th>\n" +
                "          <th>Food Status</th>\n" +
                "          <th>Activity During Check</th>\n" +
                "          <th>Excretion</th>\n" +
                "          <th>Health Status</th>\n" +
                "          <th>Cleaning</th>\n" +
                "          <th>Venom Collection</th>\n" +
                "          <th>Shedding Status</th>\n" +
                "          <th>Medication</th>\n" +
                "        </tr>";
    }
    private String HtmlHeadReg() {
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
                "        padding: 50px 150px 0px 200px;\n" +
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
                "  </head>\n" +
                "\n";
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

    private String StringOcc(String pageNo, String snakeId, JSONObject jsonObject, List<BuilderModel> occList){
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String head = HtmlHeadOcc() ;
        String htmlBody = "  <body>\n" +
                "    <div class=\"div-1\"></div>\n" +
                "    <div class=\"div-header\">\n" +
                "      <div>\n" +
                "        <img\n" +
                "          src=\"https://vrcbangladesh.org/static/media/logo_vrc.c1ca7ef7.png\"\n" +
                "          alt=\"Girl in a jacket\"\n" +
                "          width=\"70\"\n" +
                "        />\n" +
                "      </div>\n" +
                "      <div class=\"div-title\">\n" +
                "        <h3>Venom Research Centre</h3>\n" +
                "        <h3>Morphometry of Snakes</h3>\n" +
                "        <h3>Data Sheet v6.0</h3>\n" +
                "      </div>\n" +
                "      <div class=\"div-page\">\n" +
                "        <p class=\"page-num\">Sheet No: <b>"+pageNo+"</b></p>\n" +
                "        <p class=\"page-id\"><b>"+snakeId+"</b></p>\n" +
                "        <p class=\"page-date\">Date: "+currentDate+"</p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" ;
        String newSnakePart = "" ;
        if(checkBox.isChecked()){
            newSnakePart = NewSnakeOccPart(jsonObject) ;
        }
        String footerTitle = FooterTitleOcc() ;

        String htmlHead = head+ htmlBody+
                newSnakePart+
                footerTitle;
        String footerData = GetFooterData(occList) ;

        return String.format("%s%s", htmlHead, footerData);

    }
    private String StringOccDefault(JSONObject jsonObject, List<BuilderModel> occList){
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
        String newSnakePart = "" ;
        if(checkBox.isChecked()){
            newSnakePart = NewSnakeOccPart(jsonObject) ;
        }
        String footerTitle = FooterTitleOcc() ;

        String htmlHead = head+ htmlBody+
                newSnakePart+
                footerTitle;
        String footerData = GetFooterData(occList) ;
        return String.format("%s%s", htmlHead, footerData);
    }

    private String FooterTitleOcc() {
        return "      <table>\n" +
                "        <tr>\n" +
                "          <th rowspan=\"2\">Date</th>\n" +
                "          <th rowspan=\"2\">Age</th>\n" +
                "          <th colspan=\"4\">Length(cm)</th>\n" +
                "          <th colspan=\"4\">Scales</th>\n" +
                "          <th colspan=\"2\"></th>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <th>Head</th>\n" +
                "          <th>Body</th>\n" +
                "          <th>Tail</th>\n" +
                "          <th>Total</th>\n" +
                "          <th>Dorsal</th>\n" +
                "          <th>Ventral</th>\n" +
                "          <th>Anal</th>\n" +
                "          <th>Sub-Caudal</th>\n" +
                "          <th>Weight(gm)</th>\n" +
                "        </tr>";
    }
    private String NewSnakeOccPart(JSONObject jsonObject) {
        try {
            return "    <div>\n" +
                    "      <table>\n" +
                    "        <tr>\n" +
                    "          <th class=\"half-width\">Sex: "+jsonObject.getString("sex")+"</th>\n" +
                    "          <th class=\"half-width\">Age During Collection: "+jsonObject.getString("age_during_collection")+"</th>\n" +
                    "        </tr>\n" +
                    "\n" +
                    "        <tr>\n" +
                    "          <th class=\"half-width\">Date of Collection: "+jsonObject.getString("date_of_collection")+"</th>\n" +
                    "          <th class=\"half-width\">Place of Collection: "+jsonObject.getString("place_of_collection")+"</th>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <th colspan=\"2\" class=\"half-width\">\n" +
                    "            Notes: "+jsonObject.getString("notes")+"\n" +
                    "          </th>\n" +
                    "        </tr>\n" +
                    "      </table>\n" +
                    "      <br />\n" +
                    "      <table>\n" +
                    "        <thead>\n" +
                    "          <tr>\n" +
                    "            <th colspan=\"9\">Head Scalation</th>\n" +
                    "          </tr>\n" +
                    "        </thead>\n" +
                    "        <thead>\n" +
                    "          <tr>\n" +
                    "            <th>Chin-Sheild(A)</th>\n" +
                    "            <th>Chin-Sheild(P)</th>\n" +
                    "            <th>Parietal</th>\n" +
                    "            <th>Frontal</th>\n" +
                    "            <th>Prefrontal</th>\n" +
                    "            <th>Loreal</th>\n" +
                    "            <th>Mental</th>\n" +
                    "            <th>Nasal</th>\n" +
                    "            <th>Internasal</th>\n" +
                    "          </tr>\n" +
                    "        </thead>\n" +
                    "\n" +
                    "        <tbody>\n" +
                    "          <tr>\n" +
                    "            <td>"+jsonObject.getString("chin_sheild_A")+"</td>\n" +
                    "            <td>"+jsonObject.getString("chin_sheild_B")+"</td>\n" +
                    "            <td>"+jsonObject.getString("parietal")+"</td>\n" +
                    "            <td>"+jsonObject.getString("frontal")+"</td>\n" +
                    "            <td>"+jsonObject.getString("prefrontal")+"</td>\n" +
                    "            <td>"+jsonObject.getString("loreal")+"</td>\n" +
                    "            <td>"+jsonObject.getString("mental")+"</td>\n" +
                    "            <td>"+jsonObject.getString("nasal")+"</td>\n" +
                    "            <td>"+jsonObject.getString("internasal")+"</td>\n" +
                    "          </tr>\n" +
                    "        </tbody>\n" +
                    "\n" +
                    "        <thead>\n" +
                    "          <tr>\n" +
                    "            <th>Rostral</th>\n" +
                    "            <th>Temporal(f)</th>\n" +
                    "            <th>Temporal(s)</th>\n" +
                    "            <th>Upper Labial</th>\n" +
                    "            <th>Lower Labial</th>\n" +
                    "            <th>Super ocular</th>\n" +
                    "            <th>Sub ocular</th>\n" +
                    "            <th>Pre ocular</th>\n" +
                    "            <th>Post ocular</th>\n" +
                    "          </tr>\n" +
                    "        </thead>\n" +
                    "\n" +
                    "        <tbody>\n" +
                    "          <tr>\n" +
                    "            <td>"+jsonObject.getString("rostral")+"</td>\n" +
                    "            <td>"+jsonObject.getString("temporal_f")+"</td>\n" +
                    "            <td>"+jsonObject.getString("temporal_s")+"</td>\n" +
                    "            <td>"+jsonObject.getString("upper_labial")+"</td>\n" +
                    "            <td>"+jsonObject.getString("lower_labial")+"</td>\n" +
                    "            <td>"+jsonObject.getString("supra_ocular")+"</td>\n" +
                    "            <td>"+jsonObject.getString("sub_ocular")+"</td>\n" +
                    "            <td>"+jsonObject.getString("pre_ocular")+"</td>\n" +
                    "            <td>"+jsonObject.getString("post_ocular")+"</td>\n" +
                    "          </tr>\n" +
                    "        </tbody>\n" +
                    "      </table>\n" +
                    "\n" +
                    "      <br />\n" +
                    "\n" ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String HtmlHeadOcc() {
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
                "        padding: 50px 150px 0px 200px;\n" +
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
                "  </head>\n" +
                "\n" ;
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
    private String GetFooterData(List<BuilderModel> occList) {
        String htmlBody = "" ;
        for(int i =0; i< occList.size(); i++){
            BuilderModel dataModel = occList.get(i);
            String tableView = "<tr>\n" +
                    "          <td>"+dataModel.getOcc_date() +" " + dataModel.getOcc_time() +"</td>\n" +
                    "          <td>"+dataModel.getAge()+"</td>\n" +
                    "          <td>"+dataModel.getHead()+"</td>\n" +
                    "          <td>"+dataModel.getBody()+"</td>\n" +
                    "          <td>"+dataModel.getTail()+"</td>\n" +
                    "          <td>"+dataModel.getTotal()+"</td>\n" +
                    "          <td>"+dataModel.getDorsal()+"</td>\n" +
                    "          <td>"+dataModel.getVentral()+"</td>\n" +
                    "          <td>"+dataModel.getAnal()+"</td>\n" +
                    "          <td>"+dataModel.getSub_caudal()+"</td>\n" +
                    "          <td>"+dataModel.getWeight_gm()+"</td>\n" +
                    "        </tr>";

            htmlBody = String.format("%s%s", htmlBody, tableView);
        }
        String htmlFooter = "</table>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
        return String.format("%s%s", htmlBody, htmlFooter);
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

        if(printPress){
            SimpleClass.CheckInActive(TRUE);
        }else{
            SimpleClass.CheckInActive(FALSE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SimpleClass.CheckInActive(TRUE);
    }

    public void OnPickStart(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, day) -> {
                    mStartDate  = year + "-" + (month+1) + "-" + day ;
                    mStartTV.setText(mStartDate);

                }, year, month, dayOfMonth);

        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


    }
    public void OnPickEnd(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, day) -> {
                    mEndDate  = year + "-" + (month+1) + "-" + day ;
                    mEndTV.setText(mEndDate);

                }, year, month, dayOfMonth);

        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


    }
}