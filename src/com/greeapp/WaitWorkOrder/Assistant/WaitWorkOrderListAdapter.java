package com.greeapp.WaitWorkOrder.Assistant;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.greeapp.R;
import com.greeapp.Entity.WaitWorkOrder;

public class WaitWorkOrderListAdapter extends ArrayAdapter<WaitWorkOrder> {

	private Context context;
	private int listViewId;
	private OnClickListener mycListener;
	public WaitWorkOrderListAdapter(Context _ctx,int _listViewId,List<WaitWorkOrder> listItemes,OnClickListener mycListener) {
		super(_ctx,_listViewId,listItemes);
		context=_ctx;
		listViewId=_listViewId;
		this.mycListener = mycListener;
	}

	public static class ViewHolder {
		TextView tv_type, tv_clientName,tv_tel,tv_address,tv_workTime;
        ImageView iv_overflag;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
		    holder = new ViewHolder();
			LayoutInflater listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
			convertView=listContainer.inflate(listViewId, null);			
			holder.tv_type=(TextView)convertView.findViewById(R.id.tv_type);
			holder.tv_clientName=(TextView)convertView.findViewById(R.id.tv_clientName);
			holder.tv_tel=(TextView)convertView.findViewById(R.id.tv_tel);
			holder.tv_address=(TextView)convertView.findViewById(R.id.tv_address);
			holder.tv_workTime=(TextView)convertView.findViewById(R.id.tv_workTime);
			holder.iv_overflag = (ImageView) convertView.findViewById(R.id.iv_overflag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		WaitWorkOrder listItem=getItem(position);			
		holder.tv_type.setText(listItem.WorkType);
		holder.tv_clientName.setText(listItem.ClientName);
		holder.tv_tel.setText(listItem.ClientTel);
		holder.tv_address.setText(listItem.ClientAddress);
		holder.tv_workTime.setText(listItem.AppointTime);
		if(listItem.OverrunFlag.equals("0")){
			holder.iv_overflag.setBackgroundResource(R.drawable.time);	
		}else if(listItem.OverrunFlag.equals("1")){
			holder.iv_overflag.setBackgroundResource(R.drawable.soonovertime);	
		}else{
			holder.iv_overflag.setBackgroundResource(R.drawable.overtime);	
		}
		holder.tv_tel.setTag(position);
		holder.tv_tel.setOnClickListener(mycListener);
		return convertView;
	}

}
