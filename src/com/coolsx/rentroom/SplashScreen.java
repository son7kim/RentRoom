package com.coolsx.rentroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.coolsx.helper.GetCityAndDistrict;
import com.coolsx.utils.MInterfaceNotice.onGetDistrictNotify;

public class SplashScreen extends Activity implements onGetDistrictNotify {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GetCityAndDistrict getCityDistrict = new GetCityAndDistrict(this, this);
		getCityDistrict.checkUpdateCity_District();
	}

	@Override
	public void onFinish() {
		Intent i = new Intent(SplashScreen.this, MainRentRoom.class);
		startActivity(i);
		finish();
	}
}
