package com.coolsx.helper;

import java.util.List;

import android.content.Context;

import com.coolsx.constants.MData;
import com.coolsx.dto.CityDTO;
import com.coolsx.dto.DistrictDTO;
import com.coolsx.utils.MInterfaceNotice.onGetDistrictNotify;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class GetCityAndDistrict {

	public onGetDistrictNotify actionGetDistrict;
	Context _context;
	boolean _isSplashScreen;

	public GetCityAndDistrict(Context context, onGetDistrictNotify event, boolean isSplashScreen) {
		this._context = context;
		actionGetDistrict = event;
		this._isSplashScreen = isSplashScreen;
	}

	public void getListCity() {
		ParseQuery<CityDTO> query = CityDTO.getQuery();
		query.findInBackground(new FindCallback<CityDTO>() {
			@Override
			public void done(List<CityDTO> citys, ParseException e) {
				if (e == null) {
					getListDistrict();
					MData.cityDTOs.clear();
					MData.cityDTOs.addAll(citys);
				} else {
					if(_isSplashScreen){
						actionGetDistrict.onFinishLoadDataFromSplash();
					} else {
						actionGetDistrict.onFinishLoadDataFromMain();
					}
				}
			}
		});
	}

	public void getListDistrict() {

		ParseQuery<DistrictDTO> query = DistrictDTO.getQuery();
		query.findInBackground(new FindCallback<DistrictDTO>() {
			@Override
			public void done(List<DistrictDTO> districts, ParseException e) {
				if (e == null) {
					MData.districtDTOs.clear();
					MData.districtDTOs.addAll(districts);
				}
				if(_isSplashScreen){
					actionGetDistrict.onFinishLoadDataFromSplash();
				} else {
					actionGetDistrict.onFinishLoadDataFromMain();
				}
			}
		});
	}
}
