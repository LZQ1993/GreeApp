package com.greeapp.WaitWorkOrder.Assistant;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greeapp.R;
import com.greeapp.Entity.CollectionData;
import com.greeapp.Entity.ProductList;

public class ProductListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<CollectionData> productType;
	private Context context;
	
	public ProductListAdapter(Context context,
			List<CollectionData> productType) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.productType = productType;

	}
	public static class ViewHolder {
		TextView tv_productType;
	}
	
	@Override
	public int getCount() {
		return productType.size();
	}

	@Override
	public Object getItem(int position) {
		return productType.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.productlist_item, null);
			holder.tv_productType = (TextView) convertView.findViewById(R.id.tv_productType);		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		
		CollectionData cd  = (CollectionData) getItem(position);
		holder.tv_productType.setText(cd.ProductModel);
		return convertView;
	}

}
