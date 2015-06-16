package com.coolsx.rentroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.coolsx.helper.GetCityAndDistrict;
import com.coolsx.utils.MInterfaceNotice.onGetDistrictNotify;
import com.coolsx.utils.UtilDroid;

public class SplashScreen extends Activity implements onGetDistrictNotify {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
