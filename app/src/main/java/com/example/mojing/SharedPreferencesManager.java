package com.example.mojing;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;


public class SharedPreferencesManager {
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_Password = "userPassword";
    private static final String KEY_USER_Phone = "userPhone";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_USER_Role = "userRole";
    private static final String KEY_Figure_Shengao = "shengao";
    private static final String KEY_Figure_Tizhong = "tizhong";
    private static final String KEY_Figure_Tunwei = "tunwei";
    private static final String KEY_Figure_Xiongwei = "xiongwei";
    private static final String KEY_Figure_Yaowei = "yaowei";
    private static final String KEY_UserSignature = "未设置";
    private static final String KEY_UserGender = "女";
    private static final String KEY_IS_Youke = "isyouke";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isYouke() {
        return sharedPreferences.getBoolean(KEY_IS_Youke, false);
    }
    public void setIsYouke(boolean isyouke) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isyouke);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }
    public void setUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getUserSignature() {
        return sharedPreferences.getString(KEY_UserSignature, "");
    }
    public void setUserSignature(String UserSignature) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_UserSignature, UserSignature);
        editor.apply();
    }

    public String getUserGender() {
        return sharedPreferences.getString(KEY_UserGender, "");
    }
    public void setUserGender(String UserGender) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_UserGender, UserGender);
        editor.apply();
    }

    public String getUserPassword() {
        return sharedPreferences.getString(KEY_USER_Password, "");
    }
    public void setUserPassword(String userPassword) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_Password, userPassword);
        editor.apply();
    }

    public String getUserPhone() {
        return sharedPreferences.getString(KEY_USER_Phone, "");
    }
    public void setUserPhone(String userPhone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_Phone, userPhone);
        editor.apply();
    }
    public String getUserID() {
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public void setUserID(String userID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userID);
        editor.apply();
    }
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_Role, "");
    }
    public void setUserRole(String userRole) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userRole);
        editor.apply();
    }

    public String getFigureShengao() {
        return sharedPreferences.getString(KEY_Figure_Shengao, "");
    }
    public void setFigureShengao(String FigureShengao) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_Figure_Shengao, FigureShengao);
        editor.apply();
    }

    public String getFigureTizhong() {
        return sharedPreferences.getString(KEY_Figure_Tizhong, "");
    }
    public void setFigureTizhong(String FigureTizhong) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_Figure_Tizhong, FigureTizhong);
        editor.apply();
    }

    public String getFigureYaowei() {
        return sharedPreferences.getString(KEY_Figure_Yaowei, "");
    }
    public void setFigureYaowei(String FigureYaowei) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_Figure_Yaowei, FigureYaowei);
        editor.apply();
    }

    public String getFigureXiongwei() {return sharedPreferences.getString(KEY_Figure_Xiongwei, "");}
    public void setFigureXiongwei(String FigureXiongwei) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_Figure_Xiongwei, FigureXiongwei);
        editor.apply();
    }

    public String getFigureTunwei() {return sharedPreferences.getString(KEY_Figure_Tunwei, "");}
    public void setFigureTunwei(String FigureTunwei) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_Figure_Tunwei, FigureTunwei);
        editor.apply();
    }
}
