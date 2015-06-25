package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.PostArticleDTO;
import com.coolsx.helper.DeleteCommentsPost;
import com.coolsx.helper.DeleteImagesPost;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.UtilDroid;
import com.coolsx.utils.MInterfaceNotice.onPostActicle;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class MyPageActivity extends BaseActivity implements onPostActicle {

	ListView lvMyPost;
	ListPostAdapter myPostadapters;
	List<PostArticleDTO> listPost = new ArrayList<PostArticleDTO>();
	LinearLayout llProgress;
	DialogNotice dialog;
	boolean isEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_page);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.my_page_header_title);

		dialog = new DialogNotice(this);

		llProgress = (LinearLayout) findViewById(R.id.llProgressBar);

		lvMyPost = (ListView) findViewById(R.id.lv_my_result);
		Button btnAdd = (Button) findViewById(R.id.btn_Add);

		registerForContextMenu(lvMyPost);

		lvMyPost.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MData.postInfo = listPost.get(position);
				Intent i = new Intent(MyPageActivity.this, PostDetail.class);
				startActivity(i);
			}
		});

		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddNewPostActivity.initDelegate(MyPageActivity.this);
				Intent i = new Intent(MyPageActivity.this, AddNewPostActivity.class);
				startActivity(i);
			}
		});

		getMyPosts();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.lv_my_result) {
			menu.setHeaderTitle(getResources().getString(R.string.option_title));
			menu.add(Menu.NONE, 0, 0, getResources().getString(R.string.delete_option));
			menu.add(Menu.NONE, 1, 1, getResources().getString(R.string.edit_option));
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int index = item.getItemId();
		if (index == 0) {
			onDeletePost(info.position);
		} else {
			onEditPost(info.position);
		}
		return super.onContextItemSelected(item);
	}

	private void onDeletePost(final int pos) {
		llProgress.setVisibility(View.VISIBLE);
		ParseQuery<PostArticleDTO> query = PostArticleDTO.getQuery();
		query.whereEqualTo(MConstants.kPostID, listPost.get(pos).getPostID());
		query.getFirstInBackground(new GetCallback<PostArticleDTO>() {

			@Override
			public void done(PostArticleDTO object, ParseException e) {
				if (e == null) {
					object.deleteInBackground(new DeleteCallback() {

						@Override
						public void done(ParseException e) {
							llProgress.setVisibility(View.GONE);
							if (e == null) {
								/* Delete all images and comments relative*/
								DeleteImagesPost deleteImg = new DeleteImagesPost();
								deleteImg.deleteImages(listPost.get(pos).getPostID());

								DeleteCommentsPost deleteCmd = new DeleteCommentsPost();
								deleteCmd.deleteComments(listPost.get(pos).getPostID());
								
								/*Update lisview*/
								listPost.remove(pos);
								myPostadapters = new ListPostAdapter(MyPageActivity.this, listPost);
								lvMyPost.setAdapter(myPostadapters);
							} else {
								dialog.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.delete_fail_notice));
							}
						}
					});
				} else {
					llProgress.setVisibility(View.GONE);
					dialog.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.delete_fail_notice));
				}
			}
		});
	}

	private void onEditPost(int pos) {
		isEdit = true;
		MData.postInfo = listPost.get(pos);
		AddNewPostActivity.initDelegate(MyPageActivity.this);
		Intent i = new Intent(MyPageActivity.this, AddNewPostActivity.class);
		i.putExtra(MConstants.kIsEdit, true);
		startActivity(i);
	}

	private void getMyPosts() {
		if (!UtilDroid.checkInternet()) {
			dialog.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.internet_error));
			return;
		}
		ParseQuery<PostArticleDTO> query = PostArticleDTO.getQuery();
		query.whereEqualTo(MConstants.kUserIdPost, MData.userInfo.getObjID());
		query.orderByDescending(MConstants.kUpdatedAt);
		query.findInBackground(new FindCallback<PostArticleDTO>() {

			@Override
			public void done(List<PostArticleDTO> listMyPost, ParseException e) {
				llProgress.setVisibility(View.GONE);
				if (e == null) {
					listPost.clear();
					listPost.addAll(listMyPost);
					myPostadapters = new ListPostAdapter(MyPageActivity.this, listPost);
					lvMyPost.setAdapter(myPostadapters);
				} else{
					dialog.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.data_error));
				}
			}
		});
	}

	@Override
	public void onSuccess(PostArticleDTO postActicle) {
		if (!isEdit) {
			getMyPosts();
		} else {
			myPostadapters = new ListPostAdapter(MyPageActivity.this, listPost);
			lvMyPost.setAdapter(myPostadapters);
			isEdit = false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
}
