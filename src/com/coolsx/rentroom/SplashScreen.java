package com.coolsx.rentroom;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.coolsx.constants.MData;
import com.coolsx.dto.City;
import com.coolsx.dto.District;
import com.coolsx.dto.UserDTO;
import com.coolsx.helper.GetCityAndDistrict;
import com.coolsx.utils.MInterfaceNotice.onGetDistrictNotify;
import com.coolsx.utils.MySharePref;
import com.coolsx.utils.UtilDroid;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SplashScreen extends Activity implements onGetDistrictNotify {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Get Citys and Districts from SharedPreferences */
		MData.mySharePrefs = new MySharePref(this);
		MData.cityDTOs = MData.mySharePrefs.getCitys();
		MData.districtDTOs = MData.mySharePrefs.getDistricts();

		/* Check and set data for list City and District */
		if (MData.cityDTOs != null && MData.cityDTOs.size() > 0 && MData.districtDTOs != null && MData.districtDTOs.size() > 0) {
			if (MData.mySharePrefs.getRememberMe() && UtilDroid.checkInternet()) {
				String userName = MData.mySharePrefs.getUserName();
				String passWord = MData.mySharePrefs.getPassWord();
				if (userName != null && passWord != null) {
					onAutoSignIn(userName, passWord);
				} else {
					onStartMainActivity();
				}
			} else {
				onStartMainActivity();
			}
			return;
		} else {
			MData.cityDTOs = new ArrayList<City>();
			MData.districtDTOs = new ArrayList<District>();
		}

		/* Check internet and get City and District from Parse Server */
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

	private void onAutoSignIn(final String userName, final String password) {
		ParseUser.logInInBackground(userName, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (e == null) {
					MData.strPass = password;
					MData.userInfo = new UserDTO(user.getObjectId(), user.getUsername(), user.getEmail());
					onStartMainActivity();
				} else {
					// Fail
					onStartMainActivity();
				}
			}
		});
	}
}
