package com.salatiga.code.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.salatiga.code.CuacaFragment;
import com.salatiga.code.GempaFragment;

/**
 * Class Tab Adapter aplikasi
 * @author Hendro Steven Tampake
 * @version 1.0
 *
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new CuacaFragment();
		case 1:
			return new GempaFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
