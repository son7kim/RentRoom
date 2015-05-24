package com.coolsx.helper;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.CityDTO;
import com.coolsx.dto.DistrictDTO;
import com.coolsx.utils.MInterfaceNotice.onGetDistrictNotify;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GetCityAndDistrict {

	public onGetDistrictNotify actionGetDistrict;
	Context _context;

	public GetCityAndDistrict(Context context, onGetDistrictNotify event) {
		this._context = context;
		actionGetDistrict = event;
	}

	public void checkUpdateCity_District() {
		final ParseQuery<CityDTO> queryCityLocal = ParseQuery.getQuery(MConstants.kTableCity);
		queryCityLocal.fromLocalDatastore();
		queryCityLocal.orderByDescending(MConstants.kUpdatedAt);
		queryCityLocal.getFirstInBackground(new GetCallback<CityDTO>() {

			@Override
			public void done(CityDTO cityOld, ParseException e) {

				if (e == null) {
					final java.util.Date dOld = cityOld.getUpdatedAt();

					ParseQuery<ParseObject> queryCityNew = ParseQuery.getQuery(MConstants.kTableCity);
					queryCityNew.orderByDescending(MConstants.kUpdatedAt);

					queryCityNew.getFirstInBackground(new GetCallback<ParseObject>() {

						@Override
						public void done(ParseObject object, ParseException e) {
							if (e == null) {
								final java.util.Date dNew = object.getUpdatedAt();
								long lMiliSecondOld = dOld.getTime();
								long lMiliSecondNew = dNew.getTime();
								if (lMiliSecondNew > lMiliSecondOld) {
									// Update
									getListCity(false);
								} else {
									getListCity(true);
								}
							} else {
								getListCity(true);
							}
						}
					});
				} else {
					getListCity(false);
				}
			}
		});
	}

	private void getListCity(final boolean isLocal) {
		ParseQuery<CityDTO> query = CityDTO.getQuery();
		if(isLocal){
			query.fromLocalDatastore();
		}	

		query.findInBackground(new FindCallback<CityDTO>() {
			@Override
			public void done(List<CityDTO> citys, ParseException e) {
				if (e == null) {
					getListDistrict(isLocal);
					// Save to Local
					if(!isLocal)
						ParseObject.pinAllInBackground(citys);
					MData.cityDTOs.clear();
					MData.cityDTOs.addAll(citys);
				} else {
					Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
				}
			}
		});
	}

	public void getListDistrict(final boolean isLocal) {

		ParseQuery<DistrictDTO> query = DistrictDTO.getQuery();
		if (isLocal) {
			query.fromLocalDatastore();
		}

		query.findInBackground(new FindCallback<DistrictDTO>() {
			@Override
			public void done(List<DistrictDTO> districts, ParseException e) {
				if (e == null) {
					if (!isLocal)
						ParseObject.pinAllInBackground(districts);
					MData.districtDTOs.clear();
					MData.districtDTOs.addAll(districts);					
					
					actionGetDistrict.onFinish();

				} else {
					Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
				}
			}
		});
	}
}
