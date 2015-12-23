package com.greeapp.WaitWorkOrder.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.greeapp.MainActivity;
import com.greeapp.R;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Entity.WaitAcceptOrder;
import com.greeapp.Entity.WaitWorkOrder;
import com.greeapp.Infrastructure.CWAssistant.RefreshableView;
import com.greeapp.Infrastructure.CWAssistant.RefreshableView.PullToRefreshListener;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.WaitWorkOrder.Assistant.WaitWorkOrderListAdapter;

public class WaitWorkOrderListFragment extends DataRequestFragment implements
		OnItemClickListener, OnClickListener {
	
	public static final int REQUEST_ITEM_DETAIL = 1;
	private Context mContext;
	private Class<? extends Activity> targetActivity;
	private ImageButton nav_bar_btn_left;
	private RefreshableView refreshableView;
	private ListView listview;
	private ArrayList<WaitWorkOrder> ListItemes;
	private int listViewId = R.layout.waitworkorder_item;
	private WaitWorkOrderListAdapter adapter = null;
	private String UserName;
	private String currentNotiName = "WaiteWorkOrderListNotifications";
	private LinearLayout empty_view;
	public void setTargetActivity(Class<? extends Activity> _targetActivity) {
		targetActivity = _targetActivity;
	}

	public void setListViewId(int listViewId) {
		this.listViewId = listViewId;
	}

	public void setListItemes(ArrayList<WaitWorkOrder> listItemes) {
		ListItemes = listItemes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage",
				null);
		if (list.size() > 0) {
			UserMessage userMessage = (UserMessage) list.get(0);
			UserName = userMessage.UserName;
		} else {
			UserName = "";
		}
		ListItemes = new ArrayList<WaitWorkOrder>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 用来设置 无数据时的空数据提示
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_waitworkorder_list,
				container, false);
		nav_bar_btn_left = (ImageButton) view
				.findViewById(R.id.wao_list_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		refreshableView = (RefreshableView) view
				.findViewById(R.id.refreshable_view);
		listview = (ListView) view.findViewById(R.id.lv_waitWorkOrder);
		empty_view = (LinearLayout) view.findViewById(R.id.empty_view);
		empty_view.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				showProgressDialog(getActivity(), "载入中，请稍候...");
				initData();	
			}
		});
		listview.setEmptyView(empty_view);
		if (adapter == null) {
			adapter = new WaitWorkOrderListAdapter(getActivity(), listViewId,
					ListItemes,myListener);
		}
		listview.setAdapter(adapter);
		listview.setOnItemClickListener((OnItemClickListener) this);
		showProgressDialog(getActivity(), "载入中，请稍候...");
		initData();
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
			}
		}, 2);

		return view;
	}
	private OnClickListener myListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final int position = (Integer) v.getTag();
			TextView tv_tel = (TextView) v.findViewById(R.id.tv_tel);
			if (v==tv_tel) {
				if(tv_tel.getText().toString().equals("")||tv_tel.getText().toString().equals("无")){
					DefaultTip(getActivity(), "此号码无效");
				}else{
					Intent _intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel.getText().toString()));
					startActivity(_intent);
				}	
			}		
		}
	};

	private void initData() {

		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "queryUnderProcWorkList");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName" };
		String value[] = { UserName };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName);
		setRequestUtility(myru);
		requestData();

	}

	public void updateView() {
		dismissProgressDialog();
		refreshableView.finishRefreshing();
		if (result != null) {
			if (CurrentAction == currentNotiName) {
				dataResult = dataDecode.decode(result,"WaitWorkOrder");
				if (dataResult != null) {
					DataResult realData = (DataResult) dataResult;
					if (realData.getResultcode().equals("1")) {
						ListItemes.clear();
						for (int i = 0; i < realData.getResult().size(); i++) {
							WaitWorkOrder orderInfo = new WaitWorkOrder();
							orderInfo = (WaitWorkOrder) realData.getResult().get(i);
							ListItemes.add(orderInfo);
						}
						setListItemes(ListItemes);
						adapter.notifyDataSetChanged();

					} 
				} else {
					DefaultTip(getActivity(),"网络数据获取失败");		
				}
			}
		} else {
			DefaultTip(getActivity(),"网络数据获取失败");		
		}
	}

	@Override
	public void onClick(View v) {
		if (v == nav_bar_btn_left) {
			// 加载动画
			Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			Intent intent=new Intent(mContext, MainActivity.class);
			getActivity().setResult(3, intent);
			getActivity().finish();	

		}
	}
	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		WaitWorkOrder listItem = ListItemes.get(position);
		Intent _intent = new Intent(getActivity(), targetActivity);
		_intent.putExtra("position", position);
		_intent.putExtra(WaitWorkOrderInfoFragment.EXTRA_LIST_ITEM_ID,
				listItem.WorkID);
		startActivityForResult(_intent, 1);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			Bundle b = data.getExtras();
			int p = b.getInt("position");
			ListItemes.remove(p);
			adapter.notifyDataSetChanged();
		}
		if (resultCode == 2) {
			showProgressDialog(getActivity(), "更新中，请稍候...");
			initData();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ListItemes.clear();
	}
	

}
