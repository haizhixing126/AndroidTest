package com.liwuso.app.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liwuso.app.R;

public class CustomDialog extends DialogFragment {

	private String Messsage = "";

	public CustomDialog(String message) {
		this.Messsage = message;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyDialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog, container, false);
		TextView text = (TextView) v.findViewById(R.id.dialog_text);
		text.setText(this.Messsage);
		return v;

	}
}