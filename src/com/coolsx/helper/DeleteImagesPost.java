package com.coolsx.helper;

import java.util.List;

import com.coolsx.constants.MConstants;
import com.coolsx.dto.ImageDTO;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class DeleteImagesPost {
	public void deleteImages(String postId){
		ParseQuery<ImageDTO> query = ImageDTO.getQuery();
		query.whereEqualTo(MConstants.kPostID, postId);
		query.findInBackground(new FindCallback<ImageDTO>() {
			
			@Override
			public void done(List<ImageDTO> imgs, ParseException e) {
				if(e == null){
					for(ImageDTO img : imgs){
						img.deleteEventually();
					}
				}
			}
		});
	}

}
