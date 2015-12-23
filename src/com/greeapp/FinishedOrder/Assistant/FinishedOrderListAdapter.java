package com.greeapp.FinishedOrder.Assistant;

import java.util.List;

import com.greeapp.R;
import com.greeapp.Entity.FinishedOrder;
import com.greeapp.Entity.WaitAcceptOrder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FinishedOrderListAdapter extends ArrayAdapter<FinishedOrder> {
	private OnClickListener mycListener;
	private Context context;
	private int listViewId;
	public FinishedOrderListAdapter(Context _ctx,int _listViewId,List<FinishedOrder> listItemes,OnClickListener _mycListener) {
		super(_ctx,_listViewId,listItemes);
		context=_ctx;
		listViewId=_listViewId;
		this.mycListener = _mycListener;
	}

	public static class ViewHolder {
		TextView tv_type, tv_clientName,tv_tel,tv_address,tv_finishTime;

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
			holder.tv_finishTime=(TextView)convertView.findViewById(R.id.tv_finishTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FinishedOrder listItem=getItem(position);			
		holder.tv_type.setText(listItem.WorkType);
		holder.tv_clientName.setText(listItem.ClientName);
		holder.tv_tel.setText(listItem.ClientTel);
		holder.tv_address.setText(listItem.ClientAddress);
		holder.tv_finishTime.setText(listItem.FinishTime);
		holder.tv_tel.setOnClickListener(mycListener);
		holder.tv_tel.setTag(position);
		return convertView;
	}
}
