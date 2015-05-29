package com.coolsx.rentroom;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolsx.dto.ImageDTO;
import com.coolsx.utils.MInterfaceNotice.onDeleteFileNotify;

public class OnAddFileImage extends LinearLayout {

	Context _context;
	List<ImageDTO> _listImgs;

	public onDeleteFileNotify deleteFileNotify;

	public OnAddFileImage(Context context, onDeleteFileNotify event) {
		super(context);
		this._context = context;
		this.deleteFileNotify = event;

		this.setOrientation(VERTICAL);
		this.setVerticalGravity(Gravity.CENTER_VERTICAL);
	}

	public void InitView(final List<ImageDTO> listImgs, boolean isLocal) {
		_listImgs = new ArrayList<ImageDTO>();
		this._listImgs.addAll(listImgs);
		this.removeAllViews();
		int size = listImgs.size();
		for (int i = 0; i < size; i++) {
			LinearLayout ll = new LinearLayout(_context);
			ll.setOrientation(HORIZONTAL);
			ll.setVerticalGravity(Gravity.CENTER_VERTICAL);

			ImageView imgFile = new ImageView(_context);
			imgFile.setImageResource(R.drawable.ic_attach);

			TextView tvFile = new TextView(_context);
			tvFile.setTextColor(Color.BLUE);
			if (isLocal) {
				tvFile.setText(listImgs.get(i).getFileNameLocal());
			} else {
				tvFile.setText(listImgs.get(i).getFileNameServer());
				//tvFile.setText(listImgs.get(i).getFile().getUrl());
			}
			tvFile.setId(i);
			tvFile.setTypeface(null, Typeface.BOLD);
			tvFile.setSingleLine(true);
			tvFile.setEllipsize(TruncateAt.MARQUEE);
			tvFile.setPadding(5, 0, 15, 0);

			final ImageButton imgDelete = new ImageButton(_context);
			imgDelete.setId(i);
			imgDelete.setImageResource(R.drawable.ic_delete);
			imgDelete.setMinimumWidth(60);
			imgDelete.setBackgroundColor(Color.TRANSPARENT);
			imgDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					_listImgs.remove(imgDelete.getId());
					deleteFileNotify.onDeleteNotify(_listImgs);
				}
			});

			ll.addView(imgFile);
			ll.addView(tvFile);
			ll.addView(imgDelete);
			tvFile.setPadding(0, 15, 0, 15);

			this.addView(ll);
		}
	}
}
