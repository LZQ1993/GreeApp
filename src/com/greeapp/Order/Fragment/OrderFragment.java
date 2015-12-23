package com.greeapp.Order.Fragment;

import com.greeapp.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class OrderFragment extends Fragment implements OnClickListener{
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_order, container, false);
		findView(view);
		return view;
	}
	private void findView(View view) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
