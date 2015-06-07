package com.coolsx.rentroom;

import android.app.Application;

import com.coolsx.constants.MConstants;
import com.coolsx.dto.CityDTO;
import com.coolsx.dto.CommentDTO;
import com.coolsx.dto.DistrictDTO;
import com.coolsx.dto.ImageDTO;
import com.coolsx.dto.PostArticleDTO;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {		
		super.onCreate();
		
		ParseObject.registerSubclass(CommentDTO.class);
		ParseObject.registerSubclass(ImageDTO.class);
		ParseObject.registerSubclass(PostArticleDTO.class);
		ParseObject.registerSubclass(CityDTO.class);
		ParseObject.registerSubclass(DistrictDTO.class);
		
		// Initialize Crash Reporting.
	    ParseCrashReporting.enable(this);

	    // Enable Local Datastore.
	    //Parse.enableLocalDatastore(this);

	    // Add your initialization code here
	    Parse.initialize(this, MConstants.kApplicationKey, MConstants.kClientKey);


	    ParseUser.enableAutomaticUser();
	    ParseACL defaultACL = new ParseACL();
	    // Optionally enable public read access.
	     defaultACL.setPublicReadAccess(true);
	    ParseACL.setDefaultACL(defaultACL, true);
	}

}
