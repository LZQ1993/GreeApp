package com.greeapp.Entity;

import java.io.DataInputStream;
import java.io.FileInputStream;

import com.greeapp.Assistant.JsonUtil;

public class UploadData {
	public String BarCodeType;
	public String BarCode;
	public FileInputStream PIC;
	//public DataInputStream  PIC;
	public String toJson() {
    	return JsonUtil.toJson(this);
    }
}
