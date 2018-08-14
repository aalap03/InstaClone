package com.example.aalap.instaclone.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.aalap.instaclone.Preference
import com.example.aalap.instaclone.R
import kotlinx.android.synthetic.main.layout_center_home_fragment.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HomeCenterFragment : Fragment() , AnkoLogger{

    lateinit var pref : Preference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.layout_center_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = Preference(requireContext().applicationContext)
        home_user_name.text = pref.getUserName()
        info { pref.getUserId() }

        Glide.with(this)
                .load(pref.getProfilePic())
                .into(home_profile_pic)
    }

}