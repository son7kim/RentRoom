package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.PostArticleDTO;
import com.coolsx.utils.MInterfaceNotice.onPostActicle;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyPageActivity extends Activity implements onPostActicle {

	ListView lvMyPost;
	ListPostAdapter myPostadapters;
	List<PostArticleDTO> listPost = new ArrayList<PostArticleDTO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_page);
		lvMyPost = (ListView) findViewById(R.id.lv_my_result);
		Button btnAdd = (Button) findViewById(R.id.btn_Add);

		lvMyPost.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(MyPageActivity.this, AddNewPostActivity.class);
				i.putExtra(MConstants.kPostExtraKey, listPost.get(position));
				startActivity(i);
				
				// To get extra
				//PostArticleDTO post = (PostArticleDTO)getIntent().getSerializableExtra(MConstants.kPostExtraKey);
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

		ParseQuery<PostArticleDTO> query = PostArticleDTO.getQuery();
		query.whereEqualTo(MConstants.kUserIdPost, MData.userInfo.getObjID());
		query.findInBackground(new FindCallback<PostArticleDTO>() {

			@Override
			public void done(List<PostArticleDTO> listMyPost, ParseException e) {
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
}
