package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.PostArticleDTO;
import com.coolsx.utils.MInterfaceNotice.onPostActicle;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class MyPageActivity extends BaseActivity implements onPostActicle {

	ListView lvMyPost;
	ListPostAdapter myPostadapters;
	List<PostArticleDTO> listPost = new ArrayList<PostArticleDTO>();
	LinearLayout llProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_page);
		
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.my_page_header_title);
		
		llProgress = (LinearLayout)findViewById(R.id.llProgressBar);
		
		lvMyPost = (ListView) findViewById(R.id.lv_my_result);
		Button btnAdd = (Button) findViewById(R.id.btn_Add);

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

	private void getMyPosts(){
		ParseQuery<PostArticleDTO> query = PostArticleDTO.getQuery();
		query.whereEqualTo(MConstants.kUserIdPost, MData.userInfo.getObjID());
		query.findInBackground(new FindCallback<PostArticleDTO>() {

			@Override
			public void done(List<PostArticleDTO> listMyPost, ParseException e) {
				llProgress.setVisibility(View.GONE);
				if (e == null) {
					listPost.clear();
					listPost.addAll(listMyPost);
					myPostadapters = new ListPostAdapter(MyPageActivity.this, listPost);
					lvMyPost.setAdapter(myPostadapters);
				}
			}
		});
	}
	
	@Override
	public void onSuccess(PostArticleDTO postActicle) {
		listPost.add(postActicle);
		myPostadapters = new ListPostAdapter(MyPageActivity.this, listPost);
		lvMyPost.setAdapter(myPostadapters);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
}
