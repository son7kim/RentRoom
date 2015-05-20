package com.coolsx.constants;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

import com.coolsx.dto.CityDTO;
import com.coolsx.dto.DistrictDTO;

public class MData {
	public static List<CityDTO> cityDTOs = new ArrayList<CityDTO>();
	public static List<DistrictDTO> districtDTOs = new ArrayList<DistrictDTO>();
	public static List<String> sAdapterCity = new ArrayList<String>();
	public static List<String> sAdapterDistrict = new ArrayList<String>();
	public static ArrayAdapter<String> adapterCity = null;
	public static ArrayAdapter<String> adapterDistrict = null;
}
