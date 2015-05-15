package com.coolsx.rentroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.coolsx.utils.DialogNotice;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SignInActivity extends Activity{
	
	protected EditText edUserName;
    protected EditText edPass;
    protected CheckBox checkBoxRemember;
    protected TextView tvForgotPass;
    protected Button btnLogIn; 
    protected TextView tvSignUp;    
    protected TextView tvError;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.sign_in);
		
		tvSignUp = (TextView)findViewById(R.id.tv_registry);
        edUserName = (EditText)findViewById(R.id.edit_username_sign_in);
        edPass = (EditText)findViewById(R.id.edit_pass_sign_in);
        btnLogIn = (Button)findViewById(R.id.btn_sign_in);
        tvError = (TextView)findViewById(R.id.tv_error_login);
        
       // btnLogIn.setEnabled(false);
        
        btnLogIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = edUserName.getText().toString().trim();
                String password = edPass.getText().toString().trim();
 
                if (username.isEmpty() && password.isEmpty()) {
                    tvError.setText(R.string.username_pass_required);
                    tvError.setVisibility(View.VISIBLE);
                } else if(username.isEmpty()){
                	tvError.setText(R.string.username_required);
                    tvError.setVisibility(View.VISIBLE);
                } else if(password.isEmpty()){
                	tvError.setText(R.string.pass_required);
                    tvError.setVisibility(View.VISIBLE);
                }
                else {
                    setProgressBarIndeterminateVisibility(true); 
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            setProgressBarIndeterminateVisibility(false); 
                            if (e == null) {
                                // Success!
                            }
                            else {
                                // Fail
                            	DialogNotice dialog = new DialogNotice(SignInActivity.this);
                            	dialog.ShowDialog("Title", "Content");
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
}
