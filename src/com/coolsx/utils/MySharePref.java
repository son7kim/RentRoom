package com.coolsx.utils;

import java.lang.reflect.Type;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.coolsx.constants.MConstants;
import com.coolsx.dto.City;
import com.coolsx.dto.District;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@SuppressLint("CommitPrefEdits")
public class MySharePref {
	SharedPreferences mPrefs;
	Editor prefsEditor;
	
	public MySharePref(Activity activity){
		mPrefs = activity.getPreferences(Activity.MODE_PRIVATE);
		prefsEditor=  mPrefs.edit();
	}
	
	public void saveCitys(List<City> citys){		
		Gson gson = new Gson();
		String strJson = gson.toJson(citys);
		prefsEditor.putString(MConstants.pCityPrefs, strJson);
		prefsEditor.commit();
	}
	
	public List<City> getCitys(){
		Gson gson = new Gson();
		String strJson = mPrefs.getString(MConstants.pCityPrefs, "");
		Type type = new TypeToken<List<City>>(){}.getType();
		return gson.fromJson(strJson, type);
	}
	
	public void saveDistricts(List<District> districts){		
		Gson gson = new Gson();
		String strJson = gson.toJson(districts);
		prefsEditor.putString(MConstants.pDistrictPrefs, strJson);
		prefsEditor.commit();
	}
	
	public List<District> getDistricts(){
		Gson gson = new Gson();
		String strJson = mPrefs.getString(MConstants.pDistrictPrefs, "");
		Type type = new TypeToken<List<District>>(){}.getType();
		return gson.fromJson(strJson, type);
	}

	public void setRememberMe(boolean isRemember){
		prefsEditor.putBoolean(MConstants.pRemeberMe, isRemember);
		prefsEditor.commit();
	}
	
	public boolean getRememberMe(){
		return mPrefs.getBoolean(MConstants.pRemeberMe, false);
	}
	
	public void setUserName(String sUser){
		prefsEditor.putString(MConstants.pUser, sUser);
		prefsEditor.commit();
	}
	
	public String getUserName(){
		return mPrefs.getString(MConstants.pUser, null);
	}
	
	public void setPassWord(String sPass){
		prefsEditor.putString(MConstants.pPass, sPass);
		prefsEditor.commit();
	}
	
	public String getPassWord(){
		return mPrefs.getString(MConstants.pPass, null);
	}
}
