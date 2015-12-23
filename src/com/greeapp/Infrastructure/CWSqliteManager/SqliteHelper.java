package com.greeapp.Infrastructure.CWSqliteManager;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.greeapp.Infrastructure.CWFileSystem.AbsFileSystem;
import com.greeapp.Infrastructure.CWFileSystem.AssetFileSystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteHelper implements ISqlHelper{
	private static final int CLASS_TYPE_STRING=0;
	private static final int CLASS_TYPE_OBJECT=1;
	
     private SQLiteDatabase dataBase;
     private String _dataBaseName;
     private Context _context;
     private String classAbsName="";
     private Class<?> cls=null;
     private Field[] fields=null;
     private String PrimaryFieldName="";
     private String PrimaryFieldValue="";
     private ContentValues contentValues=null;
     
     
     
     public SqliteHelper(String DBName,Context context){
    	 if(DBName==""||DBName==null){
    		 _dataBaseName="DataBase.db"; 
    	 }else{
    		 _dataBaseName=DBName;	  
    	 }
    	 _context=context;
    	  DBOpen();
     }
     
     public void SQLExec(String sql){
    	 dataBase.execSQL(sql);
     }
     
     private void DBOpen(){
    	 dataBase = _context.openOrCreateDatabase(_dataBaseName, Context.MODE_PRIVATE, null);
     }
     
     private String getAbsClassName(){
    	 String classFullName = cls.getName(); 
    	 String[] names=classFullName.split("\\.");
    	 String className=names[names.length-1];
    	 return className;
     }
     
     private boolean setClassProperty(Object obj,int Type){
    	 boolean result=false;
		try {
			if (Type == CLASS_TYPE_STRING) {
				cls = Class.forName((String) obj);
			} else if (Type == CLASS_TYPE_OBJECT) {
				cls = obj.getClass();
			}
			classAbsName = getAbsClassName();
			fields = cls.getFields();
			PrimaryFieldName = fields[0].getName();
			result= true;
			
		} catch (Exception e) {
			result= false;
		}
		return result;
     }
     
	public Boolean CreateTable(String TableName) {
		boolean result=false;
		String Propertys = "";
		DBOpen();
		if (setClassProperty(TableName,CLASS_TYPE_STRING)) {
			Propertys = PrimaryFieldName+" INTEGER PRIMARY KEY AUTOINCREMENT,";
			for (int i = 0; i < fields.length; i++) {
				if(i!=0){
					Field f = fields[i];
					String FieldName = f.getName();
					f.setAccessible(true);
					Propertys += FieldName + " VARCHAR,";
				}
			}
			Propertys = Propertys.substring(0, Propertys.length() - 2);
			String sql = "CREATE TABLE IF NOT EXISTS " + classAbsName + "("
					+ Propertys + ")";
			try {
				SQLExec(sql);
				result= true;
			} catch (Exception ex) {
				result= false;
			}

		} else {
			result= false;
		}
		return result;
	}

     
     public Boolean Insert(Object obj){ 
    	 InsertOrUpdate(obj);
         dataBase.insert(classAbsName, null, contentValues);  
    	 return true;
     }
     public Boolean Update(Object obj){
    	 InsertOrUpdate(obj);
         dataBase.update(classAbsName, contentValues, PrimaryFieldName+" = ?", new String[]{PrimaryFieldValue});  
    	 return true;
     }
     public Boolean Delete(Object obj){
    	 InsertOrUpdate(obj);
    	 dataBase.delete(classAbsName, PrimaryFieldName+" = ?", new String[]{PrimaryFieldValue});  
    	 return true;
     }
     
     
     private void InsertOrUpdate(Object obj){
    	 DBOpen();
    	 contentValues = new ContentValues(); 
    	 if (setClassProperty(obj,CLASS_TYPE_OBJECT)) {
    		 for ( int i = 0 ; i < fields. length ; i++){
                 Field currentfield = fields[i];
                 String FieldName=currentfield.getName();
                 currentfield.setAccessible( true ); 
                 Object val=null;
                 try{
                    val=currentfield.get(obj);
                 }catch(Exception ex){
                	 Log.d("errors:", ex.getMessage());
                 }
                 if(val==null){
            	     val="";
                 }
                 if(i==0){
                	 PrimaryFieldValue=val.toString();
                 }else{
                	 contentValues.put(FieldName, val.toString());  
                 }
             } 
    	 }
         
     }
     
     
     public List<Object> Query(String className,String WhereStr){
    	 DBOpen();
    	 if(setClassProperty(className,CLASS_TYPE_STRING)){
    		 String SqlPRE="SELECT * FROM "+classAbsName;
             if(WhereStr!=null){
            	 SqlPRE+=" where "+WhereStr;
             }
        	 Cursor curesor = dataBase.rawQuery(SqlPRE, null);  
        	 List<Object> lists=new ArrayList<Object>();
        	 while (curesor.moveToNext()) { 
        		 Object objectCopy=null;
    			try {
    				objectCopy = cls.newInstance();
    			} catch (InstantiationException e) {
    				e.printStackTrace();
    			} catch (IllegalAccessException e) {
    				e.printStackTrace();
    			}  
        		 for ( int i = 0 ; i < fields. length ; i++){
                     Field f = fields[i];
                     String FieldName=f.getName();
                     f.setAccessible( true ); // ����Щ�����ǿ��Է��ʵ�
                     String type = f.getType().toString(); // �õ������Ե�����
                     if (type.endsWith("String")) {
                        try {
    						f.set(objectCopy, curesor.getString(curesor.getColumnIndex(FieldName))) ;
    					} catch (IllegalArgumentException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (IllegalAccessException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}        // ��������ֵ
                     } 
                     else if (type.endsWith("int") || type.endsWith("Integer")){
                    	 try {
    						f.set(objectCopy, curesor.getInt(curesor.getColumnIndex(FieldName)));
    					} catch (IllegalArgumentException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (IllegalAccessException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
                     } 
                     else {
                    	 try {
    						f.set(objectCopy, curesor.getString(curesor.getColumnIndex(FieldName)));
    					} catch (IllegalArgumentException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (IllegalAccessException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
                     }
                 } 
            	 lists.add(objectCopy);
             }
             curesor.close();
             return lists;
    	 }
    	 else{
    		 return null;
    	 }
    	 
         
     }

     
     public void CloseDB(){
    	 dataBase.close();  
     }

	public Integer[] AutobatProceed(String fileName) {
		Integer[] results = new Integer[2];
		Integer successCount = 0;
		Integer errorCount = 0;
		AbsFileSystem mabs = new AssetFileSystem(_context);
		String sqls = mabs.readTxt(fileName);
		String[] sqlList = sqls.split(";");
		DBOpen();
		for (int i = 0; i < sqlList.length; i++) {
			try {
				if(!(sqlList[i].trim().equals(""))){
					SQLExec(sqlList[i]);
					successCount++;
				}
			} catch (Exception ex) {
				errorCount++;
			}
		}

		results[0] = successCount;
		results[1] = errorCount;
		return results;
	}

}
