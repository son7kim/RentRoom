package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.coolsx.utils.MInterfaceNotice.onDeleteFileNotify;
import com.coolsx.utils.MInterfaceNotice.onPostActicle;
import com.coolsx.utils.UtilDroid;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class AddNewPostActivity extends BaseActivity implements onDeleteFileNotify {

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
	LinearLayout llProgress;
	boolean isEditPost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_new_post_page);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.add_new_header_title);

		isEditPost = getIntent().getBooleanExtra(MConstants.kIsEdit, false);
		
		llProgress = (LinearLayout) findViewById(R.id.llProgressBar);
		llProgress.setVisibility(View.GONE);

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
		adapDistrict = UtilDroid.getAdapterDistrictFromKey(AddNewPostActivity.this, MData.cityDTOs.get(0), districtAddNewDTOs);

		tvAddress.setText("..., " + districtAddNewDTOs.get(0).getDistrictName() + ", " + MData.cityDTOs.get(0).getCityName());

		spCityAddNew.setAdapter(adapCity);				
		spDistrictAddNew.setAdapter(adapDistrict);
		
		if(isEditPost){
			getActionBar().setTitle(R.string.edit_option);
			btnPost.setText(R.string.edit_button_text);
			int i = 0;
			for(i = 0; i < MData.cityDTOs.size(); i ++){
				if(MData.cityDTOs.get(i).getCityID().equals(MData.postInfo.getCityID())){
					spCityAddNew.setSelection(i);
					break;
				}
			}	
			adapDistrict = UtilDroid.getAdapterDistrictFromKey(AddNewPostActivity.this, MData.cityDTOs.get(i), districtAddNewDTOs);
			spDistrictAddNew.setAdapter(adapDistrict);
			for(int k = 0; k < districtAddNewDTOs.size(); k ++){
				if(districtAddNewDTOs.get(k).getDistrictID().equals(MData.postInfo.getDistricID())){
					spDistrictAddNew.setSelection(k);
					break;
				}
			}
			
			setDataEdit();
		}		

		spCityAddNew.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spDistrictAddNew.setAdapter(UtilDroid.getAdapterDistrictFromKey(AddNewPostActivity.this, MData.cityDTOs.get(position), districtAddNewDTOs));
				tvAddress.setText("..., " + districtAddNewDTOs.get(spDistrictAddNew.getSelectedItemPosition()).getDistrictName() + ", "
						+ MData.cityDTOs.get(position).getCityName());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		spDistrictAddNew.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				tvAddress.setText("..., " + districtAddNewDTOs.get(position).getDistrictName() + ", " + MData.cityDTOs.get(spCityAddNew.getSelectedItemPosition()).getCityName());
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

				if (edAreaMin.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_area);
					return;
				}

				if (!edAreaMax.getText().toString().trim().isEmpty() && (Integer.parseInt(edAreaMax.getText().toString()) < Integer.parseInt(edAreaMin.getText().toString()))) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.incorrect_area);
					return;
				}

				if (edCostMin.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_cost);
					return;
				}

				if (!edCostMax.getText().toString().trim().isEmpty() && (Integer.parseInt(edCostMax.getText().toString()) < Integer.parseInt(edCostMin.getText().toString()))) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.incorrect_cost);
					return;
				}

				if (edDescription.getText().toString().trim().isEmpty()) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.empty_disctiption);
					return;
				}

				addPost();
			}
		});
	}

	public static void initDelegate(onPostActicle delegate) {
		postActicleDelegate = delegate;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			byte[] bArrImages = UtilDroid.getByteArrFromDataIntent(AddNewPostActivity.this, data, requestCode);
			if (bArrImages.length > 0) {
				iNumFile++;

				ImageDTO imgDTO = new ImageDTO(UtilDroid.getRandomStringNumber(), bArrImages);
				listImgDTO.add(imgDTO);

				llFileAttach.removeAllViews();
				onAddFile.InitView(listImgDTO, true);
				llFileAttach.addView(onAddFile);
			} else {
				dialog.ShowDialog("Thong bao", "Khong the chon hinh nay");
			}
		}
	}

	private void setDataEdit(){
		edFullName.setText(MData.postInfo.getFullName());
		edPhone.setText(MData.postInfo.getPhoneNumber());
		edAddress.setText(MData.postInfo.getAddress());
		edNumRoom.setText("" +MData.postInfo.getNumRoom());
		edAreaMin.setText("" + MData.postInfo.getAreaMin());		
		edAreaMax.setText("" + MData.postInfo.getAreaMax());
		edCostMin.setText(""+ MData.postInfo.getCostMin());
		edCostMax.setText("" + MData.postInfo.getCostMax());
		edDescription.setText(MData.postInfo.getDescription());		
		tvAddress.setText("..., " + districtAddNewDTOs.get(spDistrictAddNew.getSelectedItemPosition()).getDistrictName() + ", " + MData.cityDTOs.get(spCityAddNew.getSelectedItemPosition()).getCityName());
	}
	
	private void addPost() {
		llProgress.setVisibility(View.VISIBLE);

		final PostArticleDTO newPost = new PostArticleDTO();

		newPost.put(MConstants.kName, edFullName.getText().toString().trim());
		newPost.put(MConstants.kPhoneNumber, edPhone.getText().toString().trim());
		String address = edAddress.getText().toString().trim() + ", " + districtAddNewDTOs.get(spDistrictAddNew.getSelectedItemPosition()).getDistrictName() + ", "
				+ MData.cityDTOs.get(spCityAddNew.getSelectedItemPosition()).getCityName();
		newPost.put(MConstants.kAddress, address);
		newPost.put(MConstants.kCostMin, Long.valueOf(edCostMin.getText().toString().trim()));
		if (!edCostMax.getText().toString().trim().isEmpty()) {
			newPost.put(MConstants.kCostMax, Long.valueOf(edCostMax.getText().toString().trim()));
		} else {
			newPost.put(MConstants.kCostMax, Long.valueOf(edCostMin.getText().toString().trim()));
		}
		newPost.put(MConstants.kNumRoom, Long.valueOf(edNumRoom.getText().toString().trim()));

		newPost.put(MConstants.kDiscription, edDescription.getText().toString().trim());

		newPost.put(MConstants.kAreaMin, Long.valueOf(edAreaMin.getText().toString().trim()));
		if (!edAreaMax.getText().toString().trim().isEmpty()) {
			newPost.put(MConstants.kAreaMax, Long.valueOf(edAreaMax.getText().toString().trim()));
		} else {
			newPost.put(MConstants.kAreaMax, Long.valueOf(edAreaMin.getText().toString().trim()));
		}

		newPost.put(MConstants.kUserIdPost, MData.userInfo.getObjID());

		newPost.put(MConstants.kDistrictID, districtAddNewDTOs.get(spDistrictAddNew.getSelectedItemPosition()).getDistrictID());
		newPost.put(MConstants.kCityID, MData.cityDTOs.get(spCityAddNew.getSelectedItemPosition()).getCityID());

		sRandomUUID = UtilDroid.getRandomStringUUID();
		newPost.put(MConstants.kPostID, sRandomUUID);
		newPost.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				llProgress.setVisibility(View.GONE);
				if (e == null) {
					postActicleDelegate.onSuccess(newPost);
					finish();
				} else {
					dialog.ShowDialog(getResources().getString(R.string.post_title), getResources().getString(R.string.post_error));
				}
			}
		});

		for (ImageDTO imgDTO : listImgDTO) {
			ParseFile file = new ParseFile(imgDTO.getFileNameLocal() + ".png", imgDTO.getByteArrDataLocal());
			// Upload the image into Parse Cloud
			file.saveInBackground();

			// Create a New Class called "ImageUpload" in Parse
			ParseObject imgupload = new ParseObject(MConstants.kTableImage);

			// Create a column named "ImageName" and set the string
			imgupload.put(MConstants.kImgName, imgDTO.getFileNameLocal() + ".png");
			imgupload.put(MConstants.kPostID, sRandomUUID);
			// Create a column named "ImageFile" and insert the image
			imgupload.put(MConstants.kImgFile, file);

			imgupload.saveInBackground();
		}
	}

	@Override
	public void onDeleteNotify(List<ImageDTO> listImgAfterDelete) {
		iNumFile--;
		llFileAttach.removeAllViews();
		listImgDTO.clear();
		listImgDTO = listImgAfterDelete;
		onAddFile.InitView(listImgDTO, true);
		llFileAttach.addView(onAddFile);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
}
