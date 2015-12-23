package com.greeapp.WaitAcceptOrder.Assistant;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.greeapp.R;
import com.greeapp.Entity.WaitAcceptOrder;

public class WaitAcceptOrderListAdapter extends ArrayAdapter<WaitAcceptOrder> {

	private Context context;
	private int listViewId;
	private OnClickListener mycListener;
	public WaitAcceptOrderListAdapter(Context _ctx,int _listViewId,List<WaitAcceptOrder> listItemes,OnClickListener mycListener) {
		super(_ctx,_listViewId,listItemes);
		context=_ctx;
		listViewId=_listViewId;
		this.mycListener = mycListener;
		
	}

	public static class ViewHolder {
		TextView tv_type, tv_clientName,tv_tel,tv_address,tv_assignTime;
        Button btn_AcceptOrder,btn_NoAcceptOrder;
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
			holder.tv_assignTime=(TextView)convertView.findViewById(R.id.tv_assignTime);
			holder.btn_AcceptOrder = (Button) convertView.findViewById(R.id.btn_AcceptOrder);
			holder.btn_NoAcceptOrder = (Button) convertView.findViewById(R.id.btn_NoAcceptOrder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		WaitAcceptOrder listItem=getItem(position);			
		holder.tv_type.setText(listItem.WorkType);
		holder.tv_clientName.setText(listItem.ClientName);
		holder.tv_tel.setText(listItem.ClientTel);
		holder.tv_address.setText(listItem.ClientAddress);
		holder.tv_assignTime.setText(listItem.AssignTime);
		holder.btn_AcceptOrder.setTag(position);
		holder.btn_AcceptOrder.setOnClickListener(mycListener);
		holder.btn_NoAcceptOrder.setTag(position);
		holder.btn_NoAcceptOrder.setOnClickListener(mycListener);
		holder.tv_tel.setTag(position);
		holder.tv_tel.setOnClickListener(mycListener);

		return convertView;
	}

}
