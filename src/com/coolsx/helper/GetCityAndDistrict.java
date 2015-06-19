package com.coolsx.helper;

import java.util.List;

import android.content.Context;

import com.coolsx.constants.MData;
import com.coolsx.dto.City;
import com.coolsx.dto.CityDTO;
import com.coolsx.dto.District;
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
					if (citys != null) {
						for (CityDTO cityDto : citys) {
							City cityTemp = new City(cityDto.getCityID(), cityDto.getCityName());
							MData.cityDTOs.add(cityTemp);
						}
						MData.mySharePrefs.saveCitys(MData.cityDTOs);
					}
				} else {
					if (_isSplashScreen) {
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
					if (districts != null) {
						for (DistrictDTO districtDto : districts) {
							District districtTemp = new District(districtDto.getDistrictID(), districtDto.getDistrictName(), districtDto.getCityID());
							MData.districtDTOs.add(districtTemp);
						}
						MData.mySharePrefs.saveDistricts(MData.districtDTOs);
					}
				}
				if (_isSplashScreen) {
					actionGetDistrict.onFinishLoadDataFromSplash();
				} else {
					actionGetDistrict.onFinishLoadDataFromMain();
				}
			}
		});
	}
}
