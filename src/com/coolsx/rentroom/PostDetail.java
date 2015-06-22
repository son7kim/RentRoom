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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.CommentDTO;
import com.coolsx.dto.ImageDTO;
import com.coolsx.helper.GetAttachFiles;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.MInterfaceNotice.onGetAttachFile;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class PostDetail extends BaseActivity implements onGetAttachFile {

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
	String strTextComment = "";
	ImageLoader imgLoader;
	LinearLayout.LayoutParams param;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_post);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.detail_header_title);

		imgLoader = ImageLoader.getInstance();
		imgLoader.init(ImageLoaderConfiguration.createDefault(PostDetail.this));
		param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 200, 1.0f);

		tvDescription = (TextView) findViewById(R.id.tvDescriptionDetail);
		tvCost = (TextView) findViewById(R.id.tvCostDetail);
		tvArea = (TextView) findViewById(R.id.tvAreaDetail);
		tvNumRoom = (TextView) findViewById(R.id.tvNumRoomDetail);
		tvAddress = (TextView) findViewById(R.id.tvAddressDetail);
		tvPhone = (TextView) findViewById(R.id.tvPhoneDetail);
		tvName = (TextView) findViewById(R.id.tvNameDetail);
		llFileAttach = (LinearLayout) findViewById(R.id.llFileAttachDetail);
		tvComments = (TextView) findViewById(R.id.tvComments);
		edComment = (EditText) findViewById(R.id.edit_comment);
		btnComment = (Button) findViewById(R.id.btn_comment);
		dialog = new DialogNotice(this);
		_comments = new ArrayList<CommentDTO>();

		setDataToView();
		setActionToView();
		if (MData.postInfo.getIsFileLoaded()) {
			if (MData.postInfo.getListImageDTO() != null) {
				handleEventsImageRoom(MData.postInfo.getListImageDTO());
			}
		} else {
			GetAttachFiles getAttach = new GetAttachFiles(this);
			getAttach.getFileAttach(MData.postInfo.getPostID());
		}

		getComment();
	}

	private void setDataToView() {
		DecimalFormat dFormat = new DecimalFormat();
		tvDescription.setText(MData.postInfo.getDescription());
		String sCost = dFormat.format((long) MData.postInfo.getCostMin()) + " VND";
		if (MData.postInfo.getCostMax() > 0 && MData.postInfo.getCostMax() > MData.postInfo.getCostMin()) {
			sCost = sCost + " - " + dFormat.format((long) MData.postInfo.getCostMax()) + " VND";
		}
		tvCost.setText(sCost);
		String sArea = "";
		if (MData.postInfo.getAreaMin() > 0) {
			sArea = sArea + MData.postInfo.getAreaMin() + " m2";
		}
		if (MData.postInfo.getAreaMax() > 0 && MData.postInfo.getAreaMax() > MData.postInfo.getAreaMin()) {
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

	private void getComment() {
		ParseQuery<CommentDTO> query = CommentDTO.getQuery();
		query.whereEqualTo(MConstants.kPostID, MData.postInfo.getPostID());
		query.orderByDescending(MConstants.kUpdatedAt);
		query.findInBackground(new FindCallback<CommentDTO>() {

			@Override
			public void done(List<CommentDTO> comments, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					_comments.addAll(comments);
					for (CommentDTO comment : _comments) {
						strTextComment += comment.getContentComment() + "\n";
					}
					tvComments.setText(strTextComment);
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}

	@Override
	public void onSendingGetFile() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessGetFile(List<ImageDTO> imgDTOs) {
		if (imgDTOs != null) {
			MData.postInfo.setIsFileLoaded(true);
			MData.postInfo.setListImageDTO(imgDTOs);
			handleEventsImageRoom(imgDTOs);
		}
	}

	private void handleEventsImageRoom(List<ImageDTO> imgDTOs) {
		final String[] imgLinks = new String[3];
		for (int i = 0; i < imgDTOs.size(); i++) {
			ImageDTO img = imgDTOs.get(i);
			imgLinks[i] = img.getFile().getUrl();
			final ImageView ivRoom = new ImageView(PostDetail.this);
			ivRoom.setLayoutParams(param);
			ivRoom.setId(i);
			if(i == 1){
				ivRoom.setPadding(10, 0, 0, 0);
			}
			if(i == 2){
				ivRoom.setPadding(10, 0, 0, 0);
			}

			imgLoader.displayImage(img.getFile().getUrl(), ivRoom);
			llFileAttach.addView(ivRoom);

			ivRoom.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(PostDetail.this, ImagePagerActivity.class);
					intent.putExtra(MConstants.pExtraImageLinks, imgLinks);
					intent.putExtra(MConstants.pExtraImagePos, ivRoom.getId());
					startActivity(intent);
				}
			});
		}
	}
}
