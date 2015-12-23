package com.greeapp.FinishedOrder.Domain;

import java.util.ArrayList;

import android.content.Context;

import com.greeapp.Entity.FinishedOrder;

public class FinishedOrderListLab {
private ArrayList<FinishedOrder> ListItemes;
	
	private static FinishedOrderListLab aListLab;
	private Context  appContext;
	
	private FinishedOrderListLab(Context _appContext){
		appContext=_appContext;
		ListItemes=new ArrayList<FinishedOrder>();
	}
	
	public static FinishedOrderListLab get(Context c){
		if(aListLab==null){
			aListLab=new FinishedOrderListLab(c);
		}
		return aListLab;
	}
	
	public ArrayList<FinishedOrder> getListItemes(){
		return ListItemes;
	}
	
	public FinishedOrder getListItem(int autoId){
		FinishedOrder li = ListItemes.get(autoId);
		return li;
	}
	
	
	public void add(FinishedOrder obj){
		ListItemes.add(obj);
	}
	
	public void remove(FinishedOrder obj){
		ListItemes.remove(obj);
	}
}
