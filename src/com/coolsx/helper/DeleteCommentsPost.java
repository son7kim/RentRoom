package com.coolsx.helper;

import java.util.List;

import com.coolsx.constants.MConstants;
import com.coolsx.dto.CommentDTO;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class DeleteCommentsPost {
	public void deleteComments(String postId){
		ParseQuery<CommentDTO> query = CommentDTO.getQuery();
		query.whereEqualTo(MConstants.kPostID, postId);
		query.findInBackground(new FindCallback<CommentDTO>() {
			
			@Override
			public void done(List<CommentDTO> comments, ParseException e) {
				if(e == null){
					for(CommentDTO comment : comments){						
						comment.deleteEventually();
					}
				}
			}
		});
	}
}
