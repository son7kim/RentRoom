package com.coolsx.rentroom;

import com.coolsx.constants.MConstants;
import com.parse.ParseObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class AddNewPostActivity extends Activity {

	private EditText edFullName;
	private EditText edPhone;
	private Spinner spCityAddNew;
	private Spinner spDistrictAddNew;
	private EditText edAddress;
	private EditText edNumRoom;
	private EditText edAreaMin;
	private EditText edAreaMax;
	private EditText edCostMin;
	private EditText edCostMax;
	private EditText edDescription;
	private TextView tvChooseImg;
	private LinearLayout llFileAttach;
	private TextView tvError;
	private Button btnPost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_new_post_page);
		
		edFullName = (EditText)findViewById(R.id.edit_fullname);
		edPhone = (EditText)findViewById(R.id.edit_phone_number);
		spCityAddNew = (Spinner)findViewById(R.id.spinCityAddNew);
		spDistrictAddNew = (Spinner)findViewById(R.id.spinDistrictAddNew);
		edAddress = (EditText)findViewById(R.id.edit_address);
		edNumRoom = (EditText)findViewById(R.id.edit_num_room);
		edAreaMin = (EditText)findViewById(R.id.edit_area_min);	
		edAreaMax = (EditText)findViewById(R.id.edit_area_max);
		edCostMin = (EditText)findViewById(R.id.edit_cost_min);
		edCostMax = (EditText)findViewById(R.id.edit_cost_max);
		edDescription = (EditText)findViewById(R.id.edit_discription);
		tvChooseImg = (TextView)findViewById(R.id.tv_upload_img);
		llFileAttach = (LinearLayout)findViewById(R.id.llFileAttach);
		tvError = (TextView) findViewById(R.id.tv_error_add_new_post);
		btnPost = (Button)findViewById(R.id.btn_post);
		
		btnPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tvError.setVisibility(View.GONE);
				if(!edFullName.getText().toString().trim().isEmpty()){
					if(!edPhone.getText().toString().trim().isEmpty()){
						if(!edAddress.getText().toString().trim().isEmpty()){
							if(!edCostMin.getText().toString().trim().isEmpty()){
								if(!edDescription.getText().toString().trim().isEmpty()){
									if(!edCostMax.getText().toString().trim().isEmpty()){
										if(Integer.parseInt(edCostMax.getText().toString()) >= Integer.parseInt(edCostMin.getText().toString())){
											
											// Check area
//											ParseObject newPost = new ParseObject("Post");
//											newPost.put(MConstants.kName, edFullName.getText().toString().trim());
//											newPost.put(MConstants.kPhoneNumber, edPhone.getText().toString().trim());
//											newPost.put(MConstants.kAddress, edAddress.getText().toString().trim());
//											newPost.put(MConstants.kCostMin, edCostMin.getText().toString().trim());											
										} else {
											tvError.setVisibility(View.VISIBLE);
											tvError.setText(R.string.incorrect_cost);
										}
									}
								} else {
									tvError.setVisibility(View.VISIBLE);
									tvError.setText(R.string.empty_disctiption);
								}
							} else {
								tvError.setVisibility(View.VISIBLE);
								tvError.setText(R.string.empty_cost);
							}
						} else {
							tvError.setVisibility(View.VISIBLE);
							tvError.setText(R.string.empty_address);
						}
					} else {
						tvError.setVisibility(View.VISIBLE);
						tvError.setText(R.string.empty_phone_number);
					}
				}else{
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_full_name);
				}
			}
		});
	}
}
