package com.coolsx.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.City;
import com.coolsx.dto.District;

public class UtilDroid {

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean checkInternet() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
			int mExitValue = mIpAddrProcess.waitFor();
			if (mExitValue == 0) {
				return true;
			} else {
				return false;
			}
		} catch (InterruptedException ignore) {
			ignore.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayAdapter<String> getAdapterCity(Context context) {
		MData.sAdapterCity.clear();
		for (City city : MData.cityDTOs) {
			MData.sAdapterCity.add(city.getCityName());
		}

		MData.adapterCity = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, MData.sAdapterCity);
		MData.adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return MData.adapterCity;
	}

	public static ArrayAdapter<String> getAdapterDistrictFromKey(final Context context, City city, List<District> districtDTOsTemp) {
		List<String> sAdapterDistrict = new ArrayList<String>();
		ArrayAdapter<String> adapterDistrict = null;
		districtDTOsTemp.clear();
		sAdapterDistrict.clear();
		for (District districtObj : MData.districtDTOs) {
			if (districtObj.getCityID().equals(city.getCityID())) {
				sAdapterDistrict.add(districtObj.getDistrictName());
				districtDTOsTemp.add(districtObj);
			}
		}

		adapterDistrict = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sAdapterDistrict);
		adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return adapterDistrict;
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

		final CharSequence[] options = { "Dùng Camera", "Thư viện ảnh", "Hủy" };

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setTitle("Đăng hình");

		builder.setItems(options, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (options[item].equals("Dùng Camera")) {
					Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					activity.startActivityForResult(intent, MConstants.TAKE_PHOTO);
				}

				else if (options[item].equals("Thư viện ảnh")) {

					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

					activity.startActivityForResult(intent, MConstants.CHOOSE_IMG);

				} else if (options[item].equals("Hủy")) {

					dialog.dismiss();
				}
			}
		});

		builder.show();

	}

	public static byte[] getByteArrFromDataIntent(Context context, Intent data, int requestCode) {
		Bitmap bitmap = null;
		byte[] bIimages = null;
		if (requestCode == MConstants.TAKE_PHOTO) {
			try {
				bitmap = (Bitmap) data.getExtras().get("data");
			} catch (Exception e) {
			}
		} else {
			Uri uri = Uri.parse(data.getDataString());
			try {
				bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// Compress image to lower quality scale 1 - 100
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			bIimages = stream.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
			return bIimages;
		}
		return bIimages;
	}

	public static String getRandomStringNumber() {
		Random generator = new Random();
		String x = "phong_" + (generator.nextInt(96) + 32);
		return x;
	}

	public static String getRandomStringUUID() {
		return UUID.randomUUID().toString();
	}
}
