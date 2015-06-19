package com.coolsx.dto;


public class District {
	String districtId;
	String districName;
	String cityId;

	public District(String districtId, String districtName, String cityId) {
		this.districtId = districtId;
		this.districName = districtName;
		this.cityId = cityId;
	}

	public String getDistrictName() {
		return this.districName;
	}

	public String getDistrictID() {
		return this.districtId;
	}

	public String getCityID() {
		return this.cityId;
	}
}
