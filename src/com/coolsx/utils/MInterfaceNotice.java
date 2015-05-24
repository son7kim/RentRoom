package com.coolsx.utils;

import java.util.List;

import com.coolsx.dto.ImageDTO;
import com.coolsx.dto.PostArticleDTO;

public class MInterfaceNotice {
	public interface onGetDistrictNotify {
		void onFinish();
	}

	public interface onDeleteFileNotify {
		void onDeleteNotify(List<ImageDTO> listImgAfterDelete);
	}
	
	public interface onPostActicle{
		void onSuccess(PostArticleDTO postActicle);
	}
	
}
