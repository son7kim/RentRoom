package com.coolsx.rentroom;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coolsx.constants.MConstants;
import com.coolsx.dto.PostArticleDTO;

public class ListPostAdapter extends ArrayAdapter<PostArticleDTO> {
	Context _context;
	List<PostArticleDTO> _listPostArticle;
	
	public ListPostAdapter(Context context, List<PostArticleDTO> listPost){
		super(context, 0, listPost);
		this._context = context;
		this._listPostArticle = listPost;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.my_page_adapter, null);
		
		TextView tvDescription = (TextView)view.findViewById(R.id.tv_description_adapter);
		TextView tvCost = (TextView)view.findViewById(R.id.tv_cost_adapter);
		TextView tvAddress = (TextView)view.findViewById(R.id.tv_address_adapter);
		
		tvDescription.setText(_listPostArticle.get(position).getDescription());
		tvCost.setText("Gia: " + _listPostArticle.get(position).getLong(MConstants.kCostMin));
		tvAddress.setText(_listPostArticle.get(position).getAddress());
		
		return view;
	}
}
