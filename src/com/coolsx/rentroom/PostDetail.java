package com.coolsx.rentroom;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.CommentDTO;
import com.coolsx.dto.ImageDTO;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.MInterfaceNotice.onDeleteFileNotify;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class PostDetail extends BaseActivity implements onDeleteFileNotify {

	TextView tvDescription;
	TextView tvCost;
	TextView tvArea;
	TextView tvNumRoom;
	TextView tvAddress;
	TextView tvPhone;
	TextView tvName;
	TextView tvComments;
	EditText edComment;
	Button btnComment;
	LinearLayout llFileAttach;
	OnAddFileImage onAddFile;
	DialogNotice dialog;
	List<CommentDTO> _comments;
	String strTextComment="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_post);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.detail_header_title);
		
		tvDescription = (TextView) findViewById(R.id.tvDescriptionDetail);
		tvCost = (TextView) findViewById(R.id.tvCostDetail);
		tvArea = (TextView) findViewById(R.id.tvAreaDetail);
		tvNumRoom = (TextView) findViewById(R.id.tvNumRoomDetail);
		tvAddress = (TextView) findViewById(R.id.tvAddressDetail);
		tvPhone = (TextView) findViewById(R.id.tvPhoneDetail);
		tvName = (TextView) findViewById(R.id.tvNameDetail);
		llFileAttach = (LinearLayout) findViewById(R.id.llFileAttachDetail);
		tvComments = (TextView)findViewById(R.id.tvComments);
		edComment = (EditText) findViewById(R.id.edit_comment);
		btnComment = (Button) findViewById(R.id.btn_comment);

		onAddFile = new OnAddFileImage(this, this);
		dialog = new DialogNotice(this);
		_comments = new ArrayList<CommentDTO>();

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
		
		getComment();
	}

	private void setDataToView() {
		DecimalFormat dFormat = new DecimalFormat();
		tvDescription.setText(MData.postInfo.getDescription());
		String sCost = dFormat.format((long) MData.postInfo.getCostMin()) + " VND";
		if (MData.postInfo.getCostMax() > 0) {
			sCost = sCost + " - " + dFormat.format((long) MData.postInfo.getCostMax()) + " VND";
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
		tvAddress.setText("Địa chỉ: " + MData.postInfo.getAddress());
		tvPhone.setText("" + MData.postInfo.getPhoneNumber());
		tvName.setText("Tên liên hệ: " + MData.postInfo.getFullName());
	}

	private void setActionToView() {
		tvPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:+" + tvPhone.getText().toString().trim()));
					startActivity(callIntent);
				} catch (Exception e) {
				}
			}
		});

		btnComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MData.userInfo == null) {
					dialog.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.sign_in_notice));
				} else {
					if (edComment.getText().toString().trim().isEmpty()) {
						dialog.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.comment_content_notice));
					} else {
						CommentDTO cmtDTO = new CommentDTO();
						cmtDTO.put(MConstants.kPostID, MData.postInfo.getPostID());
						cmtDTO.put(MConstants.kContentComment, MData.userInfo.getUserName() + ": " + edComment.getText().toString().trim());
						cmtDTO.saveInBackground();
						
						// set content for view
						strTextComment += MData.userInfo.getUserName() + ": " + edComment.getText().toString().trim() + "\n";
						tvComments.setText(strTextComment);
						edComment.setText("");
					}
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

	private void getComment(){
		ParseQuery<CommentDTO> query = CommentDTO.getQuery();
		query.whereEqualTo(MConstants.kPostID, MData.postInfo.getPostID());
		query.orderByDescending(MConstants.kUpdatedAt);
		query.findInBackground(new FindCallback<CommentDTO>() {
			
			@Override
			public void done(List<CommentDTO> comments, ParseException e) {
				// TODO Auto-generated method stub
				if(e == null){
					_comments.addAll(comments);
					for(CommentDTO comment:_comments){
						strTextComment += comment.getContentComment() + "\n";						
					}
					tvComments.setText(strTextComment);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
}
