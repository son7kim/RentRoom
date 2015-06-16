package com.coolsx.utils;

import java.util.List;

import com.coolsx.dto.ImageDTO;
import com.coolsx.dto.PostArticleDTO;

public class MInterfaceNotice {
	public interface onGetDistrictNotify {
		void onFinishLoadDataFromSplash();

		void onFinishLoadDataFromMain();
	}

	public interface onDeleteFileNotify {
		void onSendingDeleteFile();

		void onDeleteFileSuccess(List<ImageDTO> listImgAfterDelete);
	}

	public interface onPostActicle {
		void onSuccess(PostArticleDTO postActicle);
	}

	public interface onGetAttachFile {
		void onSendingGetFile();

		void onSuccessGetFile(List<ImageDTO> imgDTOs);
	}

	public interface onOkLoadData {
		void onOkClick();
	}
}
