package com.knu.knuguide.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.calendar.KNUDay
import com.knu.knuguide.data.calendar.Task
import kotlinx.android.synthetic.main.item_task.view.*

class CalendarTaskAdapter(val context: Context, items: ArrayList<KNUData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<KNUData> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        var holder: RecyclerView.ViewHolder? = null
        when (viewType) {
            KNUData.Type.ITEM_TASK -> {
                val view = inflater.inflate(R.layout.item_task, parent, false)
                holder = CalendarTaskHolder(view, context)
            }
        }
        return holder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item.getRecyclerType()) {
            KNUData.Type.ITEM_TASK -> {
                (holder as CalendarTaskHolder).bind(item as Task)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getRecyclerType()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: ArrayList<KNUData>) {
        this.items = items
    }

    class CalendarTaskHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Task) {
            itemView.tv_dateRanges.text = item.getDateRangeText()
            itemView.tv_content.text = item.content
        }
    }
}