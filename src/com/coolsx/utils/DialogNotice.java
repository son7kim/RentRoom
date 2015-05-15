package com.coolsx.utils;

import android.app.AlertDialog;
import android.content.Context;

public class DialogNotice {

	Context _context;
	
	public DialogNotice(Context context){
		this._context = context;		
	}
	
	public void ShowDialog(String title, String content){
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage(content)
            .setTitle(title)
            .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
	}
}
