package com.greeapp.WaitWorkOrder.Fragment;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.greeapp.R;
import com.greeapp.Entity.LocalCollectData;
import com.greeapp.Entity.OrderDetail;
import com.greeapp.Entity.ReturnTransactionMessage;
import com.greeapp.Entity.UploadData;
import com.greeapp.Entity.UserMessage;
import com.greeapp.Infrastructure.CWDataDecoder.DataResult;
import com.greeapp.Infrastructure.CWDataDecoder.JsonDecode;
import com.greeapp.Infrastructure.CWDataRequest.RequestUtility;
import com.greeapp.Infrastructure.CWFragment.DataRequestFragment;
import com.greeapp.Infrastructure.CWSqliteManager.ISqlHelper;
import com.greeapp.Infrastructure.CWSqliteManager.SqliteHelper;
import com.greeapp.WaitWorkOrder.Activity.CaptureActivity;
import com.greeapp.WaitWorkOrder.Activity.DataCollectionMainActivity;
import com.greeapp.WaitWorkOrder.Activity.GalleryActivity;
import com.greeapp.WaitWorkOrder.Assistant.Bimp;
import com.greeapp.WaitWorkOrder.Assistant.ImageItem;

public class DataCollectionFragment extends DataRequestFragment implements
		OnClickListener, OnCheckedChangeListener {
	private Context mContext;
	private String currentNotiName = "UpLoadCollectionData";
	private String currentNotiName2 = "UpLoadCollectionData2";
	private String currentNotiName3 = "UpLoadCollectionData3";
	private ImageButton nav_bar_btn_left,nav_bar_btn_right;
	private String UserName,WorkID,ProductType,SubProductID;
	private RadioGroup rgs;
	private LinearLayout ll_Interior_1, ll_External, ll_Interior_2;
	private Button btn_Interior_1, btn_External, btn_Interior_2;
	private TextView tv_Interior_1, tv_External, tv_Interior_2;
	private ImageView iv_Interior_1, iv_External, iv_Interior_2;
	private RadioButton btn1,btn2,btn3,btn4,btn5;
	private String BarCodeType = "1",BarCode="";
	private String name;
	private String path;
	private Uri uri;
	private File file;
	private int position;
	private boolean IsPhoto1=false,IsPhoto2=false,IsPhoto3=false;
	private int upLoadType;
	private String MachineType = "1",InteriorNum1="",InteriorNum2="",ExternalNum="",InteriorPicPath1="",ExternalPicPath="",InteriorPicPath2="";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		Notifications.add(currentNotiName);
		Notifications.add(currentNotiName2);
		Notifications.add(currentNotiName3);
		WorkID = getActivity().getIntent().getStringExtra("WorkID");
		ProductType = getActivity().getIntent().getStringExtra("ProductType");
		SubProductID = getActivity().getIntent().getStringExtra("SubProductID");
		position = getActivity().getIntent().getIntExtra("Position",0);
		ISqlHelper iSqlHelper = new SqliteHelper(null, mContext);
		List<Object> list = iSqlHelper.Query("com.greeapp.Entity.UserMessage",
				null);
		UserMessage userMessage = (UserMessage) list.get(0);
		UserName = userMessage.UserName;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_datacollection,
				container, false);
		nav_bar_btn_left = (ImageButton) view
				.findViewById(R.id.dc_nav_bar_btn_left);
		nav_bar_btn_right = (ImageButton) view
				.findViewById(R.id.dc_nav_bar_btn_right);
		nav_bar_btn_left.setOnClickListener(this);
		nav_bar_btn_right.setOnClickListener(this);
		rgs = (RadioGroup) view.findViewById(R.id.myRadioGroup);
		rgs.setOnCheckedChangeListener(this);
		ll_Interior_1 = (LinearLayout) view.findViewById(R.id.ll_Interior_1);
		ll_External = (LinearLayout) view.findViewById(R.id.ll_External);
		ll_Interior_2 = (LinearLayout) view.findViewById(R.id.ll_Interior_2);
		btn_Interior_1 = (Button) view.findViewById(R.id.btn_Interior_1);
		btn_External = (Button) view.findViewById(R.id.btn_External);
		btn_Interior_2 = (Button) view.findViewById(R.id.btn_Interior_2);
		tv_Interior_1 = (TextView) view.findViewById(R.id.tv_Interior_1);
		tv_External = (TextView) view.findViewById(R.id.tv_External);
		tv_Interior_2 = (TextView) view.findViewById(R.id.tv_Interior_2);
		iv_Interior_1 = (ImageView) view.findViewById(R.id.iv_Interior_1);
		iv_External = (ImageView) view.findViewById(R.id.iv_External);
		iv_Interior_2 = (ImageView) view.findViewById(R.id.iv_Interior_2);
		btn1 = (RadioButton) view.findViewById(R.id.btn1);
		btn2 = (RadioButton) view.findViewById(R.id.btn2);
		btn3 = (RadioButton) view.findViewById(R.id.btn3);
		btn4 = (RadioButton) view.findViewById(R.id.btn4);
		btn5 = (RadioButton) view.findViewById(R.id.btn5);
		btn_Interior_1.setOnClickListener(this);
		btn_External.setOnClickListener(this);
		btn_Interior_2.setOnClickListener(this);
		iv_Interior_1.setOnClickListener(this);
		iv_External.setOnClickListener(this);
		iv_Interior_2.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v == nav_bar_btn_left) {
			// 加载动画
			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.back_anim);
			nav_bar_btn_left.setAnimation(animation);
			Intent intent = new Intent();
			getActivity().setResult(0, intent);
			getActivity().finish();
		}
		if (v == nav_bar_btn_right) {
			DataIsNull();			
		}
		if (v == btn_Interior_1) {
			 Intent intent = new Intent();
             intent.setClass(getActivity(), CaptureActivity.class);
             startActivityForResult(intent,1);
		}
		if (v == btn_External) {
			 Intent intent = new Intent();
             intent.setClass(getActivity(), CaptureActivity.class);
             startActivityForResult(intent,2);
		}
		if (v == btn_Interior_2) {
			 Intent intent = new Intent();
             intent.setClass(getActivity(), CaptureActivity.class);
             startActivityForResult(intent,3);
		}
		if (v == iv_Interior_1) {
			if (!IsPhoto1) {
				photo(11);
			} else {				
				Intent intent = new Intent(getActivity(),
						GalleryActivity.class);
				intent.putExtra("ID", 11);
				startActivityForResult(intent, 11);
			}

		}
		if (v == iv_External) {
			if (!IsPhoto2) {
				photo(22);
			} else {
				Intent intent = new Intent(getActivity(),
						GalleryActivity.class);
				intent.putExtra("ID", 22);
				startActivityForResult(intent, 22);
			}

		}
		if (v == iv_Interior_2) {
			if (!IsPhoto3) {
				photo(33);
			} else {
				Intent intent = new Intent(getActivity(),
						GalleryActivity.class);
				intent.putExtra("ID", 33);
				startActivityForResult(intent, 33);
			}
		}

	}

	private void DataIsNull() {	
		if(btn1.isChecked()){
		    if(tv_Interior_1.getText().toString().equals("")||
		    		tv_External.getText().toString().equals("")||
		    		IsPhoto1==false||IsPhoto2==false){
		    	Toast.makeText(getActivity(), "请您采集全部信息再提交", Toast.LENGTH_SHORT).show();
		    }else{
		    	InteriorNum1=tv_Interior_1.getText().toString();
		    	ExternalNum=tv_External.getText().toString();
		    	for (ImageItem li : Bimp.tempSelectBitmap) {
					if (li.imageId == 11) {
						InteriorPicPath1=li.getImagePath();					
					}
					if(li.imageId == 22){
						ExternalPicPath=li.getImagePath();
					}
				}	
		    	upLoadType=2;
		    	BarCodeType = "1";
		    	BarCode = InteriorNum1;
		    	NetWork(InteriorPicPath1,currentNotiName,"内机码1图片上传中，请稍候...");
		    	
		    }
		}
		if(btn2.isChecked()){
			if(tv_Interior_1.getText().toString().equals("")||
		    		tv_External.getText().toString().equals("")||
		    		tv_Interior_2.getText().toString().equals("")||
		    		IsPhoto1==false||IsPhoto2==false||IsPhoto3==false){
		    	Toast.makeText(getActivity(), "请您采集全部信息再提交", Toast.LENGTH_SHORT).show();
		    }else{
		    	InteriorNum1=tv_Interior_1.getText().toString();
		    	InteriorNum2=tv_Interior_2.getText().toString();
		    	ExternalNum=tv_External.getText().toString();
		    	for (ImageItem li : Bimp.tempSelectBitmap) {
					if (li.imageId == 11) {
						InteriorPicPath1=li.getImagePath();					
					}
					if(li.imageId == 22){
						ExternalPicPath=li.getImagePath();
					}
					if(li.imageId == 33){
						InteriorPicPath2=li.getImagePath();
					}
				}
		    	upLoadType=3;
		    	BarCodeType = "1";
		    	BarCode = InteriorNum1;
		    	NetWork(InteriorPicPath1,currentNotiName,"内机码1图片上传中，请稍候...");
		    	
		    }
		}
		if(btn3.isChecked()){
			if(tv_Interior_1.getText().toString().equals("")||
		    		IsPhoto1==false){
		    	Toast.makeText(getActivity(), "请您采集全部信息再提交", Toast.LENGTH_SHORT).show();
		    }else{
		    	InteriorNum1=tv_Interior_1.getText().toString();
		    	for (ImageItem li : Bimp.tempSelectBitmap) {
					if (li.imageId == 11) {
						InteriorPicPath1=li.getImagePath();					
					}
				}	
		    	upLoadType=1;
		    	BarCodeType = "1";
		    	BarCode = InteriorNum1;
		    	NetWork(InteriorPicPath1,currentNotiName,"内机码1图片上传中，请稍候...");
		    }
		}
		if(btn4.isChecked()){
			if(tv_Interior_1.getText().toString().equals("")||
		    		IsPhoto1==false){
		    	Toast.makeText(getActivity(), "请您采集全部信息再提交", Toast.LENGTH_SHORT).show();
		    }else{
		    	InteriorNum1=tv_Interior_1.getText().toString();
		    	for (ImageItem li : Bimp.tempSelectBitmap) {
					if (li.imageId == 11) {
						InteriorPicPath1=li.getImagePath();					
					}
				}	
		    	upLoadType=1;
		    	BarCodeType = "1";
		    	BarCode = InteriorNum1;
		    	NetWork(InteriorPicPath1,currentNotiName,"内机码1图片上传中，请稍候...");
		    }
		}
		if(btn5.isChecked()){
			if(tv_External.getText().toString().equals("")||
		    		IsPhoto2==false){
		    	Toast.makeText(getActivity(), "请您采集全部信息再提交", Toast.LENGTH_SHORT).show();
		    }else{
		    	ExternalNum=tv_External.getText().toString();
		    	for (ImageItem li : Bimp.tempSelectBitmap) {
					if (li.imageId == 22) {
						ExternalPicPath=li.getImagePath();					
					}
				}
		    	upLoadType=1;
		    	BarCodeType = "2";
		    	BarCode = ExternalNum;
		    	NetWork(ExternalPicPath,currentNotiName,"外机码图片上传中，请稍候...");
		    }
		}
		
	}

	private void NetWork(String Imagepath,String notiName,String strtip) {
		RequestUtility myru = new RequestUtility();
		myru.setIP(null);
		myru.setMethod("WorkSystemService", "uploadData");
		Map requestCondition = new HashMap();
		String condition[] = { "SubProductID","BarCodeType","BarCode"};
		String value[] = {SubProductID,BarCodeType,BarCode};	
	    File files = new File(Imagepath);
	    String strJson = JsonDecode.toJson(condition, value);
		requestCondition.put("json", strJson);
		requestCondition.put("image", files);
		myru.setParams(requestCondition);
		myru.setNotification(notiName);
		setRequestUtility(myru);
		requestData();
		showProgressDialog(getActivity(),strtip);
		
	}
	public void updateView() {
		dismissProgressDialog();
		if (result != null) {
			dataResult=dataDecode.decode(result,"ReturnTransactionMessage");						
			if (dataResult != null) {
				DataResult realData = (DataResult) dataResult;
				if (realData.getResultcode().equals("1")) {
				   ReturnTransactionMessage msg = (ReturnTransactionMessage)realData.getResult().get(0);
				   if (CurrentAction == currentNotiName) {
							if (msg.getResult().equals("1")) {
								if(upLoadType==1){
									new AlertDialog.Builder(getActivity())
						  			.setTitle("提示")
						  			.setMessage(msg.tip)
						  			.setPositiveButton("确定",new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {		
											ISqlHelper iSqlHelper = new SqliteHelper(null,mContext);
											LocalCollectData lcd = new LocalCollectData();
											lcd.WorkID = WorkID;
											lcd.ProductModel = ProductType;
											lcd.SubProductID = SubProductID;
											lcd.InteriorNum1 = InteriorNum1;
											lcd.InteriorNum2 = InteriorNum2;
											lcd.ExternalNum = ExternalNum;
											lcd.ExternalPicPath = ExternalPicPath;
											lcd.InteriorPicPath1 = InteriorPicPath1;
											lcd.InteriorPicPath2 = InteriorPicPath2;
											lcd.IsFinishedCollectFlag="1";
											lcd.MachineType = MachineType;
											iSqlHelper.Insert(lcd);	
											Intent intent = new Intent(mContext,DataCollectionMainActivity.class);
											Bundle bundle = new Bundle();
											bundle.putInt("Position",position);
											intent.putExtras(bundle);
											getActivity().setResult(10, intent);
											getActivity().finish();									
										}
									}).setCancelable(false).show();
								    return;
								}
							    if(upLoadType==2){
									upLoadType=1;
							    	BarCodeType = "2";
							    	BarCode = ExternalNum;
							    	NetWork(ExternalPicPath,currentNotiName,"外机码图片上传中，请稍候...");
								}
								if(upLoadType==3){
									upLoadType=2;
							    	BarCodeType = "3";
							    	BarCode = InteriorNum2;
							    	NetWork(InteriorPicPath2,currentNotiName,"内机码2图片上传中，请稍候...");
								}	
							}else{
								new AlertDialog.Builder(getActivity())
					  			.setTitle("提示")
					  			.setMessage(msg.tip)
					  			.setPositiveButton("确定",new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,int which) {											
									    return;	
									}
								}).setCancelable(false).show();										
							}
				     }
					}else{
						   DefaultTip(getActivity(),"暂无数据");	
					}
				}else{
					DefaultTip(getActivity(),"数据解析失败");	
			  }  
	    }else{
			DefaultTip(getActivity(),"网络数据获取失败");			
		}
	}
	public void photo(int flag) {
		Intent openCameraIntent = new Intent();
		openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		name = "IMG_"
				+ DateFormat.format("yyyyMMdd_hhmmss",
						Calendar.getInstance(Locale.CHINA)) + ".JPEG";
		path = "/sdcard/DCIM/Camera/" + name;
		file = new File(path);
		uri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 设置拍照的照片存储在哪个位置。
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Toast.makeText(getActivity(), "SD卡 错误!", Toast.LENGTH_SHORT).show();
			return;
		}
		startActivityForResult(openCameraIntent, flag);
	}
	  /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     * 
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     if (resultCode == 100) {
	            Message message = new Message();
	            message.obj = data.getStringExtra("result");
	    		message.what = requestCode;
	    		handler.sendMessage(message);
	      } 
	    if(requestCode==11&&resultCode!=1000&&resultCode!=0){
            IsPhoto1=true;
	    	updateBitmapList(11);
	    	return;
	    }
	    if(requestCode==22&&resultCode!=1000&&resultCode!=0){
	    	IsPhoto2=true;
	    	updateBitmapList(22);
	    	return;
	    }
	    if(requestCode==33&&resultCode!=1000&&resultCode!=0){
	    	IsPhoto3=true;
	    	updateBitmapList(33);
	    	return;
	    	
	    }
		if(resultCode==1000){
			int ID = data.getIntExtra("ID", 0);
			if(ID==11){
				IsPhoto1=false;
				iv_Interior_1.setImageResource(R.drawable.photograph);
			}
			if(ID==22){
				IsPhoto2=false;		
				iv_External.setImageResource(R.drawable.photograph);
			}
			if(ID==33){
				IsPhoto3=false;
				iv_Interior_2.setImageResource(R.drawable.photograph);
			}
		}
	}

	public void updateBitmapList(int id) {
		getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
		if (Bimp.tempSelectBitmap.size() < 4) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(getActivity().getContentResolver()
						.openInputStream(uri), null, options);
				options.inSampleSize = 4;
				options.inJustDecodeBounds = false;
				Bitmap photo = BitmapFactory.decodeStream(getActivity()
						.getContentResolver().openInputStream(uri), null,options);
				Bitmap temp = createBitmap(photo);
		        saveNewBitmap(name,path,temp);
				ImageItem takePhoto = new ImageItem();
				takePhoto.setImageId(id);
				takePhoto.setBitmap(temp);
				takePhoto.setImagePath(path);
				takePhoto.setName(name);
				Bimp.tempSelectBitmap.add(takePhoto);
				Message message = new Message();
				message.what = id;
				handler.sendMessage(message);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
    private void saveNewBitmap(String _name, String _path,Bitmap _bitmap) {
    	File file = new File(_name);
    	file.delete();
		File f  = new File(_path);
	    FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
			_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			 fOut.flush();
			 fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	// 给图片添加水印  
    private Bitmap createBitmap(Bitmap src) {  
        int w = src.getWidth();  
        int h = src.getHeight();  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式 
        String mstrTime = df.format(new Date());
        String mstrSpot = getActivity().getIntent().getStringExtra("LocationSpot"); 
        int num = mstrSpot.length()/12 ;
        if((mstrSpot.length()%12)!=0){
        	num =num+1;
        }
        String spots []  = new String[num];
        for(int i=0;i<num;i++){
        	if((i*12+i)<mstrSpot.length()){
        		if((i*12+12+i)<mstrSpot.length()){
        			spots[i]=mstrSpot.substring(i*12+i,i*12+12+i);
        		}else{
        			spots[i]=mstrSpot.substring(i*12+i,mstrSpot.length());
        		}
        	}
        	
        }
        Bitmap bmpTemp = Bitmap.createBitmap(w, h, Config.ARGB_8888);  
        Canvas canvas = new Canvas(bmpTemp);  
        Paint p = new Paint();  
        String familyName = "宋体";  
        Typeface font = Typeface.create(familyName, Typeface.BOLD);  
        p.setColor(Color.BLUE);  
        p.setTypeface(font);  
        p.setTextSize(30);  
        canvas.drawBitmap(src, 0, 0, p);  
        canvas.drawText(mstrTime, 20, h-10, p);  
        for(int j=num-1;j>=0;j--){
        	 canvas.drawText(spots[j], 20, h-50-(num-j-1)*40, p);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);  
        canvas.restore();  
        return bmpTemp;  
    } 
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			   case 1:
				   tv_Interior_1.setVisibility(View.VISIBLE);
				   tv_Interior_1.setText((String)msg.obj);
				   break;
			   case 2:
				   tv_External.setVisibility(View.VISIBLE);
				   tv_External.setText((String)msg.obj);
				   break;			   
			   case 3:
				   tv_Interior_2.setVisibility(View.VISIBLE);
				   tv_Interior_2.setText((String)msg.obj);
				   break;
			case 11:
				for (ImageItem li : Bimp.tempSelectBitmap) {
					if (li.imageId == 11) {
						iv_Interior_1.setDrawingCacheEnabled(true);
						iv_Interior_1.setImageBitmap(li.getBitmap());						
					}
				}				
				break;
			case 22:
				for (ImageItem li : Bimp.tempSelectBitmap) {
					if (li.imageId ==22) {
						iv_External.setDrawingCacheEnabled(false);
						iv_External.setImageBitmap(li.getBitmap());
					}
				}				
				break;
			case 33:
				for (ImageItem li : Bimp.tempSelectBitmap) {
					if (li.imageId == 33) {
						iv_Interior_2.setDrawingCacheEnabled(false);
						iv_Interior_2.setImageBitmap(li.getBitmap());					
					}
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.btn1) {
			    MachineType = "1";
			    btn1.setFocusable(false);
			    btn2.setFocusable(true);
			    btn3.setFocusable(true);
			    btn4.setFocusable(true);
			    btn5.setFocusable(true);
				tv_Interior_1.setText("");
				tv_External.setText("");
				tv_Interior_2.setText("");
				IsPhoto1=false;IsPhoto2=false;IsPhoto3=false;
				tv_Interior_1.setVisibility(View.GONE);
				tv_External.setVisibility(View.GONE);
				tv_Interior_2.setVisibility(View.GONE);
				ll_Interior_1.setVisibility(View.VISIBLE);
				ll_External.setVisibility(View.VISIBLE);
				ll_Interior_2.setVisibility(View.GONE);
				iv_Interior_1.setImageResource(R.drawable.photograph);
				iv_External.setImageResource(R.drawable.photograph);
				iv_Interior_2.setImageResource(R.drawable.photograph);
		}
		if (checkedId == R.id.btn2) {
			    MachineType = "2";
			    btn1.setFocusable(true);
			    btn2.setFocusable(false);
			    btn3.setFocusable(true);
			    btn4.setFocusable(true);
			    btn5.setFocusable(true);
				tv_Interior_1.setText("");
				tv_External.setText("");
				tv_Interior_2.setText("");
				IsPhoto1=false;IsPhoto2=false;IsPhoto3=false;
				tv_Interior_1.setVisibility(View.GONE);
				tv_External.setVisibility(View.GONE);
				tv_Interior_2.setVisibility(View.GONE);
				ll_Interior_1.setVisibility(View.VISIBLE);
				ll_External.setVisibility(View.VISIBLE);
				ll_Interior_2.setVisibility(View.VISIBLE);
				iv_Interior_1.setImageResource(R.drawable.photograph);
				iv_External.setImageResource(R.drawable.photograph);
				iv_Interior_2.setImageResource(R.drawable.photograph);
				Bimp.tempSelectBitmap.clear();

		}
		if (checkedId == R.id.btn3) {
			    MachineType = "3";
			    btn1.setFocusable(true);
			    btn2.setFocusable(true);
			    btn3.setFocusable(false);
			    btn4.setFocusable(true);
			    btn5.setFocusable(true);
				tv_Interior_1.setText("");
				tv_External.setText("");
				tv_Interior_2.setText("");
				IsPhoto1=false;IsPhoto2=false;IsPhoto3=false;
				tv_Interior_1.setVisibility(View.GONE);
				tv_External.setVisibility(View.GONE);
				tv_Interior_2.setVisibility(View.GONE);
				ll_Interior_1.setVisibility(View.VISIBLE);
				ll_External.setVisibility(View.GONE);
				ll_Interior_2.setVisibility(View.GONE);
				iv_Interior_1.setImageResource(R.drawable.photograph);
				iv_External.setImageResource(R.drawable.photograph);
				iv_Interior_2.setImageResource(R.drawable.photograph);
				Bimp.tempSelectBitmap.clear();

		}
		if (checkedId == R.id.btn4) {
			    MachineType = "4";
			    btn1.setFocusable(true);
			    btn2.setFocusable(true);
			    btn3.setFocusable(true);
			    btn4.setFocusable(false);
			    btn5.setFocusable(true);
				tv_Interior_1.setText("");
				tv_External.setText("");
				tv_Interior_2.setText("");
				tv_Interior_1.setVisibility(View.GONE);
				tv_External.setVisibility(View.GONE);
				tv_Interior_2.setVisibility(View.GONE);
				IsPhoto1=false;IsPhoto2=false;IsPhoto3=false;
				ll_Interior_1.setVisibility(View.VISIBLE);
				ll_External.setVisibility(View.GONE);
				ll_Interior_2.setVisibility(View.GONE);
				iv_Interior_1.setImageResource(R.drawable.photograph);
				iv_External.setImageResource(R.drawable.photograph);
				iv_Interior_2.setImageResource(R.drawable.photograph);
				Bimp.tempSelectBitmap.clear();
				
		}
		if (checkedId == R.id.btn5) {
			    MachineType = "5";
			    btn1.setFocusable(true);
			    btn2.setFocusable(true);
			    btn3.setFocusable(true);
			    btn4.setFocusable(true);
			    btn5.setFocusable(false);
				tv_Interior_1.setText("");
				tv_External.setText("");
				tv_Interior_2.setText("");
				tv_Interior_1.setVisibility(View.GONE);
				tv_External.setVisibility(View.GONE);
				tv_Interior_2.setVisibility(View.GONE);
				IsPhoto1=false;IsPhoto2=false;IsPhoto3=false;
				ll_Interior_1.setVisibility(View.GONE);
				ll_External.setVisibility(View.VISIBLE);
				ll_Interior_2.setVisibility(View.GONE);
				iv_Interior_1.setImageResource(R.drawable.photograph);
				iv_External.setImageResource(R.drawable.photograph);
				iv_Interior_2.setImageResource(R.drawable.photograph);
				Bimp.tempSelectBitmap.clear();
		}

	}

}
