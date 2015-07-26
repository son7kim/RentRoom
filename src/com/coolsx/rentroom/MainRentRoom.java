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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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
	private InterstitialAd interstitial;
	private AdView adView;
	/**
	 * Check first time to show ads when click into listview item
	 */
	private boolean isShowAdsWhenFistClickListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rentroom_main);

		getActionBar().setDisplayShowHomeEnabled(false);
		
		isShowAdsWhenFistClickListView = true;

		// Locate the Banner Ad in activity_main.xml
		adView = (AdView) this.findViewById(R.id.adView);
		adView.setVisibility(View.GONE);

		// Prepare the Interstitial Ad
		interstitial = new InterstitialAd(MainRentRoom.this);
		// Insert the Ad Unit ID
		interstitial.setAdUnitId(getString(R.string.ads_id));

		requestNewInterstitial();

		// Prepare an Interstitial Ad Listener
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				adView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAdClosed() {
				requestNewInterstitial();
				if(isShowAdsWhenFistClickListView){
					isShowAdsWhenFistClickListView = false;
					startDetailActivity();
				} else {
					startHomeScreen();
				}
			}

			@Override
			public void onAdLeftApplication() {
				super.onAdLeftApplication();
			}
		});

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

	private void requestNewInterstitial() {
		// Request for Ads
		AdRequest adRequest = new AdRequest.Builder()
		// Add a test device to show Test Ads
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("E5B4F6C9EBE17DB37112945C713374A3")
				.build();

		// Load ads into Banner Ads
		adView.loadAd(adRequest);

		// Load ads into Interstitial Ads
		interstitial.loadAd(adRequest);
	}

	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	private void setDataToSpinner() {
		if (MData.districtDTOs.size() > 0 && MData.cityDTOs.size() > 0) {
			spCity.setAdapter(UtilDroid.getAdapterCity(this));
			spDistrict.setAdapter(UtilDroid.getAdapterDistrictFromKey(MainRentRoom.this, MData.cityDTOs.get(0), districtDTOsHome));
			getListPost(false, 50);
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
				if(isShowAdsWhenFistClickListView){
					displayInterstitial();
				} else {
					startDetailActivity();
				}				
			}
		});
	}

	/**
	 * Start Detail Post Activity.
	 */
	private void startDetailActivity(){
		Intent i = new Intent(MainRentRoom.this, PostDetail.class);
		startActivity(i);
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
		default:
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
			isShowAdsWhenFistClickListView = false;
			displayInterstitial();
		}
		return true;
	}

	/**
	 * Start home when user click key Back.
	 */
	private void startHomeScreen() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
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
