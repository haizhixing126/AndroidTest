package com.liwuso.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liwuso.app.R;

public class FragmentMoreInfo extends BaseFragment {

	public int item_index = 0;

	public FragmentMoreInfo(int index) {
		this.item_index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.setNavgationTitle(getResources().getStringArray(
				R.array.more_info_title)[item_index]);
		View rootView = inflater.inflate(R.layout.more_info, container, false);

		TextView myTextView = (TextView) rootView.findViewById(R.id.textView1);
		myTextView.setText(Html.fromHtml(getResources().getStringArray(
				R.array.more_info)[item_index]));

		// MoreActivity a = (MoreActivity) this.getActivity();
		// a.setActionBarText(getResources().getStringArray(
		// R.array.gift_more_about_title)[item_index]);

		return rootView;
	}

}