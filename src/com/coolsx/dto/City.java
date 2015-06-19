package com.coolsx.dto;


public class City {
	String cityId;
	String cityName;

	public City(String cityId, String cityName) {
		this.cityId = cityId;
		this.cityName = cityName;
	}

	public String getCityID() {
		return this.cityId;
	}

	public String getCityName() {
		return this.cityName;
	}

}
