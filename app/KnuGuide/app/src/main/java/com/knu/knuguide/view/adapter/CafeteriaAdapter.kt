package com.knu.knuguide.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.knu.knuguide.view.fragment.CafeteriaFragment

class CafeteriaAdapter(val fmActivity: FragmentActivity): FragmentStateAdapter(fmActivity){
    override fun getItemCount(): Int {
        return 3;
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CafeteriaFragment(position)
            1 -> CafeteriaFragment(position)
            else -> CafeteriaFragment(position)
        }
    }
}