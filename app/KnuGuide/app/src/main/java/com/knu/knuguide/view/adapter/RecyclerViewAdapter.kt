package com.knu.knuguide.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.cafeteria.Cafeteria
import kotlinx.android.synthetic.main.item_cafeteria.view.*

class RecyclerViewAdapter(
    private val context: Context,
    private var items: ArrayList<KNUData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_cafeteria, parent, false)
        return CafeteriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        bindCafeteria(item as Cafeteria, holder as CafeteriaViewHolder)
    }

    class CafeteriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun bindCafeteria(cafeteria: Cafeteria, holder: CafeteriaViewHolder){
        holder.itemView.todayFood.text = cafeteria.lunch
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getRecyclerType()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
