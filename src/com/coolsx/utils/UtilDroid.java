package com.coolsx.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class UtilDroid {

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

}
