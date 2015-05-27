package com.coolsx.rentroom;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.coolsx.constants.MData;

public class PostDetail extends Activity {

	TextView tvDescription;
	TextView tvCost;
	TextView tvArea;
	TextView tvNumRoom;
	TextView tvAddress;
	TextView tvPhone;
	TextView tvName;

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

		setDataToView();
		setActionToView();
	}

	private void setDataToView() {
		DecimalFormat dFormat = new DecimalFormat();
		tvDescription.setText(MData.postInfo.getDescription());
		String sCost = dFormat.format(MData.postInfo.getCostMin()) + " VND";
		if (MData.postInfo.getCostMax() > 0) {
			sCost = sCost + " - " + dFormat.format(MData.postInfo.getCostMax()) + " VND";
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
}
