package com.coolsx.utils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentQueryMap;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.CityDTO;
import com.coolsx.dto.DistrictDTO;
import com.coolsx.rentroom.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class UtilDroid {
	
	public static ArrayAdapter<String> getAdapterCity(Context context){		
		MData.sAdapterCity.clear();
		for(CityDTO city : MData.cityDTOs){			
			MData.sAdapterCity.add(city.getCityName());
		}
		
		MData.adapterCity = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, MData.sAdapterCity);
		MData.adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		return MData.adapterCity;
	}
	
	public static ArrayAdapter<String> getAdapterDistrictFromKey(final Context context, CityDTO city){
		ParseQuery<DistrictDTO> query = ParseQuery.getQuery(MConstants.kTableDistrict);
		query.fromLocalDatastore();
		
		query.whereEqualTo(MConstants.kCityID, city.getCityID());
		
		
		query.findInBackground(new FindCallback<DistrictDTO>() {

			@Override
			public void done(List<DistrictDTO> objects, ParseException e) {
				if(e == null){
					MData.sAdapterDistrict.clear();
					for(DistrictDTO object:objects){
						MData.sAdapterDistrict.add(object.getDistrictName());
					}
					
					MData.adapterDistrict = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, MData.sAdapterDistrict);
					MData.adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				}
			}
		});
		return MData.adapterDistrict;
	}
	
	public static ArrayAdapter<String> getAdapterDistrict(Context context, int pos) {
		ArrayAdapter<String> adapter = null;
		String[] arrSpContent;

		switch (pos) {
		case 0:
			arrSpContent = context.getResources().getStringArray(R.array.hcm_district_arrays);
			adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrSpContent);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			break;
		case 1:
			arrSpContent = context.getResources().getStringArray(R.array.hn_district_arrays);
			adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrSpContent);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			break;
		case 2:
			break;
		case 3:
			break;
		}

		return adapter;
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public final static boolean isValidUsername_Pass(String target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else { // Contains whiteSpace
			Pattern pattern = Pattern.compile("\\s");
			Matcher matcher = pattern.matcher(target);
			boolean found = matcher.find();
			return !found;
		}
	}

	public static void selectImage(final Activity activity) {

		final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setTitle("Add Photo");

		builder.setItems(options, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (options[item].equals("Take Photo"))

				{

					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

					activity.startActivityForResult(intent, 1);

				}

				else if (options[item].equals("Choose from Gallery"))

				{

					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

					activity.startActivityForResult(intent, 2);

				}

				else if (options[item].equals("Cancel")) {

					dialog.dismiss();

				}

			}

		});

		builder.show();

	}

}
