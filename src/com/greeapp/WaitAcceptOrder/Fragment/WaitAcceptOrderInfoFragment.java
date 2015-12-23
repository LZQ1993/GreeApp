package com.greeapp.WaitAcceptOrder.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.greeapp.R;
import com.greeapp.Entity.OrderDetail;
import com.greeapp.Entity.ProductList;
import com.greeapp.Entity.ReturnTransactionMessage;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.WaitAcceptOrder.Activity.WaitAcceptOrderListActivity;

public class WaitAcceptOrderInfoFragment extends DataRequestFragment implements
		OnClickListener {
	public static final String EXTRA_LIST_ITEM_ID = "com.chinawit.android.listitemId";
	private Context mContext;
	private String currentNotiName="QueryWaitAcceptOrderInfoNotifications";
	private String currentNotiName1 = "Info_AcceptOrderNotifications";
	private String currentNotiName2 = "Info_RefuseOrderNotifications";
	private ImageButton nav_bar_btn_left;
	Button btn_AcceptOrder,btn_NoAcceptOrder;
	private String WorkId;
	private int position;
	private String UserName;
	TextView tv_workId, tv_orderType, tv_appointTime, tv_workTime, tv_remark,
	tv_clientName, tv_tel1, tv_tel2, tv_installAddress, tv_SaleNum,
	tv_buyTime, tv_unit, tv_productType, tv_productNum;
	public static WaitAcceptOrderInfoFragment newInstance(String WorkId,int position) {
		Bundle args = new Bundle();
		args.putString(EXTRA_LIST_ITEM_ID, WorkId);
		args.putInt("position", position);
		WaitAcceptOrderInfoFragment _fragment = new WaitAcceptOrderInfoFragment();
		_fragment.setArguments(args);
		return _fragment;

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		WorkId = getActivity().getIntent().getStringExtra(WaitAcceptOrderInfoFragment.EXTRA_LIST_ITEM_ID);
		position = getActivity().getIntent().getIntExtra("position", 0);	
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
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_waitacceptorderinfo, container, false);
		nav_bar_btn_left = (ImageButton) view.findViewById(R.id.foinfo_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		btn_AcceptOrder = (Button) view.findViewById(R.id.btn_AcceptOrder);
		btn_NoAcceptOrder = (Button) view.findViewById(R.id.btn_NoAcceptOrder);
		btn_AcceptOrder.setOnClickListener(this);
		btn_NoAcceptOrder.setOnClickListener(this);
		tv_workId = (TextView) view.findViewById(R.id.tv_workId);
        tv_orderType = (TextView) view.findViewById(R.id.tv_orderType);
        tv_appointTime = (TextView) view.findViewById(R.id.tv_appointTime);
        tv_workTime = (TextView) view.findViewById(R.id.tv_workTime);
        tv_remark = (TextView) view.findViewById(R.id.tv_remark);
        tv_clientName = (TextView) view.findViewById(R.id.tv_clientName);
        tv_tel1 = (TextView) view.findViewById(R.id.tv_tel1);
        tv_tel1.setOnClickListener(this);
        tv_tel2 = (TextView) view.findViewById(R.id.tv_tel2);
        tv_tel2.setOnClickListener(this);
        tv_installAddress = (TextView) view.findViewById(R.id.tv_installAddress);
        tv_SaleNum = (TextView) view.findViewById(R.id.tv_SaleNum);
        tv_buyTime = (TextView) view.findViewById(R.id.tv_buyTime);
        tv_unit = (TextView) view.findViewById(R.id.tv_unit);
        tv_productType = (TextView) view.findViewById(R.id.tv_productType);
        tv_productNum = (TextView) view.findViewById(R.id.tv_productNum);
		
		initData();
		return view;
	}
	private void initData() {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "queryWorkDetail");
		Map requestCondition = new HashMap();
		String condition[] = { "WorkID" };
		String value[] = { WorkId };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "载入中，请稍候...");
	}
	
	public void updateView() {
		dismissProgressDialog();
		if (result != null) {
			if (CurrentAction == currentNotiName) {
				dataResult=dataDecode.decode(result,"OrderDetail");
				if (dataResult != null) {
					DataResult realData = (DataResult) dataResult;
						if (realData.getResultcode().equals("1")) {
							OrderDetail od = (OrderDetail)realData.getResult().get(0);						
							Message message = new Message();
							message.obj = od;
							handler.sendMessage(message);
						}else{
							DefaultTip(getActivity(),"暂无数据");	
						 }
				}else{
					DefaultTip(getActivity(),"网络数据获取失败");	
			    }         
			} else if (CurrentAction == currentNotiName1) {
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
									.setPositiveButton("确定",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 表中数据删除
													Bundle bundle=new Bundle();
													bundle.putInt("position", position); 													
													Intent intent=new Intent(mContext, WaitAcceptOrderListActivity.class);
													intent.putExtras(bundle);
													getActivity().setResult(1, intent);
													getActivity().finish();	
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
									.setPositiveButton("确定",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 表中数据删除
													Bundle bundle=new Bundle();
													bundle.putInt("position", position); 													
													Intent intent=new Intent(mContext, WaitAcceptOrderListActivity.class);
													intent.putExtras(bundle);
													getActivity().setResult(1, intent);
													getActivity().finish();	
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
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			OrderDetail orderDetial = (OrderDetail) msg.obj;
			tv_workId.setText(orderDetial.WorkID);
			tv_orderType.setText(orderDetial.WorkType);
			tv_appointTime.setText(orderDetial.AppointTime);
			tv_workTime.setText(orderDetial.AssignTime);
			tv_remark.setText(orderDetial.Remark);
			tv_clientName.setText(orderDetial.ClientName);
			tv_tel1.setText(orderDetial.ClientTel1);
			tv_tel2.setText(orderDetial.ClientTel2);
			tv_installAddress.setText(orderDetial.InstallAddress);
			tv_SaleNum.setText(orderDetial.SaleOrder);
			tv_buyTime.setText(orderDetial.BuyTime);
			tv_unit.setText(orderDetial.SaleDepartment);
			String str_type = "";
			String str_num  = "";
			for(int i = 0;i<orderDetial.ProductList.size();i++){
				ProductList pl = new ProductList();
				pl=(ProductList) orderDetial.ProductList.get(i);
				if(i==orderDetial.ProductList.size()-1){
					str_type =  str_type+pl.ProductModel;
					str_num =  str_num + pl.ProductNumber;
				}else{
					str_type = str_type + pl.ProductModel+"\n";
					str_num =  str_num + pl.ProductNumber+"\n";
				}
			}
			tv_productType.setText(str_type);
			tv_productNum.setText(str_num);
		
		}
	};
	@Override
	public void onClick(View v) {
		if (v==btn_AcceptOrder) {
			new AlertDialog.Builder(getActivity())
					.setTitle("提示")
					.setMessage("是否确认接单？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									acceptOrderNetWork(WorkId);
								}

							}).setNegativeButton("取消", null)
					.setCancelable(false).show();
			return;
		}
		if (v.getId() == R.id.btn_NoAcceptOrder) {
			showBecauseInputDialog(WorkId);

		}		
		if(v==nav_bar_btn_left){
			// 加载动画
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			Intent intent=new Intent(mContext, WaitAcceptOrderListActivity.class);
			getActivity().setResult(0, intent);
			getActivity().finish();	
		}
		if(v==tv_tel1){
	        	if(tv_tel1.getText().toString().equals("")||tv_tel1.getText().toString().equals("无")){
					DefaultTip(getActivity(), "此号码无效");
				}else{
					Intent _intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel1.getText().toString()));
					startActivity(_intent);
				}	
		}
	    if(v==tv_tel2){
				if(tv_tel2.getText().toString().equals("")||tv_tel2.getText().toString().equals("无")){
					DefaultTip(getActivity(), "此号码无效");
				}else{
					Intent _intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel2.getText().toString()));
					startActivity(_intent);
				}	
		}
	}
	
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
				.setMessage("请您填写拒单理由")
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
}
