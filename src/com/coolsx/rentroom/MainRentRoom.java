package com.coolsx.rentroom;

import com.coolsx.constants.MData;
import com.coolsx.utils.UtilDroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainRentRoom extends Activity {

	Spinner spCity;
	Spinner spDistrict;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.rentroom_main);
		
		TextView tvSignIn = (TextView) findViewById(R.id.tvSignInHome);
		Button btnSignUp = (Button) findViewById(R.id.btnRegistryHome);
		spCity = (Spinner)findViewById(R.id.spinCity);
		spDistrict = (Spinner)findViewById(R.id.spinDistrict);
				
		spCity.setAdapter(UtilDroid.getAdapterCity(this));
		spCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				spDistrict.setAdapter(UtilDroid.getAdapterDistrictFromKey(MainRentRoom.this, MData.cityDTOs.get(position)));
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
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_rent_room, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
