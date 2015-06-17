package com.chzu.getnews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;




public class TabAdapter extends FragmentPagerAdapter{

	public static final String[] TITLES = new String[] {"蔚园要问","院部动态","通知公告","教科研信息"};
	
	public TabAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		MainFragment fragment = new MainFragment(arg0+1);
		return fragment;
	}

	@Override
	public int getCount() {
		
		return TITLES.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position % TITLES.length];
	}
	
	
}
