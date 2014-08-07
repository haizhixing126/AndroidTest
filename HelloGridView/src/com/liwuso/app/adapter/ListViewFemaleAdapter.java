package com.liwuso.app.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.liwuso.app.R;
import com.pys.liwuso.bean.Person;

public class ListViewFemaleAdapter extends BaseAdapter {

	private Context context;
	private List<Person> listItems;
	private LayoutInflater listContainer;

	private int[] itemViewResourceArray = { R.anim.person_female_lisitem0,
			R.anim.person_female_lisitem1, R.anim.person_female_lisitem2,
			R.anim.person_female_lisitem3, R.anim.person_female_lisitem4, };

	public final static int SEXTYPE_FEMALE = 0X00;
	public final static int SEXTYPE_MALE = 0X01;

	static class ListItemView {
		public Button button;
	}

	public ListViewFemaleAdapter(Context context, List<Person> data) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.listItems = data;
	}

	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = null;

		if (convertView == null) {

			convertView = listContainer.inflate(itemViewResourceArray[position
					% itemViewResourceArray.length], null);

			listItemView = new ListItemView();
			listItemView.button = (Button) convertView
					.findViewById(R.id.btnPerson);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		Person person = listItems.get(position);

		listItemView.button.setText(person.Name);
		return convertView;
	}
}
