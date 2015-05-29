package com.coolsx.rentroom;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.ImageDTO;
import com.coolsx.utils.MInterfaceNotice.onDeleteFileNotify;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class PostDetail extends Activity implements onDeleteFileNotify {

	TextView tvDescription;
	TextView tvCost;
	TextView tvArea;
	TextView tvNumRoom;
	TextView tvAddress;
	TextView tvPhone;
	TextView tvName;
	LinearLayout llFileAttach;
	OnAddFileImage onAddFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_post);

		tvDescription = (TextView) findViewById(R.id.tvDescriptionDetail);
		tvCost = (TextView) findViewById(R.id.tvCostDetail);
		tvArea = (TextView) findViewById(R.id.tvAreaDetail);
		tvNumRoom = (TextView) findViewById(R.id.tvNumRoomDetail);
		tvAddress = (TextView) findViewById(R.id.tvAddressDetail);
		tvPhone = (TextView) findViewById(R.id.tvPhoneDetail);
		tvName = (TextView) findViewById(R.id.tvNameDetail);
		llFileAttach = (LinearLayout) findViewById(R.id.llFileAttachDetail);

		onAddFile = new OnAddFileImage(this, this);

		setDataToView();
		setActionToView();
		if (MData.postInfo.getIsFileLoaded()) {
			if (MData.postInfo.getListImageDTO() != null) {
				onAddFile.InitView(MData.postInfo.getListImageDTO(), false);
				llFileAttach.addView(onAddFile);
			}
		} else {
			getFileAttach();
		}
	}

	private void setDataToView() {
		DecimalFormat dFormat = new DecimalFormat();
		tvDescription.setText(MData.postInfo.getDescription());
		String sCost = dFormat.format((long) MData.postInfo.getCostMin())
				+ " VND";
		if (MData.postInfo.getCostMax() > 0) {
			sCost = sCost + " - "
					+ dFormat.format((long) MData.postInfo.getCostMax())
					+ " VND";
		}
		tvCost.setText(sCost);
		String sArea = "";
		if (MData.postInfo.getAreaMin() > 0) {
			sArea = sArea + MData.postInfo.getAreaMin() + " m2";
		}
		if (MData.postInfo.getAreaMax() > 0) {
			sArea = sArea + " - " + MData.postInfo.getAreaMax() + " m2";
		}
		tvArea.setText(sArea);
		tvNumRoom.setText("" + MData.postInfo.getNumRoom());
		tvAddress.setText("Dia chi: " + MData.postInfo.getAddress());
		tvPhone.setText("" + MData.postInfo.getPhoneNumber());
		tvName.setText("Ten lien he: " + MData.postInfo.getFullName());
	}

	private void setActionToView() {
		tvPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:+"
							+ tvPhone.getText().toString().trim()));
					startActivity(callIntent);
				} catch (Exception e) {
				}
			}
		});
	}

	private void getFileAttach() {
		ParseQuery<ImageDTO> query = ImageDTO.getQuery();
		query.whereEqualTo(MConstants.kPostID, MData.postInfo.getPostID());
		query.findInBackground(new FindCallback<ImageDTO>() {

			@Override
			public void done(List<ImageDTO> imgDTOs, ParseException e) {
				if (e == null) {
					onAddFile.InitView(imgDTOs, false);
					llFileAttach.addView(onAddFile);
					MData.postInfo.setIsFileLoaded(true);
					MData.postInfo.setListImageDTO(imgDTOs);
				}
			}
		});
	}

	@Override
	public void onDeleteNotify(List<ImageDTO> listImgAfterDelete) {
		MData.postInfo.setListImageDTO(listImgAfterDelete);
		onAddFile.InitView(MData.postInfo.getListImageDTO(), false);
		llFileAttach.removeAllViews();
		llFileAttach.addView(onAddFile);
	}
}
