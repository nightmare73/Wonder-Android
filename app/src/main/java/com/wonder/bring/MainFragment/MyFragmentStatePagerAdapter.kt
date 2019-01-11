package com.wonder.bring.MainFragment

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.wonder.bring.db.SharedPreferenceController


/**
 * Created by Kyuhee on 2018-02-05.
 */


class MyFragmentStatePagerAdapter(fm: FragmentManager, // Count number of tabs
                                  private val tabCount: Int) : android.support.v4.app.FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment? {
        // Returning the current tabs


        when (position) {           // 자바로 치면, switch문법

            0 -> return HomeFragment()
            1 -> return OrderhistoryFragment.getInstance()
            2 -> return CartFragment.getInstance()
            3 -> return MypageFragment.getInstance()
            else -> return null

        }

    }

    override fun getCount(): Int =tabCount
}


