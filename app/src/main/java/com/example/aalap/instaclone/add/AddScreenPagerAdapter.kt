package com.example.aalap.instaclone.add

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class AddScreenPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    var fragments = mutableListOf<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(fragMent: Fragment) {
        fragments.add(fragMent)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            1 -> return "Camera"
            0 -> return "Gallery"
        }
        return super.getPageTitle(position)
    }
}