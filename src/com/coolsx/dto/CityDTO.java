package com.coolsx.dto;

import com.coolsx.constants.MConstants;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName(MConstants.kTableCity)
public class CityDTO extends ParseObject {

	public CityDTO() {

	}

	public static ParseQuery<CityDTO> getQuery() {
		return ParseQuery.getQuery(CityDTO.class);
	}

	public String getCityName() {
		return getString(MConstants.kCityName);
	}
	
	public String getCityID() {
		return getString(MConstants.kCityID);
	}
}
