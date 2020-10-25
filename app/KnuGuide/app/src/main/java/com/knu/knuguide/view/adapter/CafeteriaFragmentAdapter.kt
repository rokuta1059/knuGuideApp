package com.knu.knuguide.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.knu.knuguide.view.fragment.CafeteriaFragment

class CafeteriaFragmentAdapter(private val fmActivity: FragmentActivity): FragmentStateAdapter(fmActivity) {

    private val tabTextList = arrayListOf("재정", "새롬관", "이룸관")

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CafeteriaFragment(position, tabTextList[position])
            1 -> CafeteriaFragment(position, tabTextList[position])
            else -> CafeteriaFragment(position, tabTextList[position])
        }
    }

    fun getTitle(position: Int): String = tabTextList[position]
}