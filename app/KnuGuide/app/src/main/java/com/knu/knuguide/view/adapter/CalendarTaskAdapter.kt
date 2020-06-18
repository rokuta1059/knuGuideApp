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
import com.knu.knuguide.data.calendar.Task
import com.knu.knuguide.support.KNUAdapterListener
import kotlinx.android.synthetic.main.item_task.view.*

class CalendarTaskAdapter(
    private val context: Context,
    private var items: ArrayList<KNUData>,
    private val listener: KNUAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_task, parent, false)

        return CalendarTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        bindCalendarTaskHolder(holder as CalendarTaskViewHolder, item as Task, position)
    }

    private fun bindCalendarTaskHolder(holder: CalendarTaskViewHolder, task: Task, position: Int) {
        holder.itemView.tv_dateRanges.text = task.getDateString()
        holder.itemView.tv_content.text = task.content

        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.item_task_background_select))
            holder.itemView.tv_dateRanges.setTextColor(ContextCompat.getColor(context, R.color.item_task_text_select))
            holder.itemView.tv_content.setTextColor(ContextCompat.getColor(context, R.color.item_task_text_select))
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.item_task_background_default))
            holder.itemView.tv_dateRanges.setTextColor(ContextCompat.getColor(context, R.color.item_task_text_default))
            holder.itemView.tv_content.setTextColor(ContextCompat.getColor(context, R.color.item_task_text_default))
        }

        holder.itemView.setOnClickListener {
            // Clickable 하다면
            if (listener.onCalendarTaskItemClick(task, selectedPosition == position)) {
                // RecyclerView Holder 업데이트
                notifyItemChanged(selectedPosition)
                selectedPosition = if (selectedPosition == position) -1 else position
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getRecyclerType()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun unselect() {
        selectedPosition = -1
    }

    fun setItems(items: ArrayList<KNUData>) {
        this.items = items
    }

    class CalendarTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}