package com.greeapp.Infrastructure.CWDataDecoder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.greeapp.Infrastructure.CWDomain.GlobalVariables;

public class JsonDecode {
    private static String IntStr = "boolean";
    private static String StringStr = "class java.lang.String";
    private static String BooleanStr = "boolean";
    private static String LongStr = "long";
    private static String DoubleStr = "double";
    private static String JSONArrayStr = "class org.json.JSONArray";
    String sonObject="";
    
    public List<Object> DataFromJson(String ClassName,JSONArray jsonDataArray){
        sonObject=GlobalVariables.DATAEntity_PACKAGE+"."+ClassName;	
    	Class<?> targetClass = null;
		Object targetObj = null;
		try {
			targetClass = Class.forName(sonObject);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Field[] fields = targetClass.getFields();
		
		List<Object> lists = new ArrayList<Object>();
		for (int j = 0; j < jsonDataArray.length(); j++) {
			try {
				targetObj = targetClass.newInstance();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JSONObject jsonObjectData = (JSONObject) jsonDataArray.opt(j);
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();	
				Object value = jsonObjectData.opt(fieldName);
				String valueTyper=value.getClass().toString();
				if(valueTyper.equals(JSONArrayStr)){
					try {
						field.set(targetObj,DataFromJson(fieldName,(JSONArray)value));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}else if(valueTyper.equals(StringStr)){
					try {
						field.set(targetObj, value.toString());
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
			lists.add(targetObj);
		}
		return lists;
    }
	public Object fromJson(String jsonData, String _MainObjectName,
			String _sonObject){
    	Class<?> targetClass = null;
		Object targetObj = null;
		try {
			targetClass = Class.forName(_MainObjectName);
			targetObj = targetClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		// 得到他们的属性
		Field[] fields = targetClass.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();	
			JSONObject  jsonObjectData;
			try {
				jsonObjectData = new JSONObject(jsonData);
				Object value = jsonObjectData.opt(fieldName);
				if(fieldName.equals("result")){
					try {
						field.set(targetObj,DataFromJson(_sonObject,(JSONArray)value));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
						field.set(targetObj, value.toString());
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    	return targetObj;
	}
	

   
	private static void setValue(Field field,JSONObject jsonObject,Object object) {
		Class<?> fieldType = field.getType();
		Object value = null;
		String fieldName = field.getName();
		String type = fieldType.toString();
		if(type.equals(IntStr)){
			value = jsonObject.optInt(fieldName,-1);
		}else if(type.equals(BooleanStr)){
			value = jsonObject.optBoolean(fieldName, false);
		}else if(type.equals(LongStr)){
			value = jsonObject.optLong(fieldName, -1);
		}else if(type.equals(DoubleStr)){
			value = jsonObject.optDouble(fieldName, -1);
		}else if(type.equals(StringStr)){
			value = jsonObject.optString(fieldName, "FF");
		}
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static String toJson(String[] condition, String[] value) {
		String str_json = "";
		String str1 = "{\"";
		String str2 = "}";
		String str3 = "\":\"";
		String str4 = "\",\"";
		for (int i = 0; i < condition.length; i++) {
			str1 = str1 + condition[i] + str3 + value[i] + str4;
		}
		if (str1.length() > 10) {
			str_json = str1.substring(0, str1.length() - 2) + str2;
		}
		return str_json;
	}
}
