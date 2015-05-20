package com.coolsx.dto;

import com.coolsx.constants.MConstants;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName(MConstants.kTableDistrict)
public class DistrictDTO extends ParseObject{

	public static ParseQuery<DistrictDTO> getQuery() {
		ParseQuery<DistrictDTO> query;
		query = ParseQuery.getQuery(DistrictDTO.class);
		query.setLimit(MConstants.iLimitSize);
		
		return query;
		//return ParseQuery.getQuery(DistrictDTO.class);
	}
	
	public String getDistrictName(){
		return getString(MConstants.kDistricName);
	}
	
	public String getDistrictID(){
		return getString(MConstants.kDistrictID);
	}
	
	public String getCityID(){
		return getString(MConstants.kCityID);
	}
}
