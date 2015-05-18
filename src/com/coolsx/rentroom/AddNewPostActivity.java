package com.coolsx.rentroom;

import com.coolsx.constants.MConstants;
import com.coolsx.utils.UtilDroid;
import com.parse.ParseObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
		
		spCityAddNew.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				spDistrictAddNew.setAdapter(UtilDroid.getAdapterDistrict(AddNewPostActivity.this, position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {				
			}
		});
		
		btnPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addPost(0);
				tvError.setVisibility(View.GONE);
				
				// Check validate
				if(edFullName.getText().toString().trim().isEmpty()){
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_full_name);
					return;
				}
				
				if(edPhone.getText().toString().trim().isEmpty()){
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_phone_number);
					return;
				}
				
				if(edAddress.getText().toString().trim().isEmpty()){
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_address);
					return;
				}
				
				if(edCostMin.getText().toString().trim().isEmpty()){
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_cost);
					return;
				}
				
				if(edDescription.getText().toString().trim().isEmpty()){
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_disctiption);
					return;
				}
				
				if(!edCostMax.getText().toString().trim().isEmpty() && (Integer.parseInt(edCostMax.getText().toString()) < Integer.parseInt(edCostMin.getText().toString()))){
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.incorrect_cost);
					return;
				}				
				
				if(edAreaMin.getText().toString().trim().isEmpty() && edAreaMax.getText().toString().trim().isEmpty()){
					// Add post with no Area
					addPost(0);
				} else {
					if(edAreaMin.getText().toString().trim().isEmpty() && !edAreaMax.getText().toString().trim().isEmpty()){
						tvError.setVisibility(View.VISIBLE);
						tvError.setText(R.string.empty_area_min);
						return;
					} else if(!edAreaMin.getText().toString().trim().isEmpty() && edAreaMax.getText().toString().trim().isEmpty()){
						// Add post with Area min
						addPost(1);
					} else {
						// Enter full Area min & max
						if(Integer.parseInt(edAreaMax.getText().toString()) >= Integer.parseInt(edAreaMin.getText().toString())){
							// Add post with full Area (min & max)
							addPost(2);
						} else {
							tvError.setVisibility(View.VISIBLE);
							tvError.setText(R.string.incorrect_area);
							return;
						}
					}
				}				
			}
		});
	}
		
	private void addPost(int iNumArea){
		int iPos = spDistrictAddNew.getSelectedItemPosition();
		ParseObject newPost = new ParseObject("Post");
		
		newPost.put(MConstants.kName, edFullName.getText().toString().trim());
		newPost.put(MConstants.kPhoneNumber, edPhone.getText().toString().trim());
		newPost.put(MConstants.kAddress, edAddress.getText().toString().trim());
		newPost.put(MConstants.kCostMin, edCostMin.getText().toString().trim());
		if(!edCostMax.getText().toString().trim().isEmpty()){
			newPost.put(MConstants.kCostMax, edCostMax.getText().toString().trim());
		}
		if(!edNumRoom.getText().toString().trim().isEmpty()){
			newPost.put(MConstants.kNumRoom, edNumRoom.getText().toString().trim());
		}
		newPost.put(MConstants.kDiscription, edDescription.getText().toString().trim());
		switch(iNumArea){
		case 1:
			newPost.put(MConstants.kAreaMin, edAreaMin.getText().toString().trim());
			break;
		case 2:
			newPost.put(MConstants.kAreaMin, edAreaMin.getText().toString().trim());
			newPost.put(MConstants.kAreaMax, edAreaMax.getText().toString().trim());
			break;
		}
		
		
	}
}
