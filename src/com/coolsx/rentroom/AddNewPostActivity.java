package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.DistrictDTO;
import com.coolsx.dto.ImageDTO;
import com.coolsx.dto.PostArticleDTO;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.MInterfaceNotice.onPostActicle;
import com.coolsx.utils.UtilDroid;
import com.coolsx.utils.MInterfaceNotice.onDeleteFileNotify;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class AddNewPostActivity extends Activity implements onDeleteFileNotify{

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
	private TextView tvAddress;
	private Button btnPost;
	private ArrayAdapter<String> adapCity;
	private ArrayAdapter<String> adapDistrict;
	private List<DistrictDTO> districtAddNewDTOs = new ArrayList<DistrictDTO>();
	List<ImageDTO> listImgDTO = new ArrayList<ImageDTO>();
	OnAddFileImage onAddFile;
	int iNumFile = 0;
	DialogNotice dialog;
	String sRandomUUID;
	static onPostActicle postActicleDelegate;
	Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_new_post_page);
		
		edFullName = (EditText) findViewById(R.id.edit_fullname);
		edPhone = (EditText) findViewById(R.id.edit_phone_number);
		spCityAddNew = (Spinner) findViewById(R.id.spinCityAddNew);
		spDistrictAddNew = (Spinner) findViewById(R.id.spinDistrictAddNew);
		edAddress = (EditText) findViewById(R.id.edit_address);
		tvAddress = (TextView) findViewById(R.id.tv_address_detail);
		edNumRoom = (EditText) findViewById(R.id.edit_num_room);
		edAreaMin = (EditText) findViewById(R.id.edit_area_min);
		edAreaMax = (EditText) findViewById(R.id.edit_area_max);
		edCostMin = (EditText) findViewById(R.id.edit_cost_min);
		edCostMax = (EditText) findViewById(R.id.edit_cost_max);
		edDescription = (EditText) findViewById(R.id.edit_discription);
		tvChooseImg = (TextView) findViewById(R.id.tv_upload_img);
		llFileAttach = (LinearLayout) findViewById(R.id.llFileAttach);
		tvError = (TextView) findViewById(R.id.tv_error_add_new_post);
		btnPost = (Button) findViewById(R.id.btn_post);

		onAddFile = new OnAddFileImage(this, this);
		dialog = new DialogNotice(this);		

		adapCity = MData.adapterCity;
		adapDistrict = UtilDroid.getAdapterDistrictFromKey(
				AddNewPostActivity.this, MData.cityDTOs.get(0),
				districtAddNewDTOs);

		tvAddress.setText("..., " + districtAddNewDTOs.get(0).getDistrictName()
				+ ", " + MData.cityDTOs.get(0).getCityName());

		spCityAddNew.setAdapter(adapCity);
		spDistrictAddNew.setAdapter(adapDistrict);
		spCityAddNew.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				spDistrictAddNew.setAdapter(UtilDroid
						.getAdapterDistrictFromKey(AddNewPostActivity.this,
								MData.cityDTOs.get(position),
								districtAddNewDTOs));
				tvAddress.setText("..., "
						+ districtAddNewDTOs.get(
								spDistrictAddNew.getSelectedItemPosition())
								.getDistrictName() + ", "
						+ MData.cityDTOs.get(position).getCityName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		spDistrictAddNew
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						tvAddress.setText("..., "
								+ districtAddNewDTOs.get(position)
										.getDistrictName()
								+ ", "
								+ MData.cityDTOs.get(
										spCityAddNew.getSelectedItemPosition())
										.getCityName());
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		tvChooseImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (iNumFile < 3) {
					UtilDroid.selectImage(AddNewPostActivity.this);
				} else {
					dialog.ShowDialog("Thong bao", "Upload toi da 3 file");
				}
			}
		});

		btnPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				tvError.setVisibility(View.GONE);

				// Check validate
				if (edFullName.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_full_name);
					return;
				}

				if (edPhone.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_phone_number);
					return;
				}

				if (edAddress.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_address);
					return;
				}

				if (edNumRoom.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_num_room);
					return;
				}

				if (edCostMin.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_cost);
					return;
				}

				if (edDescription.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_disctiption);
					return;
				}

				if (!edCostMax.getText().toString().trim().isEmpty()
						&& (Integer.parseInt(edCostMax.getText().toString()) < Integer
								.parseInt(edCostMin.getText().toString()))) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.incorrect_cost);
					return;
				}

				if (edAreaMin.getText().toString().trim().isEmpty()
						&& edAreaMax.getText().toString().trim().isEmpty()) {
					// Add post with no Area
					addPost(0);
				} else {
					if (edAreaMin.getText().toString().trim().isEmpty()
							&& !edAreaMax.getText().toString().trim().isEmpty()) {
						tvError.setVisibility(View.VISIBLE);
						tvError.setText(R.string.empty_area_min);
						return;
					} else if (!edAreaMin.getText().toString().trim().isEmpty()
							&& edAreaMax.getText().toString().trim().isEmpty()) {
						// Add post with Area min
						addPost(1);
					} else {
						// Enter full Area min & max
						if (Integer.parseInt(edAreaMax.getText().toString()) >= Integer
								.parseInt(edAreaMin.getText().toString())) {
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

	public static void initDelegate(onPostActicle delegate){
		postActicleDelegate = delegate;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			byte[] bArrImages = UtilDroid.getByteArrFromDataIntent(
					AddNewPostActivity.this, data, requestCode);
			if (bArrImages.length > 0) {
				iNumFile++;

				ImageDTO imgDTO = new ImageDTO(UtilDroid.getRandomStringNumber(),
						bArrImages);
				listImgDTO.add(imgDTO);

				llFileAttach.removeAllViews();
				onAddFile.InitView(listImgDTO);
				llFileAttach.addView(onAddFile);
			} else {
				dialog.ShowDialog("Thong bao", "Khong the chon hinh nay");
			}
		}
	}

	private void addPost(int iNumArea) {
		//ParseObject newPost = new ParseObject(MConstants.kTablePost);
		final PostArticleDTO newPost = new PostArticleDTO();
		
		newPost.put(MConstants.kName, edFullName.getText().toString().trim());
		newPost.put(MConstants.kPhoneNumber, edPhone.getText().toString()
				.trim());
		String address = edAddress.getText().toString().trim() + ", " + 
				districtAddNewDTOs.get(spDistrictAddNew.getSelectedItemPosition()).getDistrictName() + ", " +
				MData.cityDTOs.get(spCityAddNew.getSelectedItemPosition()).getCityName();
		newPost.put(MConstants.kAddress, address);
		newPost.put(MConstants.kCostMin, edCostMin.getText().toString().trim());
		if (!edCostMax.getText().toString().trim().isEmpty()) {
			newPost.put(MConstants.kCostMax, edCostMax.getText().toString()
					.trim());
		}
		newPost.put(MConstants.kNumRoom, edNumRoom.getText().toString().trim());
		
		newPost.put(MConstants.kDiscription, edDescription.getText().toString()
				.trim());
		switch (iNumArea) {
		case 1:
			newPost.put(MConstants.kAreaMin, edAreaMin.getText().toString()
					.trim());
			break;
		case 2:
			newPost.put(MConstants.kAreaMin, edAreaMin.getText().toString()
					.trim());
			newPost.put(MConstants.kAreaMax, edAreaMax.getText().toString()
					.trim());
			break;
		}
		
		newPost.put(MConstants.kUserIdPost, MData.userInfo.getObjID());
		
		newPost.put(MConstants.kDistrictID, districtAddNewDTOs.get(spDistrictAddNew.getSelectedItemPosition()).getDistrictID());
		
		sRandomUUID = UtilDroid.getRandomStringUUID();
		newPost.put(MConstants.kPostID, sRandomUUID);
		newPost.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if(e == null) {
					postActicleDelegate.onSuccess(newPost);
					finish();
				} else {
					dialog.ShowDialog(getResources().getString(R.string.post_title), getResources().getString(R.string.post_error));
				}
			}
		});
		
		for(ImageDTO imgDTO : listImgDTO) {
			ParseFile file = new ParseFile(imgDTO.getFileName() + ".png", imgDTO.getByteArrData());
			// Upload the image into Parse Cloud
			file.saveInBackground();

			// Create a New Class called "ImageUpload" in Parse
			ParseObject imgupload = new ParseObject(MConstants.kTableImage);

			// Create a column named "ImageName" and set the string
			imgupload.put(MConstants.kImgName, imgDTO.getFileName() + ".png");
			imgupload.put(MConstants.kPostID, sRandomUUID);
			// Create a column named "ImageFile" and insert the image
			imgupload.put(MConstants.kImgFile, file);	
			
			imgupload.saveInBackground();
		}
	}

	@Override
	public void onDeleteNotify(List<ImageDTO> listImgAfterDelete) {
		iNumFile --;
		llFileAttach.removeAllViews();
		listImgDTO.clear();
		listImgDTO = listImgAfterDelete;
		onAddFile.InitView(listImgDTO);
		llFileAttach.addView(onAddFile);
	}
}
