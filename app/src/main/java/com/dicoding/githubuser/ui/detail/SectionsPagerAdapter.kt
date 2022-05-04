package com.dicoding.githubuser.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username : String = ""

    override fun createFragment(position: Int): Fragment {
        var fragment = FollowerFragment()
        fragment.arguments = Bundle().apply{
            putInt(FollowerFragment.ARG_SECTION_NUMBER, position+1)
            putString(FollowerFragment.ARG_USERNAME, username)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2
}