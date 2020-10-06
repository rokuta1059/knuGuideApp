package com.knu.knuguide.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.support.KNUAdapterListener

class BusAdapter (
    private val context: Context,
    private var busList: ArrayList<Bus>,
    private val listener: KNUAdapterListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        lateinit var holder: RecyclerView.ViewHolder

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = busList[position]

    }

    override fun getItemViewType(position: Int): Int {
        return busList[position].getRecyclerType()
    }

    override fun getItemCount(): Int {
        return busList.size
    }

    fun getItems(): ArrayList<Bus> {
        return busList
    }

    fun setItems(items: ArrayList<Bus>) {
        this.busList = items
    }

    // 빈 공간 ViewHolder
    class EmptyDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}