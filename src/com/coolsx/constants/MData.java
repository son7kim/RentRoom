package com.coolsx.constants;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

import com.coolsx.dto.City;
import com.coolsx.dto.District;
import com.coolsx.dto.PostArticleDTO;
import com.coolsx.dto.UserDTO;
import com.coolsx.utils.MySharePref;

public class MData {
	public static List<City> cityDTOs = new ArrayList<City>();
	public static List<District> districtDTOs = new ArrayList<District>();
	public static List<String> sAdapterCity = new ArrayList<String>();
	public static ArrayAdapter<String> adapterCity = null;
	public static UserDTO userInfo = null;
	public static PostArticleDTO postInfo = null;
	public static String strPass;
	public static MySharePref mySharePrefs;
}
