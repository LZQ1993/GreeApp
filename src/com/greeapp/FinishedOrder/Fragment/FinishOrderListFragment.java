package com.greeapp.FinishedOrder.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.greeapp.R;
import com.greeapp.Entity.FinishedOrder;
import com.greeapp.FinishedOrder.Assistant.FinishedOrderListAdapter;
import com.greeapp.Infrastructure.CWAssistant.RefreshableView;
import com.greeapp.Infrastructure.CWAssistant.RefreshableView.PullToRefreshListener;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;

public class FinishOrderListFragment extends DataRequestFragment implements OnItemClickListener,OnClickListener {

	public static final int REQUEST_ITEM_DETAIL=1;
	private Context mContext;
	private Class<? extends Activity> targetActivity;
	private ImageButton nav_bar_btn_left;
	private RefreshableView refreshableView;
	private ListView listview;
	private ArrayList<FinishedOrder> ListItemes;
	private int listViewId=R.layout.finishedorderlist_item;
	private FinishedOrderListAdapter adapter=null;
	private String currentNotiName = "QueryFinishedOrderNotifications1";
	private LinearLayout empty_view;
	
	public void setTargetActivity(Class<? extends Activity> _targetActivity) {
		targetActivity = _targetActivity;
	}
	
	
	public void setListViewId(int listViewId) {
		this.listViewId = listViewId;
	}
	public void setListItemes(ArrayList<FinishedOrder> listItemes) {
		ListItemes = listItemes;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//用来设置 无数据时的空数据提示
		super.onCreateView(inflater, container,savedInstanceState);
		View view =inflater.inflate(R.layout.fragment_finishedorder_list,container, false);
		nav_bar_btn_left = (ImageButton) view.findViewById(R.id.fo_list_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		listview = (ListView)view.findViewById(R.id.lv_finishedorder);
		empty_view = (LinearLayout) view.findViewById(R.id.empty_view);
		empty_view.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				showProgressDialog(getActivity(), "载入中，请稍候...");
				initData();	
			}
		});
		listview.setEmptyView(empty_view);
		if(adapter==null){
			adapter=new FinishedOrderListAdapter(getActivity(),listViewId,ListItemes,myListener);
		}
		listview.setAdapter(adapter);
		 refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			   @Override
			   public void onRefresh() {
				   initData();
			   }
			  }, 0);
		listview.setOnItemClickListener(this);
		return view;
	}

	private void initData() {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "queryFinishedWorkList");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName", "WorkType", "WorkID", "ClientName",
				"ClientTel", "ClientAddress", "StartTime", "EndTime" };
		String value[] = {getActivity().getIntent().getStringExtra("UserName"),
				getActivity().getIntent().getStringExtra("WorkType"),
				getActivity().getIntent().getStringExtra("WorkID"),
				getActivity().getIntent().getStringExtra("ClientName"),
				getActivity().getIntent().getStringExtra("ClientTel"),
				getActivity().getIntent().getStringExtra("ClientAddress"),
				getActivity().getIntent().getStringExtra("StartTime"),
				getActivity().getIntent().getStringExtra("EndTime")};
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
				dataResult=dataDecode.decode(result,"FinishedOrder");
				if (dataResult != null) {
					DataResult realData = (DataResult) dataResult;
					if (realData.getResultcode().equals("1")) {
						ListItemes.clear();
					      for(int i=0;i<realData.getResult().size();i++){
					    	  FinishedOrder orderInfo = new FinishedOrder();
					    	  orderInfo = (FinishedOrder) realData.getResult().get(i);
					    	  ListItemes.add(orderInfo);
					      }
					      adapter.notifyDataSetChanged();
					     
					}
				}else{
					DefaultTip(getActivity(),"网络数据获取失败");		
				}
			}
		}else{
			DefaultTip(getActivity(),"网络数据获取失败");		
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


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FinishedOrder listItem=ListItemes.get(position);		
		Intent _intent=new Intent(getActivity(),targetActivity);
		_intent.putExtra(FinishedOrderInfoFragment.EXTRA_LIST_ITEM_ID, listItem.WorkID);
		startActivity(_intent);
		
	}


	@Override
	public void onClick(View v) {
		if(v==nav_bar_btn_left){
			// 加载动画
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			getActivity().finish();
			
		}
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
}