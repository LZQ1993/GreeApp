package com.greeapp.Infrastructure.CWFragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.greeapp.R;
import com.greeapp.Infrastructure.CWBroadcastReceiver.ResultBroadcastReceiver;
import com.greeapp.Infrastructure.CWDataDecoder.DataDecode;
import com.greeapp.Infrastructure.CWDataDecoder.IDataDecode;
import com.greeapp.Infrastructure.CWDataRequest.FileAndPictureNetRequest;
import com.greeapp.Infrastructure.CWDataRequest.IRequest;
import com.greeapp.Infrastructure.CWDataRequest.LocalRequest;
import com.greeapp.Infrastructure.CWDataRequest.NetRequest;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWDomain.GlobalVariables;


public class DataRequestFragment extends Fragment {
	
    public IDataDecode dataDecode=null;	
    public Object dataResult = null;
    
    private Context cxt;
    public  String result = null;
    private LocalBroadcastManager broadcastFragment;
	private boolean hasregister=false; //�Ƿ��Ѿ�ע��
	private RequestUtility requestUtility=null;
	//private LinearLayout loadingContainer;
	public List<String> Notifications=new ArrayList<String>();
	public  String CurrentAction="";
    Dialog progressDialog;
	public void setFragmentLayout(int fragmentLayout) {
		this.fragmentLayout = fragmentLayout;
	}

	private int fragmentLayout=R.layout.sample_fragment_test;
	
	
	private ResultBroadcastReceiver mydatareceive = new ResultBroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);	 
		}

		@Override
		public void dataReload(Context context, Intent intent) {
			  String action = intent.getAction();
			  if(action.equals(GlobalVariables.ACTION_DATA_RELOAD_ERROR)){
				  errorDataReload(context,intent);
			  }else{
					result = intent.getStringExtra(GlobalVariables.DATA_RESULT);
					//loadingContainer.setVisibility(View.INVISIBLE);
					CurrentAction=action;
					updateView();
			  }
		}
		private void errorDataReload(Context context, Intent intent) {
			errorTip(context,intent);
		}
	};
	
	public void DefaultTip(Context context,String tip){
		new AlertDialog.Builder(context)
		.setTitle("��ʾ")
		.setMessage(tip)
		.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						return;
					}
				}).setCancelable(false).show();
		return;
	}
	
	//��д����������ڵ����ݷ��ʴ���ʱ��ʾ����ʾ 
	public void errorTip(Context context, Intent intent){
/*		Toast.makeText(context, "��Ϣ����Ϊ:"+intent.getStringExtra(GlobalVariables.DATA_RESULT),
				Toast.LENGTH_SHORT).show();*/
		dismissProgressDialog();
		Toast.makeText(context, "��~���뱣�����糩ͨ",
				Toast.LENGTH_SHORT).show();
		
	}
	
	//��д����������ڸ�����ͼ�ϵ����ݣ� ��ʾ�� 
	public void updateView() {
	}
	
	@Override
	public void onPause() {
		super.onPause();
		removeBroadcast();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void registeBroadcast() {
		//ע����չ㲥  
		if(!hasregister){
			for(String notiAction :Notifications){
				IntentFilter filter=new IntentFilter(notiAction); 
				broadcastFragment.registerReceiver(mydatareceive, filter);
			}
	        hasregister=true;
		}
	}
	private void removeBroadcast() {
		if(hasregister){ 
			broadcastFragment.unregisterReceiver(mydatareceive);
	        hasregister=false;
		}
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cxt=getActivity();
		if(dataDecode==null){
			dataDecode=new DataDecode();
			dataResult = new Object();
		}
		broadcastFragment=LocalBroadcastManager.getInstance(cxt);
		Notifications.add(GlobalVariables.ACTION_DATA_RELOAD_OK);
		Notifications.add(GlobalVariables.ACTION_DATA_RELOAD_ERROR);
	}

	
/*    public  boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			removeBroadcast();
			loadingContainer.setVisibility(View.INVISIBLE);
		}
		return false;
    }*/
	
	public void setDataDecoder(IDataDecode _dataDecode)//,Object _dataResult)
	{
		dataDecode = _dataDecode;
	}
	
	public void setRequestUtility(RequestUtility _requestUtility){
		requestUtility=_requestUtility;	
	}
	
	public void requestData(){
		registeBroadcast();	
		IRequest mdataRequest=null;
		if (GlobalVariables.ISLOCALDATA) {
			mdataRequest = new LocalRequest(cxt);
		} else {
			mdataRequest = new NetRequest(cxt);
			//mdataRequest = new FileAndPictureNetRequest(cxt);
		}
		//loadingContainer.setVisibility(View.VISIBLE);
		
		mdataRequest.requestData(requestUtility);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(fragmentLayout, container, false);
		/*FrameLayout waitLayout=new FrameLayout(getActivity());
		FrameLayout.LayoutParams param=new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);//���岼�ֹ������Ĳ���
		waitLayout.setLayoutParams(param);
		
		waitLayout.addView(view);
		
	    loadingContainer=new LinearLayout(getActivity());
		LinearLayout.LayoutParams param_loadingContainer=new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);//���岼�ֹ������Ĳ���
		loadingContainer.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		loadingContainer.setVerticalGravity(Gravity.CENTER_VERTICAL);
		loadingContainer.setLayoutParams(param_loadingContainer);
		
		loadingContainer.setOnClickListener(new OnClickListener(){		
			@Override
			public void onClick(View v) {
				removeBroadcast();
				loadingContainer.setVisibility(View.INVISIBLE);				
			}
		});
		
		LinearLayout.LayoutParams param_loadingtxt=new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);//���岼�ֹ������Ĳ���
		TextView loadingTv=new TextView(getActivity());
		loadingTv.setText(getActivity().getResources().getString(R.string.loading_text_tip));
		loadingTv.setTextSize(15);
		loadingTv.setLayoutParams(param_loadingtxt);
		
		ProgressBar pbLoading=new ProgressBar(getActivity());
		pbLoading.setMax(100);
		FrameLayout.LayoutParams progressBarParam=new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);//���岼�ֹ������Ĳ���
		progressBarParam.gravity=Gravity.CENTER;
		
		loadingContainer.addView(pbLoading);
		loadingContainer.addView(loadingTv);
			
		loadingContainer.setVisibility(View.INVISIBLE);
		waitLayout.addView(loadingContainer);
		
		return waitLayout; */
		return view;
	}
	/**
	 * �õ��Զ����progressDialog
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.layout_progressdialog, null);// �õ�����view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
		// main.xml�е�ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// ��ʾ����
		// ���ض���
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.progressdialog_anim);
		// ʹ��ImageView��ʾ����
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// ���ü�����Ϣ
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// �����Զ�����ʽdialog
		loadingDialog.setCancelable(false);// �������á����ؼ���ȡ��
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// ���ò���
		return loadingDialog;

	}
	
	protected void showProgressDialog(Context context,String content) {
		progressDialog = createLoadingDialog(context,content);
		progressDialog.show();
		progressDialog.setOnKeyListener(onKeyListener);

	}
	private OnKeyListener onKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (progressDialog.isShowing()&&keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				removeBroadcast();
				dismissProgressDialog();
			}
			return false;
		}
	};
	protected void dismissProgressDialog() {
		  if (getActivity().isFinishing()) {
	            return;
	        }
		if (null != progressDialog && progressDialog.isShowing()) {
			removeBroadcast();
			progressDialog.dismiss();

		}
	}
}

