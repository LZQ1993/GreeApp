package com.greeapp.WaitAcceptOrder.Fragment;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.greeapp.MainActivity;
import com.greeapp.R;
import com.greeapp.Entity.ReturnTransactionMessage;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Entity.WaitAcceptOrder;
import com.greeapp.Infrastructure.CWAssistant.RefreshableView;
import com.greeapp.Infrastructure.CWAssistant.RefreshableView.PullToRefreshListener;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.WaitAcceptOrder.Assistant.WaitAcceptOrderListAdapter;

public class WaitAcceptOrderListFragment extends DataRequestFragment implements
		OnItemClickListener, OnClickListener {
	public static final int REQUEST_ITEM_DETAIL = 1;
	private Context mContext;
	private Class<? extends Activity> targetActivity;
	private ImageButton nav_bar_btn_left;
	private RefreshableView refreshableView;
	private ListView listview;
	private ArrayList<WaitAcceptOrder> ListItemes;
	private int listViewId = R.layout.waitacceptorder_item;
	private WaitAcceptOrderListAdapter adapter = null;
	private String UserName;
	private String currentNotiName = "WaitedWorkListNotifications";
	private String currentNotiName1 = "AcceptOrderNotifications";
	private String currentNotiName2 = "RefuseOrderNotifications";
	private int deleteNum;
	private LinearLayout empty_view;

	public void setTargetActivity(Class<? extends Activity> _targetActivity) {
		targetActivity = _targetActivity;
	}

	public void setListViewId(int listViewId) {
		this.listViewId = listViewId;
	}

	public void setListItemes(ArrayList<WaitAcceptOrder> listItemes) {
		ListItemes = listItemes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
		Notifications.add(currentNotiName1);
		Notifications.add(currentNotiName2);
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage",
				null);
		if (list.size() > 0) {
			UserMessage userMessage = (UserMessage) list.get(0);
			UserName = userMessage.UserName;
		} else {
			UserName = "";
		}
		ListItemes = new ArrayList<WaitAcceptOrder>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 用来设置 无数据时的空数据提示
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_waitacceptorder_list,
				container, false);
		nav_bar_btn_left = (ImageButton) view
				.findViewById(R.id.wao_list_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		listview = (ListView) view.findViewById(R.id.lv_waitAcceptOrder);
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
			adapter = new WaitAcceptOrderListAdapter(getActivity(), listViewId,
					ListItemes, myListener);		
		}
		listview.setAdapter(adapter);
		listview.setOnItemClickListener((OnItemClickListener)this);
		showProgressDialog(getActivity(), "载入中...");
		initData();
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {  
                    Thread.sleep(3000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }
				initData();
			}
		}, 1);

		return view;
	}

	private void initData() {

		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "queryWaitedWorkList");
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
				dataResult = dataDecode.decode(result,"WaitAcceptOrder");
				if (dataResult != null) {
					DataResult realData = (DataResult) dataResult;
					if (realData.getResultcode().equals("1")) {
						ListItemes.clear();
						for (int i = 0; i < realData.getResult().size(); i++) {
							WaitAcceptOrder orderInfo = new WaitAcceptOrder();
							orderInfo = (WaitAcceptOrder) realData.getResult()
									.get(i);
							ListItemes.add(orderInfo);
						}
						setListItemes(ListItemes);
						adapter.notifyDataSetChanged();

					}
				} else {
					DefaultTip(getActivity(),"网络数据获取失败");		
				}
			} else if (CurrentAction == currentNotiName1) {
				dataResult = dataDecode
						.decode(result,"ReturnTransactionMessage");
				if (dataResult != null) {
					DataResult realData1 = (DataResult) dataResult;
					if (realData1.getResultcode().equals("1")) {
						ReturnTransactionMessage msg = (ReturnTransactionMessage) realData1
								.getResult().get(0);
						if (msg.getResult().equals("1")) {

							new AlertDialog.Builder(getActivity())
									.setTitle("提示")
									.setMessage(msg.tip)
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 表中数据删除
													ListItemes.remove(deleteNum);
													adapter.notifyDataSetChanged();
													deleteNum = -1;
													return;
												}
											}).setCancelable(false).show();

						} else {
							DefaultTip(getActivity(),msg.tip);		
						}
					} else {
						DefaultTip(getActivity(),"暂无数据");	
					}
				} else {
					DefaultTip(getActivity(),"网络数据获取失败");		
				}
			} else if (CurrentAction == currentNotiName2) {
				dataResult = dataDecode.decode(result,"ReturnTransactionMessage");
				if (dataResult != null) {
					DataResult realData1 = (DataResult) dataResult;
					if (realData1.getResultcode().equals("1")) {
						ReturnTransactionMessage msg = (ReturnTransactionMessage) realData1
								.getResult().get(0);
						if (msg.getResult().equals("1")) {

							new AlertDialog.Builder(getActivity())
									.setTitle("提示")
									.setMessage(msg.tip)
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 表中数据删除
													ListItemes.remove(deleteNum);
													adapter.notifyDataSetChanged();
													deleteNum = -1;
													return;
												}
											}).setCancelable(false).show();

						} else {
							DefaultTip(getActivity(),msg.tip);		
						}
					} else {
						DefaultTip(getActivity(),"暂无数据");	
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
			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			Intent intent=new Intent(mContext, MainActivity.class);
			getActivity().setResult(2, intent);
			getActivity().finish();	

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		WaitAcceptOrder listItem = ListItemes.get(position);
		Intent _intent = new Intent(getActivity(), targetActivity);
		_intent.putExtra("position", position);
		_intent.putExtra(WaitAcceptOrderInfoFragment.EXTRA_LIST_ITEM_ID,listItem.WorkID);
		startActivityForResult(_intent, 1);

	}
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==1){
			Bundle b=data.getExtras();
			int p =b.getInt("position");
			ListItemes.remove(p);
			adapter.notifyDataSetChanged();
		}
	}



	private OnClickListener myListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final int position = (Integer) v.getTag();
			final WaitAcceptOrder listItem = ListItemes.get(position);
			TextView tv_tel = (TextView) v.findViewById(R.id.tv_tel);
			if (v.getId() == R.id.btn_AcceptOrder) {
				new AlertDialog.Builder(getActivity())
						.setTitle("提示")
						.setMessage("是否确认接单？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										deleteNum = position;
										acceptOrderNetWork(listItem.WorkID);
									}

								}).setNegativeButton("取消", null)
						.setCancelable(false).show();
				return;
			}
			if (v.getId() == R.id.btn_NoAcceptOrder) {
				deleteNum = position;
				showBecauseInputDialog(listItem.WorkID);

			}	
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

	private void showBecauseInputDialog(final String workId) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_noaccept_because_input, null);
		final EditText edtBecause = (EditText) view
				.findViewById(R.id.dialog_noaccept_because);
		// 对话框
		new AlertDialog.Builder(getActivity()).setView(view).setTitle("提示")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (edtBecause.getText().toString().equals("")) { // 判断是否为空
							showDormNubmerNotNullDialog(workId);
						} else {
							refuseOrderNetWork(workId, edtBecause.getText()
									.toString());
						}
					}
				}).setNegativeButton("取消", null).setCancelable(false) // 触摸不消失
				.show();
		return;
	}

	/**
	 * 显示不为空对话框
	 */
	private void showDormNubmerNotNullDialog(final String workId) {
		new AlertDialog.Builder(getActivity()).setTitle("提示")
				.setMessage("拒单理由不能为空")
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showBecauseInputDialog(workId); // 重新调用寝室输入对话框
					}
				}).setCancelable(false)// 触摸不消失
				.show();
	}

	private void acceptOrderNetWork(String workID) {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "ReceiveWorkOrder");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName", "WorkID" };
		String value[] = { UserName, workID };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName1);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "正在提交，请稍候...");
	}

	private void refuseOrderNetWork(String workID, String becauseContent) {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "RejectWorkOrder");
		Map requestCondition = new HashMap();
		String condition[] = { "UserName", "WorkID", "Reason" };
		String value[] = { UserName, workID, becauseContent };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName2);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "正在提交，请稍候...");
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
