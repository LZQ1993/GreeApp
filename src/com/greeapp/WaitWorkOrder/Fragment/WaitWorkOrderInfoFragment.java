package com.greeapp.WaitWorkOrder.Fragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
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
import com.greeapp.WaitWorkOrder.Activity.DataCollectionMainActivity;
import com.greeapp.WaitWorkOrder.Activity.WaitWorkOrderListActivity;

public class WaitWorkOrderInfoFragment extends DataRequestFragment implements
		OnClickListener,AMapLocationListener{
	public static final String EXTRA_LIST_ITEM_ID = "com.chinawit.android.listitemId";
	private Context mContext;
	private String currentNotiName = "QueryWaitWorkOrderInfoNotifications";
	private String currentNotiName1 = "LocationNotifications";
	private String currentNotiName2 = "ChangeSignNotifications";
	private String currentNotiName3 = "FinishedtWorkNotifications";
	private ImageButton nav_bar_btn_left;
	Button btn_ready, btn_changeSign, btn_dataAccept, btn_finished;
	private String WorkId;
	private int position;
	private String UserName;
	private Boolean IsLocation = false, IsDataAccept = false;
	TextView tv_workId, tv_orderType, tv_appointTime, tv_workTime, tv_remark,
	tv_clientName, tv_tel1, tv_tel2, tv_installAddress, tv_SaleNum,
	tv_buyTime, tv_unit, tv_productType, tv_productNum;
	private String locationTime="",locationSpot="";
	private OrderDetail intent_orderDetail = new OrderDetail();
	//����AMapLocationClient�����
	public AMapLocationClient mLocationClient = null;
	//����mLocationOption����
	public AMapLocationClientOption mLocationOption = null;
	
	public static WaitWorkOrderInfoFragment newInstance(String WorkId,
			int position) {
		Bundle args = new Bundle();
		args.putString(EXTRA_LIST_ITEM_ID, WorkId);
		args.putInt("position", position);
		WaitWorkOrderInfoFragment _fragment = new WaitWorkOrderInfoFragment();
		_fragment.setArguments(args);
		return _fragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		WorkId = getActivity().getIntent().getStringExtra(
				WaitWorkOrderInfoFragment.EXTRA_LIST_ITEM_ID);
		position = getActivity().getIntent().getIntExtra("position", 0);
		Notifications.add(currentNotiName);
		Notifications.add(currentNotiName1);
		Notifications.add(currentNotiName2);
		Notifications.add(currentNotiName3);
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage",
				null);
		if (list.size() > 0) {
			UserMessage userMessage = (UserMessage) list.get(0);
			UserName = userMessage.UserName;
		} else {
			UserName = "";
		}	
		//��ʼ����λ
		mLocationClient = new AMapLocationClient(mContext);
		mLocationClient.setLocationListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_waitworkorderinfo,container, false);
		nav_bar_btn_left = (ImageButton) view.findViewById(R.id.foinfo_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		btn_ready = (Button) view.findViewById(R.id.btn_ready);
		btn_changeSign = (Button) view.findViewById(R.id.btn_changeSign);
		btn_dataAccept = (Button) view.findViewById(R.id.btn_dataAccept);
		btn_finished = (Button) view.findViewById(R.id.btn_finished);
		btn_changeSign.setOnClickListener(this);
		btn_dataAccept.setOnClickListener(this);
		btn_finished.setOnClickListener(this);
		btn_dataAccept.setClickable(false);
		btn_dataAccept.setBackgroundResource(R.drawable.btn_noclickable_shape);
		btn_dataAccept.setTextColor(0XFF3497FF);
		btn_finished.setClickable(false);
		btn_finished.setBackgroundResource(R.drawable.btn_noclickable_shape);
		btn_finished.setTextColor(0XFF3497FF);
		btn_ready.setOnClickListener(this);
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
		showProgressDialog(getActivity(), "�����У����Ժ�...");
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
							intent_orderDetail = od;
							Message message = new Message();
							message.obj = od;
							handler.sendMessage(message);
						}else{
							DefaultTip(getActivity(),"��������");	
						 }
				}else{
					DefaultTip(getActivity(),"�������ݻ�ȡʧ��");	
			    }  
			} else {
				dataResult = dataDecode.decode(result,"ReturnTransactionMessage");
				if (dataResult != null) {
					DataResult realData1 = (DataResult) dataResult;
					if (realData1.getResultcode().equals("1")) {
						ReturnTransactionMessage msg = (ReturnTransactionMessage) realData1.getResult().get(0);
						if (msg.getResult().equals("1")) {
							 if (CurrentAction == currentNotiName1) {
								 new AlertDialog.Builder(getActivity())
						  			.setTitle("��ʾ")
						  			.setMessage(msg.tip)
						  			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {											
											IsLocation = true;
											btn_dataAccept.setClickable(true);
											btn_dataAccept.setBackgroundResource(R.drawable.uc_btn_shape);
											btn_dataAccept.setTextColor(0Xffffffff);
										}
									}).setCancelable(false).show();
								    return;
							}else if (CurrentAction == currentNotiName2){
						            	new AlertDialog.Builder(getActivity())
							  			.setTitle("��ʾ")
							  			.setMessage(msg.tip)
							  			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,int which) {
												ISqlHelper iSqlHelper = new SqliteHelper(null,mContext);
												iSqlHelper.SQLExec("delete from LocalCollectData");// ɾ������ԭ�е����ݣ���ֻ֤��һ��
												Intent intent = new Intent(mContext,WaitWorkOrderListActivity.class);
												getActivity().setResult(2,intent);
												getActivity().finish();
											}
										}).setCancelable(false).show();
									    return;					        
							           }else if (CurrentAction == currentNotiName3) {
								  			new AlertDialog.Builder(getActivity())
								  			.setTitle("��ʾ")
								  			.setMessage(msg.tip)
								  			.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,int which) {
													ISqlHelper iSqlHelper = new SqliteHelper(null,mContext);
													iSqlHelper.SQLExec("delete from LocalCollectData");// ɾ������ԭ�е����ݣ���ֻ֤��һ��
													// ��������ɾ��
													Bundle bundle = new Bundle();
													bundle.putInt("position",position);
													Intent intent = new Intent(mContext,WaitWorkOrderListActivity.class);
													intent.putExtras(bundle);
													getActivity().setResult(1,intent);
													getActivity().finish();
												}
											}).setCancelable(false).show();
							               }      
						}else{
							DefaultTip(getActivity(),msg.tip);		
						}
					} else {
						 DefaultTip(getActivity(),"��������");	
					}
				} else {
					DefaultTip(getActivity(),"�������ݻ�ȡʧ��");			
				}
			}
		} else {
			DefaultTip(getActivity(),"�������ݻ�ȡʧ��");			
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
		if (v == nav_bar_btn_left) {
			// ���ض���
			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			ISqlHelper iSqlHelper = new SqliteHelper(null,mContext);
			iSqlHelper.SQLExec("delete from LocalCollectData");// ɾ������ԭ�е����ݣ���ֻ֤��һ��
			Intent intent = new Intent(mContext,
					WaitWorkOrderListActivity.class);
			getActivity().setResult(0, intent);
			getActivity().finish();
		}
		if (v == btn_ready) {
			if(tv_workId.getText().toString().equals("")||tv_workId.getText().toString().equals("��")){
			  DefaultTip(getActivity(), "�˹�������Ч");
		      }else{
				 Location();
		      }
		}
		if (v == btn_changeSign) {
			if(tv_workId.getText().toString().equals("")||tv_workId.getText().toString().equals("��")){
				  DefaultTip(getActivity(), "�˹�������Ч");
			      }else{
				  showChangeSignInputDialog();
			    }
		}
		if (v == btn_dataAccept) {	
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("OrderDetailInfo",intent_orderDetail);
			intent.putExtra("LocationTime", locationTime);
			intent.putExtra("LocationSpot", locationSpot);
			intent.putExtras(bundle);
			intent.setClass(getActivity(), DataCollectionMainActivity.class);
			startActivityForResult(intent, 10);		
		}
		if (v == btn_finished) {
			if(tv_workId.getText().toString().equals("")||tv_workId.getText().toString().equals("��")){
				  DefaultTip(getActivity(), "�˹�������Ч");
			      }else{
						isFinishedWork();
			    }
		}
		if(v==tv_tel1){
        	if(tv_tel1.getText().toString().equals("")||tv_tel1.getText().toString().equals("��")){
				DefaultTip(getActivity(), "�˺�����Ч");
			}else{
				Intent _intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel1.getText().toString()));
				startActivity(_intent);
			}	
	    }
       if(v==tv_tel2){
			if(tv_tel2.getText().toString().equals("")||tv_tel2.getText().toString().equals("��")){
				DefaultTip(getActivity(), "�˺�����Ч");
			}else{
				Intent _intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tv_tel2.getText().toString()));
				startActivity(_intent);
			}	
	   }
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==10){
			IsDataAccept = true;
			btn_finished.setClickable(true);
			btn_finished.setBackgroundResource(R.drawable.uc_btn_shape);
			btn_finished.setTextColor(0Xffffffff);
		}
		
	}

	private void isFinishedWork() {
		if (!IsLocation) {
			new AlertDialog.Builder(getActivity())
					.setTitle("��ʾ")
					.setMessage("����δ��ɶ�λ����������ж�λ����")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).setCancelable(false).show();
		} else if (!IsDataAccept) {
			new AlertDialog.Builder(getActivity())
					.setTitle("��ʾ")
					.setMessage("����δ������ݲɼ���������������ݲɼ���������")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).setCancelable(false).show();
		} else {
			showFinishedWormRemarkInputDialog();
		}

	}

	private void showFinishedWormRemarkInputDialog() {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_finishedwork_remark_input, null);
		final EditText edtRemark = (EditText) view
				.findViewById(R.id.dialog_finishedRemark);
		// �Ի���
		new AlertDialog.Builder(getActivity()).setView(view).setTitle("��ʾ")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						FinishedWorkNetWork(edtRemark.getText().toString());
					}

				}).setNegativeButton("ȡ��", null).setCancelable(false) // ��������ʧ
				.show();
		return;
	}

	private void FinishedWorkNetWork(String remark) {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "FinishWorkOrder");
		Map requestCondition = new HashMap();
		String condition[] = { "WorkID", "UserName", "Remark" };
		String value[] = { WorkId, UserName, remark };
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName3);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "�����ύ�У����Ժ�...");
	}
	
	private void showChangeSignInputDialog() {
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_changesign_input, null);
		final EditText edtAppointDate = (EditText) view.findViewById(R.id.dialog_appointDate);
		final EditText edtAppointTime = (EditText) view.findViewById(R.id.dialog_appointTime);
		edtAppointDate.setInputType(InputType.TYPE_NULL);
		edtAppointTime.setInputType(InputType.TYPE_NULL);
		final EditText edtReason = (EditText) view.findViewById(R.id.dialog_reason);
		edtAppointDate.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
								int month = (monthOfYear + 1);
								String strdate = year + "-";
								if (month < 10) {
									strdate = strdate + "0" + month + "-";
								} else {
									strdate = strdate + month + "-";
								}
								if (dayOfMonth < 10) {
									strdate = strdate + "0" + dayOfMonth + " ";
								} else {
									strdate = strdate + dayOfMonth + " ";
								}

								edtAppointDate.setText(strdate);
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
			}		
		});
		edtAppointTime.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {	
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						
						edtAppointTime.setText(hourOfDay+":"+minute+":00");
						
					}
				},c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
			}		
		});
		new AlertDialog.Builder(getActivity()).setView(view).setTitle("��ʾ")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(edtAppointTime.getText().toString().equals("")||
								edtReason.getText().toString().equals("")){
							showChangeSignNotNullDialog();
						}else{
							String time = edtAppointDate.getText().toString()+" "+edtAppointTime.getText().toString();
							ChangeSignNetWork(time,edtReason.getText().toString());
						}	
					}		
				}).setNegativeButton("ȡ��", null).setCancelable(false) // ��������ʧ
				.show();
		return;
	}
	/**
	 * ��ʾ��Ϊ�նԻ���
	 */
	private void showChangeSignNotNullDialog() {
		new AlertDialog.Builder(getActivity()).setTitle("��ʾ")
				.setMessage("��������Ϣ��д����")
				.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showChangeSignInputDialog(); 
					}
				}).setCancelable(false)// ��������ʧ
				.show();
		        return;
	}
	private void ChangeSignNetWork(String time, String reason) {
		
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "ModifyAppoint");
		Map requestCondition = new HashMap();
		String condition[] = { "WorkID", "UserName", "AppointTime","Reason"};
		String value[] = { WorkId,UserName,time,reason};
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName2);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "�����ύ�У����Ժ�...");
		
	}

	private void Location() {
		//��ʼ����λ����
		mLocationOption = new AMapLocationClientOption();
		//���ö�λģʽΪ�߾���ģʽHight_Accuracy��Battery_SavingΪ�͹���ģʽ��Device_Sensors�ǽ��豸ģʽ
		// ͨ��GPS���Ƕ�λ����λ������Ծ�ȷ���֣�ͨ��24�����Ƕ�λ��������Ϳտ��ĵط���λ׼ȷ���ٶȿ죩  
		 LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
        // ͨ��WLAN���ƶ�����(3G/2G)ȷ����λ�ã�Ҳ����AGPS������GPS��λ����Ҫ���������ڻ��ڸ������Ⱥ��ï�ܵ����ֵȣ��ܼ��ĵط���λ��  
        ConnectivityManager nw = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = nw.getActiveNetworkInfo();
        if(netinfo!=null){
	        if(gps&&netinfo.isAvailable()){
	        	 mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);	
	        }else if(gps==false&&netinfo.isAvailable()==true){
	        	 mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
	        }
        }else{
        	DefaultTip(getActivity(),"������������ڽ��ж�λ");
        }       
		//�����Ƿ񷵻ص�ַ��Ϣ��Ĭ�Ϸ��ص�ַ��Ϣ��
		mLocationOption.setNeedAddress(true);
		//�����Ƿ�ֻ��λһ��,Ĭ��Ϊfalse
		mLocationOption.setOnceLocation(false);
		//�����Ƿ�ǿ��ˢ��WIFI��Ĭ��Ϊǿ��ˢ��
		mLocationOption.setWifiActiveScan(true);
		//�����Ƿ�����ģ��λ��,Ĭ��Ϊfalse��������ģ��λ��
		mLocationOption.setMockEnable(false);
		//���ö�λ���,��λ����,Ĭ��Ϊ2000ms
		mLocationOption.setInterval(-1);
		//����λ�ͻ��˶������ö�λ����
		mLocationClient.setLocationOption(mLocationOption);
		//������λ
		mLocationClient.startLocation();	
	}
 
 
	private void LocationInfoNetWork(double _latitude,double _longtitude,String _currentSpot) {
		String latitude = _latitude+"";
		String longtitude = _longtitude+"";
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "ArriveSpot");
		Map requestCondition = new HashMap();
		String condition[] = { "WorkID", "UserName", "Latitude","Longtitude","CurrentSpot"};
		String value[] = { WorkId,UserName,latitude,longtitude,_currentSpot};
		String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		myru.setParams(requestCondition);
		myru.setNotification(currentNotiName1);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(), "�����ύ�У����Ժ�...");

	}
	public void onLocationChanged(AMapLocation amapLocation) {
	    if (amapLocation != null) {
	        if (amapLocation.getErrorCode() == 0) {
	        //��λ�ɹ��ص���Ϣ�����������Ϣ
	      //  amapLocation.getLocationType();//��ȡ��ǰ��λ�����Դ�������綨λ����������λ���ͱ�
	      //  amapLocation.getLatitude();//��ȡ����
	      //  amapLocation.getLongitude();//��ȡγ��
	       // amapLocation.getAccuracy();//��ȡ������Ϣ
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = new Date(amapLocation.getTime());
	        df.format(date);//��λʱ��
	       /* amapLocation.getAddress();//��ַ�����option������isNeedAddressΪfalse����û�д˽��
	        amapLocation.getCountry();//������Ϣ
	        amapLocation.getProvince();//ʡ��Ϣ
	        amapLocation.getCity();//������Ϣ
	        amapLocation.getDistrict();//������Ϣ
	        amapLocation.getRoad();//�ֵ���Ϣ
	        amapLocation.getCityCode();//���б���
	        amapLocation.getAdCode();//��������
	       */
	        
	        mLocationClient.stopLocation();//ֹͣ��λ
	        locationTime= df.format(date);
	        if(amapLocation.getAddress()==null||amapLocation.getAddress().equals("")){
	        	 locationSpot="δ�ܶ�λ��ǰλ��";
	        	showLocationDialog(amapLocation.getLatitude(),amapLocation.getLongitude(),"δ�ܶ�λ��ǰλ��");      	
	        }else{
                locationSpot=amapLocation.getAddress();
	        	showLocationDialog(amapLocation.getLatitude(),amapLocation.getLongitude(),amapLocation.getAddress());  	
	        }
	        
	    } else {
	        //��ʾ������ϢErrCode�Ǵ����룬errInfo�Ǵ�����Ϣ������������
	        Log.e("AmapError","location Error, ErrCode:"
	            + amapLocation.getErrorCode() + ", errInfo:"
	            + amapLocation.getErrorInfo());
	        }
	    }
	}

	private void showLocationDialog(final double Latitude, final double Longtitude, final String CurrentSpot) {
		new AlertDialog.Builder(getActivity())
		   .setTitle("��ʾ")
		   .setMessage("("+Latitude+","+Longtitude+")"+CurrentSpot)
		  .setPositiveButton("�ύ", new DialogInterface.OnClickListener() {
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
						LocationInfoNetWork(Latitude,Longtitude,CurrentSpot);
					}

				}).setNegativeButton("ȡ��", null).setCancelable(false) // ��������ʧ
				.show();
		   return;
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	
		mLocationClient.onDestroy();//���ٶ�λ�ͻ���
	}
}
