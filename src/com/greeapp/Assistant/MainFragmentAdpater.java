package com.greeapp.Assistant;

import java.util.List;

import com.greeapp.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainFragmentAdpater implements OnCheckedChangeListener {
	
    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private RadioGroup rgs; // 用于切换tab
    private FragmentActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id

    private int currentTab = 0; // 当前Tab页面索引

    public MainFragmentAdpater(FragmentActivity fragmentActivity,
                     List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
      /*  getSupportFragmentManager()获取一个FragmentManager
        FragmentTransaction对fragment进行添加,移除,替换,以及执行其他动作。
        从 FragmentManager 获得一个FragmentTransaction的实例 :*/
       
       
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();
        this.rgs.setOnCheckedChangeListener(this);

    }

    /**
     * 监听radiobutton改变时间，当选择了不同的radiobutton会出发这个函数
     */
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < rgs.getChildCount(); i++) {
            if (rgs.getChildAt(i).getId() == checkedId) {
                Fragment fragment = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);
                getCurrentFragment().onPause(); // 暂停当前tab
                if (fragment.isAdded()) {
                    fragment.onResume(); // 启动目标tab的onResume()
                } else {
                    ft.add(fragmentContentId, fragment);
                }
                showTab(i); // 显示目标tab
                ft.commit();


            }
        }
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab

    }

    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        // 设置切换动画
        if (index > currentTab) {

            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.slide_right_in,
                    R.anim.slide_right_out);
        }
        return ft;
    }

}
