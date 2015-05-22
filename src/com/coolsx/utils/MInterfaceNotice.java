package com.coolsx.utils;

import java.util.List;

import com.coolsx.dto.ImageDTO;

public class MInterfaceNotice {
	public interface onGetDistrictNotify {
		void onFinish();
	}

	public interface onDeleteFileNotify {
		void onDeleteNotify(List<ImageDTO> listImgAfterDelete);
	}
}
