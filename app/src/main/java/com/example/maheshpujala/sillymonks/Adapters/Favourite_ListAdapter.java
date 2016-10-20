package com.example.maheshpujala.sillymonks.Adapters;


import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maheshpujala.sillymonks.Activities.FavouritesFragment;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.EnhancedListView;
import com.example.maheshpujala.sillymonks.Utils.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Favourite_ListAdapter extends BaseAdapter{
	private Context mContext;
	EnhancedListView favouritesListView;
	LinkedHashMap favouritesMap;
	FavouritesFragment favouritesFragment;
	public Favourite_ListAdapter(Context mContext, LinkedHashMap favouritesMap, FavouritesFragment favouritesFragment, EnhancedListView favouritesListView) {
		this.mContext=mContext;
		this.favouritesMap =favouritesMap;
		this.favouritesFragment =favouritesFragment;
		this.favouritesListView= favouritesListView;
	}

	@Override
	public int getCount() {
		return favouritesMap.size();
	}

	@Override
	public Object getItem(int position) {
		return favouritesMap.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public void remove(int position) {
		try {
			favouritesMap.remove(position);
			notifyDataSetChanged();
		} catch (Exception e) {}
	}

	public void insert(int position, String item) {
		try {
			favouritesMap.put(position, item);
			notifyDataSetChanged();
		} catch (Exception e) {}
	}

	public class ViewHolder {
		TextView favouriteText,favDescText;
		ImageView favIcon;
		SquareImageView favouriteImage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.favourite_item, null);
			holder.favouriteImage = (SquareImageView) convertView.findViewById(R.id.favourite_image);
			holder.favouriteText = (TextView) convertView.findViewById(R.id.favourite_text);
			holder.favDescText = (TextView) convertView.findViewById(R.id.fav_description);
			holder.favIcon = (ImageView) convertView.findViewById(R.id.favourite_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		List<Article> articles= new ArrayList<Article>(favouritesMap.values()) ;
		Log.e("articles---from map",""+articles);

		Picasso.with(mContext).load(articles.get(position).getThumbImage()).into(holder.favouriteImage);
		holder.favouriteText.setText(articles.get(position).getTitle());
		holder.favDescText.setText(Html.fromHtml(articles.get(position).getDescription()));

		Log.e("articles.get(position).getTitle()",""+articles.get(position).getTitle());

		return convertView;

	}
}
