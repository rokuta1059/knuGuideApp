package com.knu.knuguide.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.calendar.KNUDay
import kotlinx.android.synthetic.main.item_day.view.*

class CalendarAdapter(val context: Context, items: ArrayList<KNUData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<KNUData> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        var holder: RecyclerView.ViewHolder? = null
        when (viewType) {
            KNUData.Type.ITEM_DAY_EMPTY -> {
                val view = inflater.inflate(R.layout.item_day_empty, parent, false)
                holder = EmptyDayViewHolder(view)
            }
            KNUData.Type.ITEM_DAY -> {
                val view = inflater.inflate(R.layout.item_day, parent, false)
                holder = DayViewHolder(view, context)
            }
        }
        return holder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item.getRecyclerType()) {
            KNUData.Type.ITEM_DAY_EMPTY -> {
                (holder as EmptyDayViewHolder).bind()
            }
            KNUData.Type.ITEM_DAY -> {
                (holder as DayViewHolder).bind(item as KNUDay)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getRecyclerType()
    }

    override fun getItemCount(): Int { return items.size }

    fun setItems(items: ArrayList<KNUData>) {
        this.items = items
    }

    class EmptyDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {}
    }

    class DayViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: KNUDay) {
            itemView.tv_day.text = item.day.toString()
            if (item.isWeekEnd) {
                itemView.tv_day.setTextColor(Color.RED)
            }
            else {
                itemView.tv_day.setTextColor(Color.BLACK)
            }

            if (item.getTask().size > 0)
                itemView.tv_day.background = ContextCompat.getDrawable(context, R.drawable.border_circle)
            else
                itemView.tv_day.background = null
        }
    }
}