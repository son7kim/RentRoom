package com.coolsx.rentroom;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolsx.constants.MData;
import com.coolsx.dto.UserDTO;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.UtilDroid;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignInActivity extends BaseActivity {

	protected EditText edUserName;
	protected EditText edPass;
	protected CheckBox checkBoxRemember;
	protected TextView tvForgotPass;
	protected Button btnLogIn;
	protected TextView tvSignUp;
	protected TextView tvError;
	LinearLayout llProgress;
	DialogNotice notice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.signin_title);

		notice = new DialogNotice(SignInActivity.this);
		tvSignUp = (TextView) findViewById(R.id.tv_registry);
		edUserName = (EditText) findViewById(R.id.edit_username_sign_in);
		edPass = (EditText) findViewById(R.id.edit_pass_sign_in);
		btnLogIn = (Button) findViewById(R.id.btn_sign_in);
		tvError = (TextView) findViewById(R.id.tv_error_login);
		checkBoxRemember = (CheckBox)findViewById(R.id.checkbox_remember);

		llProgress = (LinearLayout)findViewById(R.id.llProgressBar);
		llProgress.setVisibility(View.GONE);
		
		edUserName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				tvError.setVisibility(View.GONE);				
			}
		});
		
		edPass.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				tvError.setVisibility(View.GONE);				
			}
		});
		
		btnLogIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UtilDroid.hideSoftKeyboard(SignInActivity.this);
				doLogIn();
			}
		});

		tvSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
				startActivity(i);
			}
		});
	}
	
	private void doLogIn(){
		tvError.setVisibility(View.GONE);

		final String username = edUserName.getText().toString().trim();
		final String password = edPass.getText().toString().trim();

		if (username.isEmpty() && password.isEmpty()) {
			tvError.setText(R.string.username_pass_required);
			tvError.setVisibility(View.VISIBLE);
		} else if (username.isEmpty()) {
			tvError.setText(R.string.username_required);
			tvError.setVisibility(View.VISIBLE);
		} else if (password.isEmpty()) {
			tvError.setText(R.string.pass_required);
			tvError.setVisibility(View.VISIBLE);
		} else {
			if (!UtilDroid.checkInternet()) {
				notice.ShowDialog(getResources().getString(R.string.notice_title), getResources().getString(R.string.internet_error));
				return;
			}
			llProgress.setVisibility(View.VISIBLE);
			ParseUser.logInInBackground(username, password, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException e) {
					llProgress.setVisibility(View.GONE);
					if (e == null) {
						MData.strPass = password;
						MData.userInfo = new UserDTO(user.getObjectId(), user.getUsername(), user.getEmail());
						if(checkBoxRemember.isChecked()){
							MData.mySharePrefs.setRememberMe(true);
							MData.mySharePrefs.setUserName(username);
							MData.mySharePrefs.setPassWord(password);
						}
						finish();
					} else {
						// Fail
						tvError.setText(getString(R.string.cannot_sign_in));
						tvError.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_ENTER){
			doLogIn();
		}
		return super.onKeyUp(keyCode, event);
	}
}
