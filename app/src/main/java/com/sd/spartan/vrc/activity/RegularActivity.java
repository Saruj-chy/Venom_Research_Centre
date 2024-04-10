package com.sd.spartan.vrc.activity;


import static com.sd.spartan.vrc.controller.AppConstants.ACT_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.CLEANING;
import static com.sd.spartan.vrc.controller.AppConstants.DATE;
import static com.sd.spartan.vrc.controller.AppConstants.DATE_TIME;
import static com.sd.spartan.vrc.controller.AppConstants.EXC_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FALSE;
import static com.sd.spartan.vrc.controller.AppConstants.FD_STS_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_NUM;
import static com.sd.spartan.vrc.controller.AppConstants.FOOD_WEIGHT;
import static com.sd.spartan.vrc.controller.AppConstants.HEALTH_STATUS;
import static com.sd.spartan.vrc.controller.AppConstants.HEALTH_STATUS_Pos;
import static com.sd.spartan.vrc.controller.AppConstants.INFO_TYPE;
import static com.sd.spartan.vrc.controller.AppConstants.LAT;
import static com.sd.spartan.vrc.controller.AppConstants.LONG;
import static com.sd.spartan.vrc.controller.AppConstants.MEDI_DETAILS;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_ID;
import static com.sd.spartan.vrc.controller.AppConstants.MEM_USERNAME;
import static com.sd.spartan.vrc.controller.AppConstants.REG_ID;
import static com.sd.spartan.vrc.controller.AppConstants.REG_TITLE;
import static com.sd.spartan.vrc.controller.AppConstants.REMARKS;
import static com.sd.spartan.vrc.controller.AppConstants.SHED_NAMES;
import static com.sd.spartan.vrc.controller.AppConstants.SNAKE_ID;
import static com.sd.spartan.vrc.controller.AppConstants.TIME;
import static com.sd.spartan.vrc.controller.AppConstants.TRUE;
import static com.sd.spartan.vrc.controller.AppConstants.VENOM_COLLECTION;
import static com.sd.spartan.vrc.controller.SimpleClass.ListViewItemCLick;
import static com.sd.spartan.vrc.controller.SimpleClass.SavedClickListen;
import static com.sd.spartan.vrc.controller.SimpleClass.dateTimeIntCheck;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sd.spartan.vrc.R;
import com.sd.spartan.vrc.constant.Config;
import com.sd.spartan.vrc.constant.ProgressHelper;
import com.sd.spartan.vrc.controller.AppConstants;
import com.sd.spartan.vrc.model.ClassModel;
import com.sd.spartan.vrc.model.PopupSureCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class RegularActivity extends AppCompatActivity {
    private ConstraintLayout foodDetailsCons, actDetailsCons, mediDetailsCons ;
    private LinearLayout noticeLL;
    private ImageView foodUpImg, foodDownImg, actUpImg, actDownImg, medUpImg, medDownImg, medAlarmImg ;
    private TextView mGroupNameTextView;
    private TextView mSnakeNameTextView;
    private TextView foodItemTV, foodStatusTV, actCheckTV, excTV, shedTV, mediTV, noticeDateTimeTV;
    private TextInputLayout healthStatusTIL;
    private TextInputEditText foodWeightTIE, foodNumbersTIE,  healthStatusTIE, mediNameTIE,  qtyMediTIE, mediDetailsTIE, remarksTIE, dateTIE, timeTIE, noticeMsgTIE;
    private Spinner healthStatusSpin, cleanSpin, venomCollectSpin;
    private Button savedBtn ;

    private String mSnakeId;
    private String reg_id;
    private boolean healthStatusBool = false, noticeLayoutShow = false ;
    private String food_num;
    private String food_weight;
    private String cleaning;
    private String venom_collection;
    private String medi_details;
    private String remarks;
    private String food_names="";
    private String fd_sts_names="";
    private String act_names="";
    private String exc_names ="";
    private String shed_status_name ="";
    private final String medi_process="";
    private String health_status="" ;
    private String noticeMsg="", noticeDateTime ="";
    private int healthStatusPos = 0 ;

    private String dateReg, timeReg;

    private List<ClassModel> foodItemList ;
    private List<String> foodItemStrList ;
    private List<ClassModel> foodStatusList ;
    private List<String> foodStatusStrList ;
    private List<ClassModel> actDuringList ;
    private List<String> actDuringStrList ;
    private List<ClassModel> excList ;
    private List<String> excStrList ;
    private List<ClassModel> shedList ;
    private List<String> shedStrList ;
    private List<ClassModel> mediList ;
    private List<String> mediStrList ;


    //popup
    TextView titleLayoutTV;
    TextInputLayout newItemTIL ;
    TextInputEditText mNewItemTIE ;
    ListView mSearchListView ;
    Button savedLayoutBtn, newAddLayoutBtn ;

    private AlertDialog.Builder dialogBuilder  ;
    private AlertDialog dialog;
    private ArrayAdapter adapter ;

    LocationController locationController ;

    FloatingActionButton fab_main, fab_btn_1, fab_btn_2 ;
    LinearLayout linear_fab_1, linear_fab_2;
    boolean isFABOpen = false, insertBtn = true;
    private int year, month, day ;
    private String dateSelect ="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular);
        SimpleClass.CheckInActive(TRUE);

        Initialize() ;
        Toolbar toolbar = findViewById(R.id.appbar) ;
        setSupportActionBar(toolbar) ;
        ActionBar actionBar = getSupportActionBar() ;
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true) ;
        actionBar.setDisplayShowCustomEnabled(true) ;
        actionBar.setTitle(REG_TITLE) ;
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        GetStringExtra() ;
        LoadAllItems();
        SpinnerData() ;
    }

    private void SpinnerData() {
        healthStatusSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                healthStatusPos = position ;
                if((position+1) == (getResources().getStringArray(R.array.health_status)).length){
                    healthStatusTIL.setVisibility(View.VISIBLE);
                    healthStatusBool = true ;
                }else{
                    healthStatusTIL.setVisibility(View.GONE);
                    healthStatusBool = false ;
                }
                if(position != 0){
                    health_status = (String) parent.getItemAtPosition(position) ;
                }else{
                    health_status = "";
                }
                if(healthStatusPos==3){
                    Toast.makeText(RegularActivity.this, "Are you confirm, this snake is dead!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cleanSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    cleaning = (String) parent.getItemAtPosition(position) ;
                }else{
                    cleaning = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        venomCollectSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != 0){
                    venom_collection = (String) parent.getItemAtPosition(position) ;
                }else{
                    venom_collection = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void GetStringExtra() {
        String mGroupName = getIntent().getStringExtra("group_name");
        mSnakeId = getIntent().getStringExtra("snake_id");
        String mSnakeName = getIntent().getStringExtra("snake_name");
        mGroupNameTextView.setText(mGroupName);
        mSnakeNameTextView.setText(mSnakeName);
    }


    private void LoadAllItems() {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;

        AppController.getAppController().getAppNetworkController().makeRequest(Config.GET_REGULAR_DATA, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("error").equalsIgnoreCase("false")){
                    JSONArray JO1 = object.getJSONArray("food_item") ;
                    for(int i=0; i<=JO1.length(); i++) {
                        ClassModel buildModel ;
                        if(i==JO1.length()){
                            buildModel = new ClassModel("", "Others" ) ;
                        }else{
                            JSONObject mObject = JO1.getJSONObject(i);
                            buildModel = new ClassModel(mObject.getString("food_id"), mObject.getString("food_name")) ;
                        }
                        foodItemList.add(buildModel) ;
                    }

                    JSONArray JO2 = object.getJSONArray("food_status") ;
                    for(int i=0; i<=JO2.length(); i++) {
                        ClassModel buildModel ;
                        if(i==JO2.length()){
                            buildModel = new ClassModel("", "Others" ) ;
                        }else{
                            JSONObject mObject = JO2.getJSONObject(i);
                            buildModel = new ClassModel(mObject.getString("fd_sts_id"), mObject.getString("fd_sts_name")) ;

                        }
                        foodStatusList.add(buildModel) ;
                    }
                    JSONArray JO3 = object.getJSONArray("act_during") ;
                    for(int i=0; i<=JO3.length(); i++) {
                        ClassModel buildModel ;
                        if(i==JO3.length()){
                            buildModel = new ClassModel("", "Others" ) ;

                        }else{
                            JSONObject mObject = JO3.getJSONObject(i);
                            buildModel = new ClassModel(mObject.getString("act_id"), mObject.getString("act_name")) ;
                        }
                        actDuringList.add(buildModel) ;
                    }
                    JSONArray JO4 = object.getJSONArray("excretion") ;
                    for(int i=0; i<=JO4.length(); i++) {
                        ClassModel buildModel ;
                        if(i==JO4.length()){
                            buildModel = new ClassModel("", "Others" ) ;
                        }else{
                            JSONObject mObject = JO4.getJSONObject(i);
                            buildModel = new ClassModel(mObject.getString("exc_id"), mObject.getString("exc_name")) ;
                        }
                        excList.add(buildModel) ;
                    }
                    JSONArray JO5 = object.getJSONArray("shed_status") ;
                    for(int i=0; i<=JO5.length(); i++) {
                        ClassModel buildModel ;
                        if(i==JO5.length()){
                            buildModel = new ClassModel("", "Others" ) ;
                        }else{
                            JSONObject mObject = JO5.getJSONObject(i);
                            buildModel = new ClassModel(mObject.getString("shed_id"), mObject.getString("shed_name")) ;
                        }
                        shedList.add(buildModel) ;
                    }
                    JSONArray JO6 = object.getJSONArray("medication") ;
                    for(int i=0; i<=JO6.length(); i++) {
                        ClassModel buildModel ;
                        if(i==JO6.length()){
                            buildModel = new ClassModel("", "Others") ;
//
                        }else{
                            JSONObject mObject = JO6.getJSONObject(i);
                            buildModel = new ClassModel(mObject.getString("medi_id"), mObject.getString("medi_name")) ;
//
                        }
                        mediList.add(buildModel) ;
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }, map );
    }

    public void OnFoodItems(View view) {
        foodItemStrList.clear();
        CreatePopupDialogList(foodItemList,  "Food Items") ;
        ListViewItemCLick(foodItemList, foodItemStrList, mSearchListView, newItemTIL, savedLayoutBtn, newAddLayoutBtn);
        SavedClickListen(foodItemStrList, foodItemTV, titleLayoutTV, savedLayoutBtn, dialog) ;
        NewAddButtonListener("Food Items", "food_id", "food_name", foodItemList, 1) ;

    }
    public void OnFoodStatus(View view) {
        foodStatusStrList.clear();
        CreatePopupDialogList(foodStatusList,  "Food Status") ;
        ListViewItemCLick(foodStatusList, foodStatusStrList, mSearchListView, newItemTIL, savedLayoutBtn, newAddLayoutBtn);
        SavedClickListen(foodStatusStrList, foodStatusTV, titleLayoutTV, savedLayoutBtn, dialog) ;
        NewAddButtonListener("Food Status", "fd_sts_id", "fd_sts_name", foodStatusList, 2) ;


    }
    public void OnActDuringCheck(View view) {
        actDuringStrList.clear();
        CreatePopupDialogList(actDuringList,  "Activity During Check") ;
        ListViewItemCLick(actDuringList, actDuringStrList, mSearchListView, newItemTIL, savedLayoutBtn, newAddLayoutBtn);
        SavedClickListen(actDuringStrList, actCheckTV, titleLayoutTV, savedLayoutBtn, dialog) ;
        NewAddButtonListener("Activity During Check", "act_id", "act_name", actDuringList, 3) ;

    }
    public void OnExcretion(View view) {
        excStrList.clear();
        CreatePopupDialogList(excList,  "Excretion") ;
        ListViewItemCLick(excList, excStrList, mSearchListView, newItemTIL, savedLayoutBtn, newAddLayoutBtn);
        SavedClickListen(excStrList, excTV, titleLayoutTV, savedLayoutBtn, dialog) ;
        NewAddButtonListener("Excretion", "exc_id", "exc_name", excList, 4) ;
    }
    public void OnSheddingStatus(View view) {
        shedStrList.clear();
        CreatePopupDialogList(shedList,  "Shedding Status") ;
        ListViewItemCLick(shedList, shedStrList, mSearchListView, newItemTIL, savedLayoutBtn, newAddLayoutBtn);
        SavedClickListen(shedStrList, shedTV, titleLayoutTV, savedLayoutBtn, dialog) ;
        NewAddButtonListener("Shedding Status", "shed_id", "shed_name", shedList, 5) ;
    }
    public void OnMedication(View view) {
        mediStrList.clear();
        CreatePopupDialogList(mediList,  "Medication") ;
        ListViewItemCLick(mediList, mediStrList, mSearchListView, newItemTIL, savedLayoutBtn, newAddLayoutBtn);
        SavedClickListen(mediStrList, mediTV, titleLayoutTV, savedLayoutBtn, dialog) ;
        NewAddButtonListener("Medication", "medi_id", "medi_name", mediList, 6) ;

    }

    //image click
    public void OnFoodDownImg(View view) {
        ShowDetailsImg(foodDetailsCons, foodUpImg, foodDownImg, true) ;
    }
    public void OnFoodUpImg(View view) {
        ShowDetailsImg(foodDetailsCons, foodUpImg, foodDownImg, false) ;

    }
    public void OnActDownImg(View view) {
        ShowDetailsImg(actDetailsCons, actUpImg, actDownImg, true) ;
    }
    public void OnActUpImg(View view) {
        ShowDetailsImg(actDetailsCons, actUpImg, actDownImg, false) ;
    }
    public void onMedDownImg(View view) {
        ShowDetailsImg(mediDetailsCons, medUpImg, medDownImg, true) ;
    }
    public void OnMedUpImg(View view) {
        ShowDetailsImg(mediDetailsCons, medUpImg, medDownImg, false) ;
    }
    private void ShowDetailsImg(ConstraintLayout cons, ImageView upImg, ImageView downImg, boolean show) {
        if(show){
            cons.setVisibility(View.VISIBLE);
            upImg.setVisibility(View.VISIBLE);
            downImg.setVisibility(View.GONE);
        }else{
            cons.setVisibility(View.GONE);
            upImg.setVisibility(View.GONE);
            downImg.setVisibility(View.VISIBLE);
        }
    }
    public void OnMedAddImg(View view) {
        String medProcess = "", mediDetails;
        String medName = Objects.requireNonNull(mediNameTIE.getText()).toString().trim();
        String doseQty = Objects.requireNonNull(qtyMediTIE.getText()).toString().trim();
        if( !medName.equalsIgnoreCase("") || !doseQty.equalsIgnoreCase("") || mediTV.getTag() != null ){
            if(mediTV.getTag() != null){
                medProcess = mediTV.getText().toString().trim() ;
            }

            if(Objects.requireNonNull(mediDetailsTIE.getText()).toString().trim().equalsIgnoreCase("")){
                mediDetails = doseQty+" "+ medName+ " " + medProcess ;
            }else{
                mediDetails = mediDetailsTIE.getText().toString().trim()+", \n" +
                        doseQty+" "+ medName+ " " + medProcess ;
            }
            ClearMedicationDetails(mediDetails) ;
        }
        mediNameTIE.setText("");
        qtyMediTIE.setText("");
        mediTV.setTag(null);
    }
    private void ClearMedicationDetails(String details) {
        mediDetailsTIE.setVisibility(View.VISIBLE);
        mediDetailsTIE.setText(details);
        mediDetailsCons.setVisibility(View.GONE);
        mediNameTIE.setText("");
        mediTV.setText(getResources().getString(R.string.medi_process));
        mediTV.setTag(null);
        qtyMediTIE.setText("");
        ShowDetailsImg(mediDetailsCons, medUpImg, medDownImg, false) ;
    }


    private void NewAddButtonListener(String title, String dataNameId, String dataNameTitle, List<ClassModel> itemList, int typeId) {
        newAddLayoutBtn.setOnClickListener(view -> {
            String data = Objects.requireNonNull(mNewItemTIE.getText()).toString().trim() ;
            if(data.equalsIgnoreCase("")){
                Toast.makeText(RegularActivity.this, "Please fill the "+title, Toast.LENGTH_SHORT).show();
            }else{
                PopupSureCheck.setTitle(title) ;
                PopupSureCheck.setDetail("Are you sure you want to add?");
                PopupSureCheck.CreatePopup(RegularActivity.this, view1 -> {
                    PopupSureCheck.ll.setVisibility(View.GONE);
                    PopupSureCheck.progress.setVisibility(View.VISIBLE);
                    InsertNewItemData(data, dataNameId, dataNameTitle, itemList, typeId ) ;
                }, view12 -> PopupSureCheck.dialog.cancel());
            }



        });
    }
    private void InsertNewItemData(String data, String dataNameId, String dataNameTitle, List<ClassModel> itemList, int typeId) {
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(dataNameTitle, data) ;
        map.put("type_id", typeId+"") ;

        AppController.getAppController().getAppNetworkController().makeRequest(Config.INSERT_FOOD_ITEM, response -> {
            PopupSureCheck.dialog.cancel();
            PopupSureCheck.progress.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("error").equalsIgnoreCase("false")){
                    itemList.clear();
                    newItemTIL.setVisibility(View.GONE);
                    mNewItemTIE.setText("");
                    newAddLayoutBtn.setVisibility(View.GONE);
                    savedLayoutBtn.setVisibility(View.VISIBLE);


                    JSONArray JO1 = object.getJSONArray("data") ;
                    for(int i=0; i<=JO1.length(); i++) {
                        ClassModel buildModel ;
                        if(i==JO1.length()){
                            buildModel = new ClassModel("", "Others" ) ;
                        }else{
                            JSONObject mObject = JO1.getJSONObject(i);
                            buildModel = new ClassModel(mObject.getString(dataNameId), mObject.getString(dataNameTitle)) ;
                        }
                        itemList.add(buildModel) ;
                        adapter = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_list_item_single_choice, getStringArrayList(itemList));
                        mSearchListView.setAdapter(adapter);
                        mSearchListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    }
                }
                AppController.getAppController().getInAppNotifier().showToast(object.getString("msg"));


            } catch (JSONException ignored) {
            }
        }, error -> {
        }, map );
    }
    private void CreatePopupDialogList(List<ClassModel> itemList, String title)
    {
        View view = getLayoutInflater().inflate(R.layout.layout_popup, null);
        titleLayoutTV = view.findViewById(R.id.text_title) ;
        mSearchListView = view.findViewById(R.id.listview_popup);
        newItemTIL = view.findViewById(R.id.text_name_layout);
        mNewItemTIE = view.findViewById(R.id.edit_name_layout);
        savedLayoutBtn = view.findViewById(R.id.btn_saved_layout);
        newAddLayoutBtn = view.findViewById(R.id.btn_new_add_layout);

        titleLayoutTV.setText(title);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_single_choice, getStringArrayList(itemList));
        mSearchListView.setAdapter(adapter);
        mSearchListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


    }
    private List<String> getStringArrayList(List<ClassModel> itemList) {
        List<String> strArray = new ArrayList<>() ;

        for (int i=0; i<itemList.size(); i++){
            strArray.add(itemList.get(i).getName()) ;

        }
        return strArray;
    }

    private void Initialize() {
        foodDetailsCons = findViewById(R.id.constraint_food_details) ;
        actDetailsCons = findViewById(R.id.constraint_act_details) ;
        mediDetailsCons = findViewById(R.id.constraint_medi_details) ;

        foodUpImg = findViewById(R.id.image_up_food) ;
        foodDownImg = findViewById(R.id.image_down_food) ;
        actUpImg = findViewById(R.id.image_up_act) ;
        actDownImg = findViewById(R.id.image_down_act) ;
        medUpImg = findViewById(R.id.image_up_med) ;
        medDownImg = findViewById(R.id.image_down_med) ;
        medAlarmImg = findViewById(R.id.image_medi_alarm) ;

        mGroupNameTextView = findViewById(R.id.text_group_name_daily) ;
        mSnakeNameTextView = findViewById(R.id.text_snake_name_daily) ;

        foodItemTV = findViewById(R.id.text_food_items) ;
        foodStatusTV = findViewById(R.id.text_food_status) ;
        actCheckTV = findViewById(R.id.text_act_during_check) ;
        excTV = findViewById(R.id.text_exc) ;
        shedTV = findViewById(R.id.text_shed) ;
        mediTV = findViewById(R.id.text_medi) ;


        healthStatusTIL = findViewById(R.id.text_input_health_status) ;

        foodWeightTIE = findViewById(R.id.edit_food_weight) ;
        foodNumbersTIE = findViewById(R.id.edit_numbers) ;
        healthStatusTIE = findViewById(R.id.edit_health_status) ;
        mediNameTIE = findViewById(R.id.edit_medi_name) ;
        qtyMediTIE = findViewById(R.id.edit_medi_qty) ;
        mediDetailsTIE = findViewById(R.id.edit_medi_details) ;
        remarksTIE = findViewById(R.id.edit_remarks) ;
        dateTIE = findViewById(R.id.edit_date) ;
        timeTIE = findViewById(R.id.edit_time) ;
        noticeLL = findViewById(R.id.linear_dose_notice) ;
        noticeMsgTIE = findViewById(R.id.edit_notice_msg) ;
        noticeDateTimeTV = findViewById(R.id.text_notice_time) ;

        healthStatusSpin = findViewById(R.id.spinner_health_status) ;
        cleanSpin = findViewById(R.id.spinner_cleaning) ;
        venomCollectSpin = findViewById(R.id.spinner_venom_colection) ;
        savedBtn = findViewById(R.id.btn_daily_saved) ;

        linear_fab_1 =  findViewById(R.id.linear_fab_1);
        linear_fab_2 =  findViewById(R.id.linear_fab_2);
        fab_main =  findViewById(R.id.fab_main);
        fab_btn_1 =  findViewById(R.id.fab_btn_1);
        fab_btn_2 =  findViewById(R.id.fab_btn_2);

        foodItemList = new ArrayList<>() ;
        foodStatusList = new ArrayList<>() ;
        actDuringList = new ArrayList<>() ;
        excList = new ArrayList<>() ;
        shedList = new ArrayList<>() ;
        mediList = new ArrayList<>() ;

        foodItemStrList = new ArrayList<>() ;
        foodStatusStrList = new ArrayList<>() ;
        actDuringStrList = new ArrayList<>() ;
        excStrList = new ArrayList<>() ;
        shedStrList = new ArrayList<>() ;
        mediStrList = new ArrayList<>() ;

        dialogBuilder = new AlertDialog.Builder(this);
        locationController = new LocationController(this) ;

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }

    public void onRegularDataEntry(View view) {
        closeFABMenu() ;
        boolean getData = getAllRegularData() ;
        if(getData){
            locationController.getLastLocation();

            if( LocationController.latitude==0 || LocationController.longitude==0 ){
                locationController.getLastLocation();
                Toast.makeText(this, "Please turn on location ", Toast.LENGTH_SHORT).show();
            }
            //date and time select ekn off, because old data entry complete
//            else if(dateRegError){
//                Toast.makeText(this, "Please correct your date ", Toast.LENGTH_SHORT).show();
//            }
//            else if(timeRegError){
//                Toast.makeText(this, "Please correct your time ", Toast.LENGTH_SHORT).show();
//            }
            else {
                PopupSureCheck.setTitle("Regular Data");
                if(insertBtn){
                    PopupSureCheck.setDetail("Are you sure you want to insert the data");
                }else{
                    PopupSureCheck.setDetail("Are you sure you want to update the data");
                }
                PopupSureCheck.CreatePopup(this, view1 -> { //yes button
                    PopupSureCheck.ll.setVisibility(View.GONE);
                    PopupSureCheck.progress.setVisibility(View.VISIBLE);
                    if(insertBtn){
                        InsertData(Config.INSERT_REGULAR_DATA);
                    }else{
                        InsertData(Config.UPDATE_REGULAR_DATA) ;
                    }
                }, view12 -> { // no button
                    PopupSureCheck.dialog.cancel();
                });
            }
        }
    }

    private void InsertData(String URL) {
        AppController.getAppController().getAppNetworkController().makeRequest(URL, response -> {
            PopupSureCheck.dialog.cancel();
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("error").equalsIgnoreCase("false")){
                    onBackPressed();
                }
                AppController.getAppController().getInAppNotifier().showToast(object.getString("msg"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> dialog.cancel(), hashmap() );
    }
    private HashMap<String, String> hashmap(){
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(SNAKE_ID, mSnakeId) ;
        map.put(FOOD_WEIGHT, food_weight) ;
        map.put(FOOD_NUM, food_num) ;
        map.put(FOOD_NAMES, food_names) ;
        map.put(FD_STS_NAMES, fd_sts_names) ;
        map.put(ACT_NAMES, act_names) ;
        map.put(EXC_NAMES, exc_names) ;
        map.put(HEALTH_STATUS, health_status) ;
        map.put(HEALTH_STATUS_Pos, healthStatusPos+"") ;
        map.put(CLEANING, cleaning) ;
        map.put(VENOM_COLLECTION, venom_collection) ;
        map.put(SHED_NAMES, shed_status_name) ;
        map.put(MEDI_DETAILS, medi_details) ;
        map.put("notice_msg", noticeMsg) ;
        map.put("notice_date_time", noticeDateTime) ;
        map.put(REMARKS, remarks) ;

        map.put(MEM_ID, AppController.mem_id) ;
        map.put(MEM_USERNAME, AppController.mem_username) ;
        map.put(LAT, LocationController.latitude+"") ;
        map.put(LONG, LocationController.longitude+"" ) ;
        map.put(DATE, SimpleClass.GetCurrentDate() ) ;
        map.put(TIME, SimpleClass.GetCurrentTime() ) ;
        map.put(DATE_TIME, SimpleClass.GetCurrentDateTime() ) ;
        if(!insertBtn){
            map.put(REG_ID, reg_id) ;
        }

        return map;
    }

    private boolean getAllRegularData() {
        if (!Objects.requireNonNull(mediNameTIE.getText()).toString().equals("") || mediTV.getTag() != null || !Objects.requireNonNull(qtyMediTIE.getText()).toString().equals("")) {
            Toast.makeText(this, "Select the medication add button", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(foodItemTV.getTag()==null || foodStatusTV.getTag()==null || actCheckTV.getTag()==null || excTV.getTag()==null || shedTV.getTag()==null || Objects.requireNonNull(mediDetailsTIE.getText()).toString().trim().equals("") ){
            Toast.makeText(this, "You are not fill up all fields", Toast.LENGTH_SHORT).show();
        }

        if(foodItemTV.getTag() != null){
            food_names = foodItemTV.getText().toString().trim() ;
        }
        if(foodStatusTV.getTag() != null){
            fd_sts_names = foodStatusTV.getText().toString().trim() ;
        }
        if(actCheckTV.getTag() != null){
            act_names = actCheckTV.getText().toString().trim() ;
        }
        if(excTV.getTag() != null){
            exc_names = excTV.getText().toString().trim() ;
        }
        if(shedTV.getTag() != null){
            shed_status_name = shedTV.getText().toString().trim() ;
        }
        if(healthStatusBool){
            health_status = Objects.requireNonNull(healthStatusTIE.getText()).toString().trim() ;
        }
        if(cleaning.equals("")){
            cleaning = "Yes";
        }


        food_num = Objects.requireNonNull(foodNumbersTIE.getText()).toString().trim()  ;
        food_weight = Objects.requireNonNull(foodWeightTIE.getText()).toString().trim() ;
        medi_details = Objects.requireNonNull(mediDetailsTIE.getText()).toString().trim() ;
        remarks = Objects.requireNonNull(remarksTIE.getText()).toString().trim() ;
        dateReg = Objects.requireNonNull(dateTIE.getText()).toString().trim() ;
        timeReg = Objects.requireNonNull(timeTIE.getText()).toString().trim() ;
        noticeMsg = Objects.requireNonNull(noticeMsgTIE.getText()).toString().trim() ;

        String[] dateSplit = dateReg.split("-");
        String[] timeSplit = timeReg.split(":");

        if(dateSplit.length==3){
            if (dateSplit[0].length() == 4 && (dateTimeIntCheck(dateSplit[1]) > 0 && dateTimeIntCheck(dateSplit[1]) < 13)) {
                if (dateTimeIntCheck(dateSplit[2]) > 0) {
                    dateTimeIntCheck(dateSplit[2]);
                }
            }
        }
        if(timeSplit.length >=2 && timeSplit.length <=3){
            if ((dateTimeIntCheck(timeSplit[0]) >= 0 && dateTimeIntCheck(timeSplit[0]) < 24)) {
                if (dateTimeIntCheck(timeSplit[1]) >= 0) {
                    dateTimeIntCheck(timeSplit[1]);
                }
            }
        }
        return true;
    }




    //fab button click
    public void OnFabMainClick(View view) {
        if (!isFABOpen) {
            showFABMenu();
        } else {
            closeFABMenu();
        }
    }
    private void showFABMenu() {
        isFABOpen = true;
        linear_fab_1.setVisibility(View.VISIBLE);
        linear_fab_2.setVisibility(View.VISIBLE);
        fab_main.animate().rotationBy(90);
        linear_fab_1.animate().translationY(-getResources().getDimension(R.dimen.dimen_20));
        linear_fab_2.animate().translationY(-getResources().getDimension(R.dimen.dimen_70));
    }
    private void closeFABMenu() {
        isFABOpen = false;
        fab_main.animate().rotation(0);
        linear_fab_1.animate().translationY(0);
        linear_fab_2.animate().translationY(0);
        linear_fab_1.setVisibility(View.GONE);
        linear_fab_2.setVisibility(View.GONE);
    }
    public void OnUpdateFabClick(View view) {
        OnDateDialog(true) ;
    }
    public void OnAccessFabClick(View view) {
        OnDateDialog(false) ;

    }
    private void OnDateDialog(boolean updateBtn) {
        long maxTime = System.currentTimeMillis();
        long minTime = System.currentTimeMillis() - (3*24*60*60*1000) ;
        DatePickerDialog datepick = new DatePickerDialog(RegularActivity.this, (view, year, month, dayOfMonth) -> {

            Calendar selectCalendars = Calendar.getInstance();
            selectCalendars.set(year, month, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            dateSelect = simpleDateFormat.format(selectCalendars.getTime());


            if(updateBtn){
                RetriveData(11);
            }else{
                RetriveData(12);
            }


        },year,month,day);

        if(updateBtn){
            datepick.getDatePicker().setMinDate(minTime);
        }
        datepick.getDatePicker().setMaxDate(maxTime);
        datepick.show();
    }
    private void RetriveData(int infoType) {
        ClearAllData();
        ProgressHelper.isDialogVisible();
        ProgressHelper.showDialog(RegularActivity.this, "please wait...");
        HashMap<String, String> map = new HashMap<>() ;
        map.put(AppConstants.AUTH_TOKEN, AppController.auth_token) ;
        map.put(AppConstants.MEM_PHN_NUM, AppController.mem_phn_num) ;
        map.put(SNAKE_ID, mSnakeId) ;
        map.put(MEM_ID, AppController.mem_id) ;
        map.put(DATE, dateSelect) ;
        map.put(INFO_TYPE, infoType+"") ;


        AppController.getAppController().getAppNetworkController()
                .makeRequest(Config.RETRIVE_DATA_BY_DATE, response -> {
                    ProgressHelper.dismissDialog();
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString("error").equalsIgnoreCase("false")){
                            closeFABMenu() ;
                            savedBtn.setText(AppConstants.UPDATE);
                            insertBtn = false ;
                            JSONObject dataObj = object.getJSONObject("data");
                            reg_id = dataObj.getString(REG_ID) ;
                            food_weight = dataObj.getString(FOOD_WEIGHT) ;
                            food_num = dataObj.getString(FOOD_NUM) ;
                            food_names = dataObj.getString(FOOD_NAMES) ;
                            fd_sts_names = dataObj.getString(FD_STS_NAMES) ;
                            act_names = dataObj.getString(ACT_NAMES) ;
                            exc_names = dataObj.getString(EXC_NAMES) ;
                            health_status = dataObj.getString(HEALTH_STATUS) ;
                            cleaning = dataObj.getString(CLEANING) ;
                            venom_collection = dataObj.getString(VENOM_COLLECTION) ;
                            shed_status_name = dataObj.getString(SHED_NAMES) ;
                            medi_details = dataObj.getString(MEDI_DETAILS) ;
                            remarks = dataObj.getString(REMARKS) ;

                            dateReg = dataObj.getString("reg_date") ;
                            timeReg = dataObj.getString("reg_time") ;
                            dateTIE.setText(dateReg);
                            timeTIE.setText(timeReg);


                            foodWeightTIE.setText(food_weight);
                            foodNumbersTIE.setText(food_num);
                            TextviewNullCheck(foodItemTV, food_names) ;
                            TextviewNullCheck(foodStatusTV, fd_sts_names) ;
                            TextviewNullCheck(actCheckTV, act_names) ;
                            TextviewNullCheck(excTV, exc_names);
                            TextviewNullCheck(shedTV, shed_status_name);
                            TextviewNullCheck(mediTV, medi_process);
                            if(!medi_details.equalsIgnoreCase("")){
                                ClearMedicationDetails(medi_details);
                            }
                            remarksTIE.setText(remarks);


                            getSpinnerValueCheck(health_status, R.array.health_status, healthStatusSpin, healthStatusTIL, healthStatusTIE, true );
                            getSpinnerValueCheck(cleaning, R.array.cleaning, cleanSpin, null, null, false );
                            getSpinnerValueCheck(venom_collection, R.array.venom_collection, venomCollectSpin, null, null, false );
                        }else{
                            onBackPressed();
                            AppController.getAppController().getInAppNotifier().showToast(object.getString("msg"));
                        }
                    } catch (JSONException ignored) {
                    }
                }, error -> dialog.cancel(), map );
    }

    private void TextviewNullCheck(TextView textView, String name) {
        if(!name.equals("")){
            textView.setText(name);
            textView.setTag(1);
        }
    }

    private void ClearAllData() {
        foodWeightTIE.setText("");
        foodNumbersTIE.setText("");
        foodItemTV.setText(getResources().getString(R.string.food_items));
        foodStatusTV.setText(getResources().getString(R.string.food_status));
        actCheckTV.setText(getResources().getString(R.string.act_chk));
        excTV.setText(getResources().getString(R.string.exc));
        healthStatusSpin.setSelection(0);
        cleanSpin.setSelection(0);
        venomCollectSpin.setSelection(0);
        shedTV.setText(getResources().getString(R.string.shed_sts));
        mediTV.setText(getResources().getString(R.string.medi_process));
        mediNameTIE.setText("");
        remarksTIE.setText("");
        qtyMediTIE.setText("");


        noticeLL.setVisibility(View.GONE);
        medAlarmImg.setVisibility(View.GONE);
        dateTIE.setText("");
        timeTIE.setText("");

    }
    private void getSpinnerValueCheck(String name, int array, Spinner spinner, TextInputLayout inputText, TextInputEditText inputEdit, boolean layoutExist) {
        if(name.equalsIgnoreCase("")){
            spinner.setSelection(0);
        }else{
            String[] values = getResources().getStringArray(array);
            for (int i=1; i< values.length; i++){
                if(name.equalsIgnoreCase(values[i])){
                    healthStatusPos = i;
                    spinner.setSelection(i);
                    if(layoutExist){
                        inputText.setVisibility(View.GONE);
                    }
                    return;
                }else if(i == values.length-1){
                    spinner.setSelection(values.length-1);
                    if(layoutExist){
                        inputText.setVisibility(View.VISIBLE);
                        inputEdit.setText(name);
                    }

                }
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        closeFABMenu();
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
        closeFABMenu();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SimpleClass.CheckInActive(TRUE);
    }


    public void OnMedAlarmClick(View view) {
        noticeLayoutShow = !noticeLayoutShow;
        if(noticeLayoutShow){
            noticeLL.setVisibility(View.VISIBLE);
        }else{
            noticeLL.setVisibility(View.GONE);
            noticeMsgTIE.setText("");
            noticeDateTimeTV.setText(R.string.select_time);
            noticeDateTime = "";
        }
    }

    public void OnNoticeTimeClick(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year1, month1, dayOfMonth) -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(RegularActivity.this, (view11, hourOfDay, minute1) -> {
                String dateTime = year1 + "-" + (month1 +1) + "-" + day +" "+ hourOfDay+":"+ minute1;
                AppController.getAppController().getInAppNotifier().log("TAG", dateTime);
                noticeDateTimeTV.setText(dateTime);
                noticeDateTime = dateTime;
            }, hour, minute, true); // true for 24-hour time

            timePickerDialog.show();            }, year, month, day);

        datePickerDialog.show();
    }
}