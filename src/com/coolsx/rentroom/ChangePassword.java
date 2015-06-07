package com.coolsx.rentroom;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolsx.constants.MData;
import com.coolsx.utils.DialogNotice;
import com.coolsx.utils.UtilDroid;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ChangePassword extends BaseActivity {

	private EditText edOldPass;
	private EditText edNewPass;
	private EditText edConfirmNewPass;
	private TextView tvError;
	private Button btnChange;
	private DialogNotice dialog;
	private LinearLayout llProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.change_pass);

		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.change_pass_title);

		dialog = new DialogNotice(this);
		llProgress = (LinearLayout) findViewById(R.id.llProgressBar);
		llProgress.setVisibility(View.GONE);

		edOldPass = (EditText) findViewById(R.id.edit_old_pass);
		edNewPass = (EditText) findViewById(R.id.edit_new_pass);
		edConfirmNewPass = (EditText) findViewById(R.id.edit_new_pass_confirm);
		tvError = (TextView) findViewById(R.id.tv_error_change_pass);
		btnChange = (Button) findViewById(R.id.btn_change_pass);

		setActionToView();
	}

	private void setActionToView() {
		btnChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvError.setVisibility(View.GONE);
				if (!MData.strPass.equals(edOldPass.getText().toString().trim())) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.invalid_old_pass);
					return;
				}
				
				if (edNewPass.getText().toString().trim().length() <= 5) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.invalid_lenght_pass);
					return;
				}
				
				if (!UtilDroid.isValidUsername_Pass(edNewPass.getText().toString().trim())) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.invalid_new_pass);
					return;
				}

				if (!edNewPass.getText().toString().trim().equals(edConfirmNewPass.getText().toString().trim())) {
					tvError.setVisibility(View.VISIBLE);
					tvError.setText(R.string.invalid_confirm_pass);
					return;
				}

				llProgress.setVisibility(View.VISIBLE);
				
				ParseUser currentUser = ParseUser.getCurrentUser();
				currentUser.setPassword(edNewPass.getText().toString().trim());
				currentUser.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						llProgress.setVisibility(View.GONE);
						if (e == null) {
							MData.strPass = edNewPass.getText().toString().trim();
							dialog.ShowDialog(getResources().getString(R.string.change_pass_title), getResources().getString(R.string.request_success));
						} else {
							dialog.ShowDialog(getResources().getString(R.string.change_pass_title), getResources().getString(R.string.request_fail));
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
