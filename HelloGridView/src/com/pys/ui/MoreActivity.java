package com.pys.ui;

import com.pys.hellogridview.R;
import com.pys.utility.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MoreActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.SetActionBar(this, getString(R.string.gift_actionbar_more));
		setContentView(R.layout.activity_more);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new MoreHomeFragment()).commit();
		}
	}

	public void navigateFragment(View view) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		MoreHomeAboutFragment newFragment = new MoreHomeAboutFragment();
		switch (view.getId()) {
		case R.id.button0:
			newFragment.item_index = 0;
			break;
		case R.id.button1:
			newFragment.item_index = 1;
			break;
		case R.id.button2:
			newFragment.item_index = 2;
			break;
		case R.id.button4:
			newFragment.item_index = 3;
			break;
		default:
			newFragment.item_index = 0;
			break;
		}

		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

	public void openAdviceFragment(View view) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		MoreAdviceFragment newFragment = new MoreAdviceFragment();
		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();

	}

	public void sendAdvice(View view) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		MoreAdviceFragment newFragment = new MoreAdviceFragment();
		transaction.replace(R.id.container, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void setActionBarText(String title) {
		ActionBar actionBar = getSupportActionBar();
		Utils.SetActionBar(this, title);
	}

	public static class MoreHomeFragment extends Fragment {

		public MoreHomeFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_more, container,
					false);
			return rootView;
		}

	}

	public static class MoreHomeAboutFragment extends Fragment {

		public int item_index;

		public MoreHomeAboutFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_more_about,
					container, false);
			TextView myTextView = (TextView) rootView
					.findViewById(R.id.textView1);
			myTextView.setText(Html.fromHtml(getResources().getStringArray(
					R.array.gift_more_about)[item_index]));

			MoreActivity a = (MoreActivity) this.getActivity();
			a.setActionBarText(getResources().getStringArray(
					R.array.gift_more_about_title)[item_index]);

			return rootView;
		}

	}

	public static class MoreAdviceFragment extends Fragment {

		public MoreAdviceFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_more_advice,
					container, false);
			MoreActivity activity = (MoreActivity) this.getActivity();
			activity.setActionBarText("�������");

			return rootView;
		}
	}

}