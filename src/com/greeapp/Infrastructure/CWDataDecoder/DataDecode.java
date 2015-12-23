package com.greeapp.Infrastructure.CWDataDecoder;




public class DataDecode implements IDataDecode {

	private String mainClassName="";
	@Override
	public Object decode(String data, String className){
		
		DataResult dataResult = new DataResult();
		JsonDecode jsonDecode = new JsonDecode();
		if(mainClassName.equals("")){
			mainClassName="com.greeapp.Infrastructure.CWDataDecoder.DataResult";
		}
		if(!(jsonDecode.fromJson(data,mainClassName,className) instanceof DataResult)){
			dataResult = null;					
		}else{
		dataResult = (DataResult)jsonDecode.fromJson(data,mainClassName,className);
		}
		return  dataResult;
	}
	
	public void setMainClassName(String mainClassName){
		this.mainClassName=mainClassName;
	}


}
