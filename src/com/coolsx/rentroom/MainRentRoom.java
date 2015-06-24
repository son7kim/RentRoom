package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.coolsx.constants.MConstants;
import com.coolsx.constants.MData;
import com.coolsx.dto.District;
import com.coolsx.dto.PostArticleDTO;
import com.coolsx.helper.GetCityAndDistrict;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.MInterfaceNotice.onGetDistrictNotify;
import com.coolsx.utils.MInterfaceNotice.onOkLoadData;
import com.coolsx.utils.UtilDroid;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainRentRoom extends BaseActivity implements onOkLoadData, onGetDistrictNotify {

	Spinner spCity;
	Spinner spDistrict;
	Spinner spCostCompair;
	Spinner spAraeCompair;
	EditText edCost;
	EditText edArea;
	TextView tvSignIn;
	Button btnSearch;
	Button btnSignUp;
	LinearLayout llSignInUp;
	private List<District> districtDTOsHome = new ArrayList<District>();
	private List<PostArticleDTO> listPost = new ArrayList<PostArticleDTO>();
	private ListPostAdapter listPostadapters;
	private ListView lvPost;
	Menu _menu;
	DialogNotice notice;
	GetCityAndDistrict getCityDistrict;
	DialogNotice noticeLoadData;
	LinearLayout llProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rentroom_main);

		getActionBar().setDisplayShowHomeEnabled(false);

		notice = new DialogNotice(MainRentRoom.this);
		noticeLoadData = new DialogNotice(MainRentRoom.this, MainRentRoom.this);
		getCityDistrict = new GetCityAndDistrict(this, this, false);

		tvSignIn = (TextView) findViewById(R.id.tvSignInHome);
		btnSignUp = (Button) findViewById(R.id.btnRegistryHome);
		btnSearch = (Button) findViewById(R.id.btn_search);
		spCity = (Spinner) findViewById(R.id.spinCity);
		spDistrict = (Spinner) findViewById(R.id.spinDistrict);
		spCostCompair = (Spinner) findViewById(R.id.spinEqualCost);
		spAraeCompair = (Spinner) findViewById(R.id.spinEqualArea);
		lvPost = (ListView) findViewById(R.id.lv_search_result);
		edCost = (EditText) findViewById(R.id.edit_cost_search);
		edArea = (EditText) findViewById(R.id.edit_area_search);
		llSignInUp = (LinearLayout) findViewById(R.id.llSignIn_SignUp);

		llProgress = (LinearLayout) findViewById(R.id.llProgressBar);
		llProgress.setVisibility(View.GONE);

		setDataToSpinner();

		setActionToView();
	}

	private void setDataToSpinner() {
		if (MData.districtDTOs.size() > 0 && MData.cityDTOs.size() > 0) {
			spCity.setAdapter(UtilDroid.getAdapterCity(this));
			spDistrict.setAdapter(UtilDroid.getAdapterDistrictFromKey(MainRentRoom.this, MData.cityDTOs.get(0), districtDTOsHome));
			getListPost(false, 150);
		} else {
			noticeLoadData.ShowDialogRequestData(getResources().getString(R.string.notice_title), getResources().getString(R.string.data_error));
		}
	}

	private void setActionToView() {
		spCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				spDistrict.setAdapter(UtilDroid.getAdapterDistrictFromKey(MainRentRoom.this, MData.cityDTOs.get(position), districtDTOsHome));
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
				getListPost(true, 100);
			}
		});

		lvPost.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MData.postInfo = listPost.get(position);
				Intent i = new Intent(MainRentRoom.this, PostDetail.class);
				startActivity(i);
			}
		});
	}

	private void getListPost(boolean isSearch, int iLimit) {
		if (!UtilDroid.checkInternet()) {
			notice.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.internet_error));
			return;
		}
		llProgress.setVisibility(View.VISIBLE);

		ParseQuery<PostArticleDTO> query = PostArticleDTO.getQuery();
		query.orderByDescending(MConstants.kUpdatedAt);
		if (isSearch) {
			query.whereContains(MConstants.kDistrictID, districtDTOsHome.get(spDistrict.getSelectedItemPosition()).getDistrictID());

			if (!edCost.getText().toString().isEmpty()) {
				switch (spCostCompair.getSelectedItemPosition()) {
				case 0:
					query.whereGreaterThanOrEqualTo(MConstants.kCostMin, Long.valueOf(edCost.getText().toString()));
					break;
				case 1:
					query.whereLessThanOrEqualTo(MConstants.kCostMin, Long.valueOf(edCost.getText().toString()));
					break;
				case 2:
					query.whereEqualTo(MConstants.kCostMin, Long.valueOf(edCost.getText().toString()));
					break;
				}
			}

			if (!edArea.getText().toString().isEmpty()) {
				switch (spAraeCompair.getSelectedItemPosition()) {
				case 0:
					query.whereGreaterThanOrEqualTo(MConstants.kAreaMin, Long.valueOf(edArea.getText().toString()));
					break;
				case 1:
					query.whereLessThanOrEqualTo(MConstants.kAreaMin, Long.valueOf(edArea.getText().toString()));
					break;
				case 2:
					query.whereEqualTo(MConstants.kAreaMin, Long.valueOf(edArea.getText().toString()));
					break;
				}
			}
		} else {
			query.whereEqualTo(MConstants.kCityID, "hcm");
			query.setLimit(iLimit);
		}

		query.findInBackground(new FindCallback<PostArticleDTO>() {

			@Override
			public void done(List<PostArticleDTO> listPostQuery, ParseException e) {
				llProgress.setVisibility(View.GONE);
				if (e == null) {
					listPost.clear();
					listPost.addAll(listPostQuery);
					listPostadapters = new ListPostAdapter(MainRentRoom.this, listPost);
					lvPost.setAdapter(listPostadapters);
				} else {
					notice.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.data_error));
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (this._menu == null) {
			getMenuInflater().inflate(R.menu.main_rent_room, menu);
			this._menu = menu;
			menu.setGroupVisible(0, false);
			llSignInUp.setVisibility(View.VISIBLE);

			if (MData.userInfo != null) {
				if (this._menu != null) {
					this._menu.setGroupVisible(0, true);
					llSignInUp.setVisibility(View.GONE);
				}
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_my_page:
			Intent i = new Intent(MainRentRoom.this, MyPageActivity.class);
			startActivity(i);
			break;
		case R.id.action_favorite:
			break;
		case R.id.action_change_pass:
			Intent intent = new Intent(MainRentRoom.this, ChangePassword.class);
			startActivity(intent);
			break;
		case R.id.action_sign_out:
			ParseUser.logOutInBackground();
			MData.userInfo = null;
			_menu.setGroupVisible(0, false);
			llSignInUp.setVisibility(View.VISIBLE);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MData.userInfo != null) {
			if (this._menu != null) {
				this._menu.setGroupVisible(0, true);
				llSignInUp.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.addCategory(Intent.CATEGORY_HOME);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
		return true;
	}

	@Override
	public void onOkClick() {
		if (UtilDroid.checkInternet()) {
			llProgress.setVisibility(View.VISIBLE);
			getCityDistrict.getListCity();
		} else {
			noticeLoadData.ShowDialogRequestData(getResources().getString(R.string.notice_title), getResources().getString(R.string.data_error));
		}

	}

	@Override
	public void onFinishLoadDataFromSplash() {
	}

	@Override
	public void onFinishLoadDataFromMain() {
		llProgress.setVisibility(View.GONE);
		setDataToSpinner();
	}
}
