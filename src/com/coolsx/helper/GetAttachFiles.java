package com.coolsx.helper;

import java.util.List;

import com.coolsx.constants.MConstants;
import com.coolsx.dto.ImageDTO;
import com.coolsx.utils.MInterfaceNotice.onGetAttachFile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class GetAttachFiles {
	public onGetAttachFile getAttachFileDelegate;
	public GetAttachFiles(onGetAttachFile event){
		this.getAttachFileDelegate = event;
	}
	public void getFileAttach(String postId) {
		getAttachFileDelegate.onSendingGetFile();
		ParseQuery<ImageDTO> query = ImageDTO.getQuery();
		query.whereEqualTo(MConstants.kPostID, postId);
		query.findInBackground(new FindCallback<ImageDTO>() {

			@Override
			public void done(List<ImageDTO> imgDTOs, ParseException e) {
				if (e == null) {
					getAttachFileDelegate.onSuccessGetFile(imgDTOs);
				} else {
					getAttachFileDelegate.onSuccessGetFile(null);
				}
			}
		});
	}
}
