package com.coolsx.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.CityDTO;
import com.coolsx.dto.DistrictDTO;
import java.util.UUID;

public class UtilDroid {

	public static ArrayAdapter<String> getAdapterCity(Context context) {
		MData.sAdapterCity.clear();
		for (CityDTO city : MData.cityDTOs) {
			MData.sAdapterCity.add(city.getCityName());
		}

		MData.adapterCity = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, MData.sAdapterCity);
		MData.adapterCity
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return MData.adapterCity;
	}

	public static ArrayAdapter<String> getAdapterDistrictFromKey(
			final Context context, CityDTO city, List<DistrictDTO> districtDTOsTemp) {
		List<String> sAdapterDistrict = new ArrayList<String>();
		ArrayAdapter<String> adapterDistrict = null;
		districtDTOsTemp.clear();
		sAdapterDistrict.clear();
		for (DistrictDTO districtObj : MData.districtDTOs) {
			if (districtObj.getCityID().equals(city.getCityID())) {
				sAdapterDistrict.add(districtObj.getDistrictName());
				districtDTOsTemp.add(districtObj);
			}
		}

		adapterDistrict = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, sAdapterDistrict);
		adapterDistrict
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return adapterDistrict;
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
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

		final CharSequence[] options = { "Take Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setTitle("Add Photo");

		builder.setItems(options, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (options[item].equals("Take Photo"))
				{
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//					File f = new File(android.os.Environment
//							.getExternalStorageDirectory(), "temp.jpg");
//
//					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//
//					activity.startActivityForResult(intent, MConstants.CHOOSE_IMG);
					
					Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					activity.startActivityForResult(intent, MConstants.TAKE_PHOTO);
				}

				else if (options[item].equals("Choose from Gallery"))
				{

					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

					activity.startActivityForResult(intent, MConstants.CHOOSE_IMG);

				}
				else if (options[item].equals("Cancel")) {

					dialog.dismiss();
				}
			}
		});

		builder.show();

	}

	public static byte[] getByteArrFromDataIntent(Context context, Intent data, int requestCode){	
		Bitmap bitmap = null;
		if(requestCode == MConstants.TAKE_PHOTO){
			try{		        
				bitmap = (Bitmap) data.getExtras().get("data");    
			} catch(Exception e){				
			}
		} else {
			Uri uri = Uri.parse(data.getDataString());
			try {
				bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
			} catch (FileNotFoundException e) {
				e.printStackTrace ();
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}		
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// Compress image to lower quality scale 1 - 100
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] bIimages = stream.toByteArray();	
		
		return bIimages;
	}
	
	public static String getRandomStringNumber() {
	    Random generator = new Random();
	    String x = "phong_"+(generator.nextInt(96) + 32);
	    return x;
	}
	
	public static String getRandomStringUUID(){		
		return UUID.randomUUID().toString();
	}
}
