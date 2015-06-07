package com.coolsx.rentroom;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolsx.constants.MConstants;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.UtilDroid;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends BaseActivity {

	private EditText edUsername;
	private EditText edPass;
	private EditText edConfirmPass;
	private EditText edEmail;
	private EditText edConfirmEmail;
	private Button btnSignUp;
	private TextView tvErrorSignUp;
	private DialogNotice dialog;
	private LinearLayout llProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sign_up);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.sign_up_title);

		edUsername = (EditText) findViewById(R.id.edit_username_sign_up);
		edPass = (EditText) findViewById(R.id.edit_pass_sign_up);
		edConfirmPass = (EditText) findViewById(R.id.edit_pass_confirm_sign_up);
		edEmail = (EditText) findViewById(R.id.edit_email_sign_up);
		edConfirmEmail = (EditText) findViewById(R.id.edit_confirm_email_sign_up);
		btnSignUp = (Button) findViewById(R.id.btn_sign_up);
		tvErrorSignUp = (TextView) findViewById(R.id.tv_error_sign_up);

		llProgress = (LinearLayout) findViewById(R.id.llProgressBar);
		llProgress.setVisibility(View.GONE);

		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvErrorSignUp.setVisibility(View.GONE);

				if (edUsername.getText().toString().trim().length() <= 5) {
					tvErrorSignUp.setVisibility(View.VISIBLE);
					tvErrorSignUp.setText(R.string.invalid_lenght_username);
					return;
				}

				if (!UtilDroid.isValidUsername_Pass(edUsername.getText().toString().trim())) {
					tvErrorSignUp.setVisibility(View.VISIBLE);
					tvErrorSignUp.setText(R.string.invalid_username);
					return;
				}

				if (edPass.getText().toString().trim().length() <= 5) {
					tvErrorSignUp.setVisibility(View.VISIBLE);
					tvErrorSignUp.setText(R.string.invalid_lenght_pass);
					return;
				}

				if (!UtilDroid.isValidUsername_Pass(edPass.getText().toString().trim())) {
					tvErrorSignUp.setVisibility(View.VISIBLE);
					tvErrorSignUp.setText(R.string.invalid_pass);
					return;
				}

				if (!edPass.getText().toString().trim().equals(edConfirmPass.getText().toString().trim())) {
					tvErrorSignUp.setVisibility(View.VISIBLE);
					tvErrorSignUp.setText(R.string.invalid_confirm_pass);
					return;
				}

				if (!UtilDroid.isValidEmail(edEmail.getText().toString().trim())) {
					tvErrorSignUp.setVisibility(View.VISIBLE);
					tvErrorSignUp.setText(R.string.invalid_email);
					return;
				}

				if (!edEmail.getText().toString().trim().equals(edConfirmEmail.getText().toString().trim())) {
					tvErrorSignUp.setVisibility(View.VISIBLE);
					tvErrorSignUp.setText(R.string.invalid_confirm_email);
					return;
				}

				// Registry
				llProgress.setVisibility(View.VISIBLE);
				ParseUser newUser = new ParseUser();
				newUser.put(MConstants.kUsername, edUsername.getText().toString().trim());
				newUser.put(MConstants.kPassword, edPass.getText().toString().trim());
				newUser.put(MConstants.kEmail, edEmail.getText().toString().trim());
				newUser.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException ex) {
						llProgress.setVisibility(View.GONE);
						if (ex == null) {
							finish();
						} else {
							if (dialog == null) {
								dialog = new DialogNotice(SignUpActivity.this);
							}
							dialog.ShowDialog(getResources().getString(R.string.sign_up_title), getResources().getString(R.string.sign_up_not_success));
						}
					}
				});
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
}
