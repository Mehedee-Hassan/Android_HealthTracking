package com.example.androidbtcontrol.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {
	
	Context mContext;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor spEditor;
	
	
	private final String IS_BT_ACTIVE = "is_bluetooth_active";
    private final String LOGIN_STATUS = "login_status";
    private final String USER_ID = "user_id";
    private final String USER_NAME = "user_name";

	public PreferenceUtil(Context mContext) 
	{
		super();
		this.mContext = mContext;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
	}


    public void setUserID (String user_id) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_ID, user_id);
        spEditor.commit();

    }

    public String getUserID () {
        return sharedPreferences.getString(USER_ID, "");

    }


    public void setLogInStatus (int status) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(LOGIN_STATUS, status);
        spEditor.commit();

    }

    public int getLogInStatus () {
        return sharedPreferences.getInt(LOGIN_STATUS, 0);
    }

    public void setBTStatus(int status) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(IS_BT_ACTIVE, status);
        spEditor.commit();
    }

    public int getBTStatus() {
        return sharedPreferences.getInt(IS_BT_ACTIVE, 0);
    }


}