package com.coolsx.rentroom;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolsx.dto.ImageDTO;

public class OnAddFileImage extends LinearLayout {

	Context _context;
	public OnAddFileImage(Context context) {
		super(context);
		this._context = context;
		
		this.setOrientation(VERTICAL);		
	}

	public void InitView(List<ImageDTO> listImgs){
		this.removeAllViews();
		int size = listImgs.size();
		for(int i = 0; i < size; i ++){
			LinearLayout ll = new LinearLayout(_context);
			ll.setOrientation(HORIZONTAL);
			
			ImageView imgFile = new ImageView(_context);
			imgFile.setImageResource(R.drawable.ic_back);
			
			TextView tvFile = new TextView(_context);
			tvFile.setTextColor(Color.BLUE);
			tvFile.setText(listImgs.get(i).getFileName());
			tvFile.setId(i);
			tvFile.setTypeface(null, Typeface.BOLD);
			tvFile.setSingleLine(true);
			tvFile.setEllipsize(TruncateAt.MARQUEE);
			
			ImageView imgDelete = new ImageView(_context);
			imgDelete.setImageResource(R.drawable.abc_ic_clear);
			imgDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
			
			ll.addView(imgFile);
			ll.addView(tvFile);
			ll.addView(imgDelete);
			
			this.addView(ll);
		}
	}
}
