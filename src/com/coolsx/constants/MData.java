package com.coolsx.constants;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

import com.coolsx.dto.CityDTO;
import com.coolsx.dto.DistrictDTO;
import com.coolsx.dto.UserDTO;

public class MData {
	public static List<CityDTO> cityDTOs = new ArrayList<CityDTO>();
	public static List<DistrictDTO> districtDTOs = new ArrayList<DistrictDTO>();
	public static List<String> sAdapterCity = new ArrayList<String>();
	public static ArrayAdapter<String> adapterCity = null;
	public static UserDTO userInfo = null;
}
