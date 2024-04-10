package com.sd.spartan.vrc.controller;

import android.content.Context;
import android.content.SharedPreferences;


public class MySharedPreferenceManager {
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MySharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getUserNo() {

        return sharedPreferences.getString(AppConstants.USER_NO, "-1");
    }


    public String getUserLat() {
        return sharedPreferences.getString(AppConstants.LATITUDE, "0");
    }

    public String getUserLon() {
        return sharedPreferences.getString(AppConstants.LONGITUDE, "0");
    }

    public String getFCMToken(String userNo) {
        return sharedPreferences.getString(AppConstants.FCM_TOKEN + userNo, "");
    }

    public String getFCMToken() {
        return sharedPreferences.getString(AppConstants.FCM_TOKEN, "");
    }

    public void saveUserFCMToken(String userno, String token) {
        editor = sharedPreferences.edit();

        editor.putString(AppConstants.FCM_TOKEN + userno, token);
        editor.putString(AppConstants.FCM_TOKEN, token);

        editor.apply();
    }

    public void saveUserLatitude(String lat) {
        editor = sharedPreferences.edit();

        editor.putString(AppConstants.LATITUDE, lat);

        editor.apply();
    }

    public void saveUserLongitude(String lon) {
        editor = sharedPreferences.edit();

        editor.putString(AppConstants.LONGITUDE, lon);

        editor.apply();
    }

    public void saveLoggedIn(boolean flag) {
        editor = sharedPreferences.edit();

        editor.putBoolean(AppConstants.USER_LOGGED_IN, flag);

        editor.apply();
    }

    public boolean isLoggedIn(){

        return sharedPreferences.getBoolean(AppConstants.USER_LOGGED_IN, false);
    }

    public boolean hasSingleAccount() {
        return getUserCount()==1;
    }

    public boolean hasMultipleAccounts() {
        return getUserCount()>1;
    }

    public boolean hasNoAccounts() {
        return getUserCount()==0;
    }

    public int getUserCount(){
        return sharedPreferences.contains("user_count")?sharedPreferences.getInt("user_count", 0):0;
    }

    public String getSavedHost(String authkey) {
        return sharedPreferences.getString(AppConstants.USER_HOST, "");
    }

    public String getSavedDomain(String authkey) {
        return sharedPreferences.getString(AppConstants.USER_DOMAIN, "");

    }

    public String getSavedUsername(String authkey) {
        return sharedPreferences.getString(AppConstants.USER_USERNAME, "");

    }

    public void saveLoginInfo(String host, String domain, String username){
        editor = sharedPreferences.edit();

        editor.putString(AppConstants.USER_HOST, host);
        editor.putString(AppConstants.USER_DOMAIN, domain);
        editor.putString(AppConstants.USER_USERNAME, username);
        editor.putInt("user_count", 1);

        editor.apply();
    }

    public void saveLoginSuccessInfo(String shop_name, String topic_name, String auth_domain, String auth_key) {
        editor = sharedPreferences.edit();

        editor.putString(AppConstants.USER_SHOPNAME, shop_name);
        editor.putString(AppConstants.USER_TOPICNAME, topic_name);
        editor.putString(AppConstants.USER_AUTHDOMAIN, auth_domain);
        editor.putString(AppConstants.USER_AUTHKEY, auth_key);

        editor.apply();
    }

    public String getSavedShopName() {
        return sharedPreferences.getString(AppConstants.USER_SHOPNAME, "");

    }

    public String getSavedTopicName() {
        return sharedPreferences.getString(AppConstants.USER_TOPICNAME, "");

    }

    public String getSavedAuthDomain() {
        return sharedPreferences.getString(AppConstants.USER_AUTHDOMAIN, "");

    }

    public String getSavedAuthKey() {
        return sharedPreferences.getString(AppConstants.USER_AUTHKEY, "");

    }

    public void clearDB() {
        sharedPreferences.edit().clear().apply();
    }
}