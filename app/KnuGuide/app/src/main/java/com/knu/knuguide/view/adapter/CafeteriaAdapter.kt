package com.knu.knuguide.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.knu.knuguide.view.fragment.IrumFragment
import com.knu.knuguide.view.fragment.JaejeongFragment
import com.knu.knuguide.view.fragment.SaeromFragment

class CafeteriaAdapter(fmActivity: FragmentActivity): FragmentStateAdapter(fmActivity){
    override fun getItemCount(): Int {
        return 3;
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1 -> JaejeongFragment()
            2 -> SaeromFragment()
            else -> IrumFragment()
        }
    }
}