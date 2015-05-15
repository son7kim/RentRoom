package com.coolsx.rentroom;

import com.coolsx.utils.UtilDroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends Activity {

	private EditText edUsername;
	private EditText edPass;
	private EditText edConfirmPass;
	private EditText edEmail;
	private EditText edConfirmEmail;
	private Button btnSignUp;
	private TextView tvErrorSignUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sign_up);

		edUsername = (EditText) findViewById(R.id.edit_username_sign_up);
		edPass = (EditText) findViewById(R.id.edit_pass_sign_up);
		edConfirmPass = (EditText) findViewById(R.id.edit_pass_confirm_sign_up);
		edEmail = (EditText) findViewById(R.id.edit_email_sign_up);
		edConfirmEmail = (EditText) findViewById(R.id.edit_confirm_email_sign_up);
		btnSignUp = (Button) findViewById(R.id.btn_sign_up);
		tvErrorSignUp = (TextView) findViewById(R.id.tv_error_sign_up);

		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvErrorSignUp.setVisibility(View.GONE);
				if (edUsername.getText().toString().trim().length() > 5) {
					if (UtilDroid.isValidUsername_Pass(edUsername.getText().toString().trim())) {
						if (edPass.getText().toString().trim().length() > 5) {
							if (UtilDroid.isValidUsername_Pass(edPass.getText().toString().trim())) {
								if(edPass.getText().toString().trim().equals(edConfirmPass.getText().toString().trim())){
									if(UtilDroid.isValidEmail(edEmail.getText().toString().trim())){
										if(edEmail.getText().toString().trim().equals(edConfirmEmail.getText().toString().trim())){
											// Registry
										} else {
											tvErrorSignUp.setVisibility(View.VISIBLE);
											tvErrorSignUp.setText(R.string.invalid_confirm_email);
										}
									} else {
										tvErrorSignUp.setVisibility(View.VISIBLE);
										tvErrorSignUp.setText(R.string.invalid_email);
									}
								} else {
									tvErrorSignUp.setVisibility(View.VISIBLE);
									tvErrorSignUp.setText(R.string.invalid_confirm_pass);
								}
							} else {
								tvErrorSignUp.setVisibility(View.VISIBLE);
								tvErrorSignUp.setText(R.string.invalid_pass);
							}
						} else {
							tvErrorSignUp.setVisibility(View.VISIBLE);
							tvErrorSignUp.setText(R.string.invalid_lenght_pass);
						}
					} else {
						tvErrorSignUp.setVisibility(View.VISIBLE);
						tvErrorSignUp.setText(R.string.invalid_username);
					}
				} else {
					tvErrorSignUp.setVisibility(View.VISIBLE);
					tvErrorSignUp.setText(R.string.invalid_lenght_username);
				}
			}
		});
	}
}
