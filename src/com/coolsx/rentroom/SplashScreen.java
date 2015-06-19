package com.coolsx.rentroom;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.coolsx.constants.MData;
import com.coolsx.dto.City;
import com.coolsx.dto.District;
import com.coolsx.helper.GetCityAndDistrict;
import com.coolsx.utils.MInterfaceNotice.onGetDistrictNotify;
import com.coolsx.utils.MySharePref;
import com.coolsx.utils.UtilDroid;

public class SplashScreen extends Activity implements onGetDistrictNotify {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*Get Citys and Districts from SharedPreferences*/
		MData.mySharePrefs = new MySharePref(this);
		MData.cityDTOs = MData.mySharePrefs.getCitys();
		MData.districtDTOs = MData.mySharePrefs.getDistricts();
		
		/*Check and set data for list City and District*/
		if(MData.cityDTOs != null && MData.cityDTOs.size() > 0 && MData.districtDTOs != null && MData.districtDTOs.size() > 0){
			onStartMainActivity();
			return;
		}else{
			MData.cityDTOs = new ArrayList<City>();
			MData.districtDTOs = new ArrayList<District>();
		}
		
		/*Check internet and get City and District from Parse Server*/
		if (UtilDroid.checkInternet()) {
			GetCityAndDistrict getCityDistrict = new GetCityAndDistrict(this, this, true);
			getCityDistrict.getListCity();
		} else {
			onStartMainActivity();
		}
	}

	@Override
	public void onFinishLoadDataFromSplash() {
		onStartMainActivity();
	}

	@Override
	public void onFinishLoadDataFromMain() {
	}

	private void onStartMainActivity() {
		Intent i = new Intent(SplashScreen.this, MainRentRoom.class);
		startActivity(i);
		finish();
	}
}
