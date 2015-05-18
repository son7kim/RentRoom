package com.coolsx.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.coolsx.rentroom.R;

public class UtilDroid {

	public static ArrayAdapter<String> getAdapterDistrict(Context context, int pos){
		ArrayAdapter<String> adapter = null;
		String [] arrSpContent;
		
		switch(pos){
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
