package com.coolsx.rentroom;

import android.content.Intent;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.signin_title);

		tvSignUp = (TextView) findViewById(R.id.tv_registry);
		edUserName = (EditText) findViewById(R.id.edit_username_sign_in);
		edPass = (EditText) findViewById(R.id.edit_pass_sign_in);
		btnLogIn = (Button) findViewById(R.id.btn_sign_in);
		tvError = (TextView) findViewById(R.id.tv_error_login);
		checkBoxRemember = (CheckBox)findViewById(R.id.checkbox_remember);

		llProgress = (LinearLayout)findViewById(R.id.llProgressBar);
		llProgress.setVisibility(View.GONE);
		
		btnLogIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
								tvError.setText(e.getMessage());
								tvError.setVisibility(View.VISIBLE);
							}
						}
					});
				}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
}
