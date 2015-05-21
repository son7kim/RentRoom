package com.coolsx.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.CityDTO;
import com.coolsx.dto.DistrictDTO;

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
					activity.startActivityForResult(intent, MConstants.CHOOSE_IMG);
				      

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

	public static void onSelectImageOk(Context context, Intent data){
		Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
		onResultChoosed (bitmap1);
		
//		if (data == null) {
//			try{		        
//				Bitmap bitmap = (Bitmap) data.getExtras().get("data");     
//						
//				onResultChoosed (bitmap);
//				
//			} catch(Exception e){				
//			}
//		} else {
//			Uri uri = Uri.parse(data.getDataString());
//			Bitmap bitmap = null;
//			try {
//				bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace ();
//			} catch (IOException e) {
//				e.printStackTrace ();
//			}
//			
//			onResultChoosed (bitmap);
//		}
	}
	
	public static Uri getPhotoFileUri(String fileName) {
	    // Get safe storage directory for photos
	    File mediaStorageDir = new File(
	        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");

	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
	        Log.d("", "failed to create directory");
	    }

	    // Return the file target for the photo based on filename
	    return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
	}
	
	public static byte[] GetByteArrayFromFile (String filename)
	{
			File fInfo = new File(filename);			

			 try {
				InputStream is = new FileInputStream(fInfo);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        long length = fInfo.length();
			return null;
//			int size = (int) fInfo.length();
//			byte[] bytes = new byte[size];
//			try {				
//			    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(fInfo));
//			    buf.read(bytes, 0, bytes.length);
//			    buf.close();
//			} catch (FileNotFoundException e) {
//			    e.printStackTrace();
//			} catch (IOException e) {
//			    e.printStackTrace();
//			}
//			
//		return bytes;
	}
	
	static void onResultChoosed(Bitmap bitmap){
		// Convert it to byte
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// Compress image to lower quality scale 1 - 100
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] image = stream.toByteArray();	

	}
}
