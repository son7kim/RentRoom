package com.coolsx.rentroom;

import com.coolsx.constants.MData;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
		
		tvDescription = (TextView)findViewById(R.id.tvDescriptionDetail);
		tvCost = (TextView)findViewById(R.id.tvCostDetail);
		tvArea = (TextView)findViewById(R.id.tvAreaDetail);
		tvNumRoom = (TextView)findViewById(R.id.tvNumRoomDetail);
		tvAddress = (TextView)findViewById(R.id.tvAddressDetail);
		tvPhone = (TextView)findViewById(R.id.tvPhoneDetail);
		tvName = (TextView)findViewById(R.id.tvNameDetail);
		
		setDataToView();
	}
	
	private void setDataToView(){
		tvDescription.setText(MData.postInfo.getDescription());
		String sCost = MData.postInfo.getCostMin() + " VND";
		if(MData.postInfo.getCostMax() > 0){
			sCost = sCost + " - " + MData.postInfo.getCostMax() + " VND";
		}
		tvCost.setText(sCost);
		String sArea = "";
		if(MData.postInfo.getAreaMin() > 0){
			sArea = sArea + MData.postInfo.getAreaMin() + " m2";
		}
		if(MData.postInfo.getAreaMax() > 0){
			sArea = sArea + " - " + MData.postInfo.getAreaMax() + " m2";
		}
		tvArea.setText(sArea);
		tvNumRoom.setText(""+MData.postInfo.getNumRoom());
		tvAddress.setText("Dia chi: " + MData.postInfo.getAddress());
		tvPhone.setText("So DT: " + MData.postInfo.getPhoneNumber());
		tvName.setText("Ten lien he: " + MData.postInfo.getFullName());
	}
}
