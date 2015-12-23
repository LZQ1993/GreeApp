package com.greeapp.Infrastructure.CWFileSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class AbsFileSystem implements IFileSystem {

	private Context cxt;
	public void setContext(Context _cxt) {
		cxt=_cxt;
	}
	
	public Context getContext(){
		return cxt;
	}

	@Override
	public String readTxt(String inFileName) {
		String text = null;
		try {
			InputStream is = getContext().getAssets().open(inFileName);
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(reader);
			StringBuffer buffer = new StringBuffer("");
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
				buffer.append("\n");
			}				
			text = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	@Override
	public boolean write(String content, String outFileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> getFileList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void addFile(File[] currentFiles,List<Map<String,Object>> filesAndFolders){
		for(int i=0;i<currentFiles.length;i++){
			Map<String,Object> fileAndFolder= new HashMap<String,Object>();
			if(currentFiles[i].isFile()){
				fileAndFolder.put("type", "file");
				fileAndFolder.put("name", currentFiles[i].getName());
				filesAndFolders.add(fileAndFolder);
			}else{
				fileAndFolder.put("type", "folder");
				fileAndFolder.put("name", currentFiles[i].getName());
				filesAndFolders.add(fileAndFolder);
				addFile(currentFiles[i].listFiles(),filesAndFolders);
			}

		}
	}
	
    public String getLocalPath() {
        return cxt.getApplicationContext().getFilesDir().getAbsolutePath();
    }
    
	public Bitmap getImage(String fileName){
		  Bitmap image = null;  
	      AssetManager am = getContext().getResources().getAssets();  
	      try  
	      {  
	          InputStream is = am.open(fileName);  
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  
	  
	      return image;
	}

}
