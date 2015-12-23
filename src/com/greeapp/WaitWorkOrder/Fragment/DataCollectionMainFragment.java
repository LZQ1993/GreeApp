package com.greeapp.WaitWorkOrder.Fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.greeapp.R;
import com.greeapp.Entity.CollectionData;
import com.greeapp.Entity.LocalCollectData;
import com.greeapp.Entity.OrderDetail;
import com.greeapp.Entity.ProductList;
import com.greeapp.Entity.ProductType;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.WaitWorkOrder.Activity.DataCollectionActivity;
import com.greeapp.WaitWorkOrder.Activity.DataFinishedCollectionDetailActivity;
import com.greeapp.WaitWorkOrder.Activity.WaitWorkOrderItemDetailActivity;
import com.greeapp.WaitWorkOrder.Assistant.ProductListAdapter;

public class DataCollectionMainFragment extends DataRequestFragment implements
		OnClickListener{
	private Context mContext;
	private String currentNotiName = "";
	private ImageButton nav_bar_btn_left;
	private TextView tv_waitcollection,tv_finishedcollection;
	private String UserName;
	private List<CollectionData> finished;
	private List<CollectionData> waited;
	private ListView ls_finishedCollection,ls_waitCollection;
	private OrderDetail orderDetail;
	private ProductListAdapter finishedproductListAdapter,waitedproductListAdapter;
	private int totalNum = 0;
	private String locationTime="",locationSpot="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage",null);
		UserMessage userMessage = (UserMessage) list.get(0);
		UserName = userMessage.UserName;
		orderDetail = (OrderDetail) getActivity().getIntent().getSerializableExtra("OrderDetailInfo");
		initList();
	}
	private void initList() {
		finished = new ArrayList<CollectionData>();
		waited = new ArrayList<CollectionData>();
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
		
		for(int i=0;i<orderDetail.ProductList.size();i++){
			ProductList pl = (ProductList) orderDetail.ProductList.get(i);
			for(int j=0;j<Integer.parseInt(pl.ProductNumber);j++){
				CollectionData cd = new CollectionData();
				cd.ProductModel = pl.ProductModel;
				ProductType pt = (ProductType) pl.ProductType.get(j);				
				cd.SubProductID = pt.SubProductID;
				List<Object> list = iSqlHelper.Query("com.greeapp.Entity.LocalCollectData","SubProductID='"+pt.SubProductID+"' and IsFinishedCollectFlag ='1'");
				if(list.size()>0){
					finished.add(cd);
				}else{
					waited.add(cd);
				}
			}
			totalNum = totalNum+Integer.parseInt(pl.ProductNumber);
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_datacollection_main,
				container, false);
		view.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(backlistener);
		nav_bar_btn_left = (ImageButton) view.findViewById(R.id.dc_nav_bar_btn_left);
		nav_bar_btn_left.setOnClickListener(this);
		tv_waitcollection =  (TextView) view.findViewById(R.id.tv_waitcollection);
		tv_finishedcollection =  (TextView) view.findViewById(R.id.tv_finishedcollection);
		tv_finishedcollection.setText("已采集("+finished.size()+")");
		tv_waitcollection.setText("未采集("+waited.size()+")");
		finishedproductListAdapter = null;
		waitedproductListAdapter= null;
		finishedproductListAdapter = new ProductListAdapter(getActivity(),finished);
		waitedproductListAdapter = new ProductListAdapter(getActivity(),waited);
		ls_finishedCollection = (ListView) view.findViewById(R.id.ls_finishedCollection);
		ls_waitCollection = (ListView) view.findViewById(R.id.ls_waitCollection);
		ls_finishedCollection.setAdapter(finishedproductListAdapter);
		ls_waitCollection.setAdapter(waitedproductListAdapter);
		ls_finishedCollection.setOnItemClickListener(finishedItemClickListener);
		ls_waitCollection.setOnItemClickListener(waitedItemClickListener);
        return view;
		}
		
		
	@Override
	public void onClick(View v) {
		if (v == nav_bar_btn_left) {
			Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);	
			finishedFragment();
		}
	}
	
	public void finishedFragment(){
		if(finished.size()==totalNum){
			Intent intent=new Intent(mContext, WaitWorkOrderItemDetailActivity.class);
			getActivity().setResult(10, intent);
			getActivity().finish();	
		}else{
			new AlertDialog.Builder(getActivity())
			.setTitle("提示")
			.setMessage("您还未完成数据采集工作，是否继续退出？")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							getActivity().finish();		
							return;
						}
					})
			.setNegativeButton("取消", null)
			.setCancelable(false).show();
			return;
		}			
	}
	
	private OnItemClickListener finishedItemClickListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//Toast.makeText(getActivity(), finished.get(position).SubProductID, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.putExtra("SubProductID",finished.get(position).SubProductID);
			intent.setClass(getActivity(), DataFinishedCollectionDetailActivity.class);
			startActivity(intent);	
		}
		
	};
	
	private OnItemClickListener waitedItemClickListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.putExtra("WorkID", orderDetail.WorkID);
			intent.putExtra("ProductType",waited.get(position).ProductModel);
			intent.putExtra("SubProductID",waited.get(position).SubProductID);
			intent.putExtra("LocationTime",getActivity().getIntent().getStringExtra("LocationTime"));
			intent.putExtra("LocationSpot", getActivity().getIntent().getStringExtra("LocationSpot"));
			intent.putExtra("Position",position);
			intent.setClass(getActivity(), DataCollectionActivity.class);
			startActivityForResult(intent,1);
		}
		
	};
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==10){
			Bundle b = data.getExtras();
			int p = b.getInt("Position");
			finished.add(waited.get(p));
			waited.remove(p);
			finishedproductListAdapter.notifyDataSetChanged();
			waitedproductListAdapter.notifyDataSetChanged();
			tv_finishedcollection.setText("已采集("+finished.size()+")");
			tv_waitcollection.setText("未采集("+waited.size()+")");
			
		}
	}
	private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK) {  //表示按返回键 时的操作                     
                    finishedFragment();
                    return false;    //已处理
                }
            }
            return false;
        }
    };
	
	
}
