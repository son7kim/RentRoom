package com.coolsx.utils;

import com.coolsx.utils.MInterfaceNotice.onOkLoadData;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogNotice{

	Context _context;
	public onOkLoadData loadDataDelegate;
	
	public DialogNotice(Context context, onOkLoadData event){
		this._context = context;
		this.loadDataDelegate = event;
	}
	
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
	
	public void ShowDialogRequestData(String title, String content){
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage(content)
            .setTitle(title)
            .setPositiveButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					loadDataDelegate.onOkClick();
				}
			});
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
	}
}
