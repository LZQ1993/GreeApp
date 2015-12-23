package com.greeapp.Assistant;

import java.util.List;

import com.greeapp.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainFragmentAdpater implements OnCheckedChangeListener {
	
    private List<Fragment> fragments; // һ��tabҳ���Ӧһ��Fragment
    private RadioGroup rgs; // �����л�tab
    private FragmentActivity fragmentActivity; // Fragment������Activity
    private int fragmentContentId; // Activity����Ҫ���滻�������id

    private int currentTab = 0; // ��ǰTabҳ������

    public MainFragmentAdpater(FragmentActivity fragmentActivity,
                     List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // Ĭ����ʾ��һҳ
      /*  getSupportFragmentManager()��ȡһ��FragmentManager
        FragmentTransaction��fragment�������,�Ƴ�,�滻,�Լ�ִ������������
        �� FragmentManager ���һ��FragmentTransaction��ʵ�� :*/
       
       
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();
        this.rgs.setOnCheckedChangeListener(this);

    }

    /**
     * ����radiobutton�ı�ʱ�䣬��ѡ���˲�ͬ��radiobutton������������
     */
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < rgs.getChildCount(); i++) {
            if (rgs.getChildAt(i).getId() == checkedId) {
                Fragment fragment = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);
                getCurrentFragment().onPause(); // ��ͣ��ǰtab
                if (fragment.isAdded()) {
                    fragment.onResume(); // ����Ŀ��tab��onResume()
                } else {
                    ft.add(fragmentContentId, fragment);
                }
                showTab(i); // ��ʾĿ��tab
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
        currentTab = idx; // ����Ŀ��tabΪ��ǰtab

    }

    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        // �����л�����
        if (index > currentTab) {

            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.slide_right_in,
                    R.anim.slide_right_out);
        }
        return ft;
    }

}
