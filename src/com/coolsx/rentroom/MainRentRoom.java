package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.DistrictDTO;
import com.coolsx.dto.PostArticleDTO;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.UtilDroid;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class MainRentRoom extends Activity {

	Spinner spCity;
	Spinner spDistrict;
	Spinner spCostCompair;
	Spinner spAraeCompair;
	EditText edCost;
	EditText edArea;
	private List<DistrictDTO> districtDTOsHome = new ArrayList<DistrictDTO>();
	private List<PostArticleDTO> listPost = new ArrayList<PostArticleDTO>();
	private ListPostAdapter listPostadapters;
	private ListView lvPost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rentroom_main);

		TextView tvSignIn = (TextView) findViewById(R.id.tvSignInHome);
		Button btnSignUp = (Button) findViewById(R.id.btnRegistryHome);
		Button btnSearch = (Button) findViewById(R.id.btn_search);
		spCity = (Spinner) findViewById(R.id.spinCity);
		spDistrict = (Spinner) findViewById(R.id.spinDistrict);
		spCostCompair = (Spinner) findViewById(R.id.spinEqualCost);
		spAraeCompair = (Spinner) findViewById(R.id.spinEqualArea);
		lvPost = (ListView) findViewById(R.id.lv_search_result);
		edCost = (EditText) findViewById(R.id.edit_cost_search);
		edArea = (EditText) findViewById(R.id.edit_area_search);

		spCity.setAdapter(UtilDroid.getAdapterCity(this));
		spCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				spDistrict.setAdapter(UtilDroid.getAdapterDistrictFromKey(
						MainRentRoom.this, MData.cityDTOs.get(position),
						districtDTOsHome));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		tvSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainRentRoom.this, SignInActivity.class);
				startActivity(i);
			}
		});

		btnSignUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainRentRoom.this, SignUpActivity.class);
				startActivity(i);
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getListPost(true, -1);
			}
		});

		getListPost(false, 15);
		
		lvPost.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MData.postInfo = listPost.get(position);
				Intent i = new Intent(MainRentRoom.this, PostDetail.class);				
				startActivity(i);
			}
		});
		
	}
	DialogNotice notice = new DialogNotice(MainRentRoom.this);
	private void getListPost(boolean isSearch, int iLimit) {
		ParseQuery<PostArticleDTO> query = PostArticleDTO.getQuery();
		if (isSearch) {
			query.whereContains(MConstants.kDistrictID, districtDTOsHome.get(spDistrict.getSelectedItemPosition()).getDistrictID());
			query.orderByDescending(MConstants.kUpdatedAt);
			
			if(!edCost.getText().toString().isEmpty()){
				switch (spCostCompair.getSelectedItemPosition()) {
				case 0:
					 query.whereGreaterThanOrEqualTo(MConstants.kCostMax,Long.valueOf(edCost.getText().toString()));
					break;
				case 1:
					query.whereLessThanOrEqualTo(MConstants.kCostMin,Long.valueOf(edCost.getText().toString()));
					break;
				case 2:
					query.whereEqualTo(MConstants.kCostMin,Long.valueOf(edCost.getText().toString()));					
					break;
				}
			}
			
			if(!edArea.getText().toString().isEmpty()){
				switch (spAraeCompair.getSelectedItemPosition()) {
				case 0:
					 query.whereGreaterThanOrEqualTo(MConstants.kAreaMax,Long.valueOf(edArea.getText().toString()));
					break;
				case 1:
					query.whereLessThanOrEqualTo(MConstants.kAreaMin,Long.valueOf(edArea.getText().toString()));
					break;
				case 2:
					query.whereEqualTo(MConstants.kAreaMin,Long.valueOf(edArea.getText().toString()));
					break;
				}
			}
		} else {
			query.whereEqualTo(MConstants.kCityID, "hcm");
			query.setLimit(iLimit);
		}

		query.findInBackground(new FindCallback<PostArticleDTO>() {

			@Override
			public void done(List<PostArticleDTO> listPostQuery,
					ParseException e) {
				if (e == null) {
					listPost.clear();
					listPost.addAll(listPostQuery);
					listPostadapters = new ListPostAdapter(MainRentRoom.this,
							listPost);
					lvPost.setAdapter(listPostadapters);
				} else {
					
					notice.ShowDialog("Error", e.getMessage());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_rent_room, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
