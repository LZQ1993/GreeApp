package com.greeapp.Infrastructure.CWFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.greeapp.R;

public class TitleFragment extends Fragment { 
	 
	private Context _content;
	private String Title;
	private Boolean leftVisibility,rightVisibility;
    private ImageButton mLeftMenu,mRightMenu; 
    private Button nav_bar_tv_title;
    private int id;
    public TitleFragment(){
    	super();
    }
    public TitleFragment(Context _content){
    	super();
    }
    public TitleFragment(Context _content, String title,Boolean leftVisibility, Boolean rightVisibility,int id) {
		super();
		this._content = _content;
		Title = title;
		rightVisibility = rightVisibility;
		this.leftVisibility = leftVisibility;
		this.id = id;
	}

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  
    {  
        View view = inflater.inflate(R.layout.title_navbar, container, false);  
        mLeftMenu = (ImageButton) view.findViewById(R.id.nav_bar_btn_left); 
        
        if(leftVisibility){
        	mLeftMenu.setVisibility(View.VISIBLE);
        	 mLeftMenu.setOnClickListener(new OnClickListener()  
             {  
                 @Override  
                 public void onClick(View v)  
                 {  
                    getActivity().finish();
                 }  
             });  
        }else{
        	mLeftMenu.setVisibility(View.INVISIBLE);
        }
        nav_bar_tv_title = (Button) view.findViewById(R.id.nav_bar_tv_title);
        if(Title==null){
       
        	nav_bar_tv_title.setVisibility(View.INVISIBLE);
        }else{
        	nav_bar_tv_title.setText(Title);
        	nav_bar_tv_title.setVisibility(View.VISIBLE);
        }
        mRightMenu = (ImageButton) view.findViewById(R.id.nav_bar_btn_right);  
        if(rightVisibility){
        	mRightMenu.setVisibility(View.VISIBLE);
        	 mRightMenu.setBackground(getResources().getDrawable(id));
        }else{
        	mRightMenu.setVisibility(View.INVISIBLE);
        }
       
        return view;  
    } 
	
}  

