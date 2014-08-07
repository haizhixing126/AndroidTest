package com.liwuso.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liwuso.app.AppContext;
import com.liwuso.app.AppException;
import com.liwuso.app.R;
import com.liwuso.app.adapter.ListViewMaleAdapter;
import com.liwuso.app.adapter.ListViewNewsAdapter;
import com.liwuso.app.adapter.ListViewFemaleAdapter;
import com.liwuso.app.common.UIHelper;
import com.liwuso.widget.PullToRefreshListView;
import com.pys.liwuso.bean.NewsList;
import com.pys.liwuso.bean.Notice;
import com.pys.liwuso.bean.Person;
import com.pys.liwuso.bean.PersonList;
import com.pys.liwuso.bean.Product;

public class Main extends BaseActivity {

	private ProgressBar mHeadProgress;
	private ProgressBar lvFemale_foot_progress;
	private ProgressBar lvMale_foot_progress;	

	private int curNewsCatalog = NewsList.CATALOG_ALL;

	private PullToRefreshListView lvFemale;
	private PullToRefreshListView lvMale;

	private Handler lvFemaleHandler;
	private Handler lvMaleHandler;

	private ListViewFemaleAdapter lvFemaleAdapter;
	private ListViewMaleAdapter lvMaleAdapter;

	private List<Person> lvFemaleData = new ArrayList<Person>();

	private int lvFemaleSumData;
	
	private TextView lvFemale_foot_more;

	private AppContext appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appContext = (AppContext) getApplication();

		this.initHeadView();
		this.initFrameListView();
	}

	private void initHeadView() {
		mHeadProgress = (ProgressBar) findViewById(R.id.main_head_progress);
		// TODO: lvPerson_foot_progress = (ProgressBar)
		// findViewById(R.id.main_head_progress);
	}

	private void initFrameListView() {

		this.initPersonListView();

		this.initFrameListViewData();
	}

	private void initFrameListViewData() {
		lvFemaleHandler = this.getLvHandler(lvFemale, lvFemaleAdapter,
				lvFemale_foot_more, lvFemale_foot_progress,
				AppContext.PAGE_SIZE);

		// Load Person data
		if (lvFemaleData.isEmpty()) {
			loadLvPersonData(curNewsCatalog, 0, lvFemaleHandler,
					UIHelper.LISTVIEW_ACTION_INIT);
		}
	}

	private void loadLvPersonData(final int catalog, final int pageIndex,
			final Handler handler, final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					PersonList list = appContext.getPersonList(catalog,
							pageIndex, isRefresh);
					msg.what = list.getPageSize();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_PERSON;
				if (curNewsCatalog == catalog)
					handler.sendMessage(msg);
			}
		}.start();
	}

	private void initPersonListView() {
		lvFemaleAdapter = new ListViewFemaleAdapter(this, lvFemaleData);
		lvFemale = (PullToRefreshListView) findViewById(R.id.frame_listview_person_female);
		lvFemale.setAdapter(lvFemaleAdapter);

		lvFemale.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO:
			}
		});
		lvFemale.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvFemale.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvFemaleData.isEmpty())
					return;

				// 判断是否滚动到底部
				int pageIndex = 1;
				loadLvPersonData(curNewsCatalog, pageIndex, lvFemaleHandler,
						UIHelper.LISTVIEW_ACTION_SCROLL);
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvFemale.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvFemale.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				loadLvPersonData(curNewsCatalog, 0, lvFemaleHandler,
						UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
	}

	private Handler getLvHandler(final PullToRefreshListView lv,
			final BaseAdapter adapter, final TextView more,
			final ProgressBar progress, final int pageSize) {
		return new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what >= 0) {
					// listview数据处理
					Notice notice = handleLvData(msg.what, msg.obj, msg.arg2,
							msg.arg1);

					if (msg.what < pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						// more.setText(R.string.load_full);
					} else if (msg.what == pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						// more.setText(R.string.load_more);
					}

				} else if (msg.what == -1) {
					// 有异常--显示加载出错 & 弹出错误消息
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					((AppException) msg.obj).makeToast(Main.this);
				}
				if (adapter.getCount() == 0) {
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					// more.setText(R.string.load_empty);
				}
				// progress.setVisibility(ProgressBar.GONE);
				mHeadProgress.setVisibility(ProgressBar.GONE);
				if (msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH) {
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update)
							+ new Date().toLocaleString());
					lv.setSelection(0);
				} else if (msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}
		};
	}

	private Notice handleLvData(int what, Object obj, int objtype,
			int actiontype) {
		Notice notice = null;
		switch (actiontype) {
		case UIHelper.LISTVIEW_ACTION_INIT:
		case UIHelper.LISTVIEW_ACTION_REFRESH:
		case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
			int newdata = 0;// 新加载数据-只有刷新动作才会使用到
			switch (objtype) {
			case UIHelper.LISTVIEW_DATATYPE_PERSON:
				PersonList plist = (PersonList) obj;
				notice = plist.getNotice();
				lvFemaleSumData = what;
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
					if (lvFemaleData.size() > 0) {
						for (Person person1 : plist.getPersonList()) {
							boolean b = false;
							for (Person person2 : lvFemaleData) {
								if (person1.getId() == person2.getId()) {
									b = true;
									break;
								}
							}
							if (!b)
								newdata++;
						}
					} else {
						newdata = what;
					}
				}
				lvFemaleData.clear();
				lvFemaleData.addAll(plist.getPersonList());
				break;

			case UIHelper.LISTVIEW_ACTION_SCROLL:
				switch (objtype) {
				case UIHelper.LISTVIEW_DATATYPE_PERSON:
					PersonList list = (PersonList) obj;
					notice = list.getNotice();
					lvFemaleSumData += what;
					if (lvFemaleData.size() > 0) {
						for (Person person1 : list.getPersonList()) {
							boolean b = false;
							for (Person person2 : lvFemaleData) {
								if (person1.getId() == person2.getId()) {
									b = true;
									break;
								}
							}
							if (!b)
								lvFemaleData.add(person1);
						}
					} else {
						lvFemaleData.addAll(list.getPersonList());
					}
					break;
				}
				break;
			}
		}
		return notice;
	}

	public void clickBar(View view) {
		int viewId = view.getId();
		switch (viewId) {
		case R.id.bottom_btn0:
			clickFragment(new FragmentPerson(), 0);
			break;
		case R.id.bottom_btn1:
			clickFragment(new FragmentSearch(), 1);
			break;
		case R.id.bottom_btn2:
			clickFragment(new FavoriteFrament(), 2);
			break;
		case R.id.bottom_btn3:
			clickFragment(new FragmentMore(), 3);
			break;
		}
	}

	public void searchPerson(View view) {
		relaceFragment(new FragmentAge());
	}

	public void searchAge(View view) {
		relaceFragment(new FragmentPurpose());
	}

	public void searchPurpose(View view) {
		relaceFragment(new FragmentProduct());
	}

	public void clickMore(View view) {
		switch (view.getId()) {
		case R.id.btnAbout:
			relaceFragment(new FragmentMoreInfo(0));
			break;
		case R.id.btnQuestion:
			relaceFragment(new FragmentMoreInfo(1));
			break;
		case R.id.btnAgreement:
			relaceFragment(new FragmentMoreInfo(2));
			break;
		case R.id.btnContact:
			relaceFragment(new FragmentMoreInfo(3));
			break;
		case R.id.btnAdvice:
			relaceFragment(new FragmentAdvice());
			break;
		case R.id.btnCheckVertion:
			relaceFragment(new FragmentVersion());
			break;
		}
	}

	public void checkVersion(View view) {
		MyDialog m = new MyDialog();
		// m.show(getSupportFragmentManager(), "");
	}

	private void relaceFragment(Fragment newFragment) {
		//
		// FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		// transaction.replace(R.id.container, newFragment);
		// transaction.addToBackStack(null);
		// transaction.commit();
	}

	private void clickFragment(Fragment newFragment, int iconIndex) {
		for (int i = 0; i < 4; i++) {
			// if (i == iconIndex)
			// menu.getItem(i).setIcon(pressedIcons[i]);
			// else
			// menu.getItem(i).setIcon(unPressedIcons[i]);
		}
		relaceFragment(newFragment);
	}

	public void setTitle(String title) {
		TextView titleView = (TextView) this.findViewById(R.id.navbar_title);
		titleView.setText(title);
	}

}
