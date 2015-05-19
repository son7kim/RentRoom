package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.coolsx.dto.CityDTO;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

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
		
		tvSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		
		getListCity();
	}

	List<CityDTO> cityDTOs= new ArrayList<CityDTO>();
	
	private void getListCity() {
		 
	    ParseQuery<CityDTO> query = CityDTO.getQuery();
	 
	    query.findInBackground(new FindCallback<CityDTO>() {	
			@Override
	        public void done(List<CityDTO> citys, ParseException e) {
	            if (e == null) {	               
	               cityDTOs.addAll(citys);
	               setToSpinner();
	            } else {
	                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
	            }
	        }
	    });
	}
	
	private void setToSpinner(){
		try {
			for(CityDTO cityDTO : cityDTOs){
				String title = cityDTO.getCityName();
				String sID = cityDTO.getCityID();
				System.out.println(title);			
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
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
