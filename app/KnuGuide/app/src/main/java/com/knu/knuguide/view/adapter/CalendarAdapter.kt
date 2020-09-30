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
import com.knu.knuguide.data.calendar.DayPosition
import com.knu.knuguide.data.calendar.DayType
import com.knu.knuguide.data.calendar.KNUDay
import com.knu.knuguide.support.KNUAdapterListener
import kotlinx.android.synthetic.main.item_day.view.*
import kotlinx.android.synthetic.main.knu_appbar_collapse.view.*

class CalendarAdapter(
    private val context: Context,
    private var items: ArrayList<KNUData>,
    private val listener: KNUAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        lateinit var holder: RecyclerView.ViewHolder

        when (viewType) {
            KNUData.Type.ITEM_DAY_EMPTY -> {
                val view = inflater.inflate(R.layout.item_day_empty, parent, false)
                holder = EmptyDayViewHolder(view)
            }
            KNUData.Type.ITEM_DAY -> {
                val view = inflater.inflate(R.layout.item_day, parent, false)
                holder = DayViewHolder(view)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item.getRecyclerType()) {
            KNUData.Type.ITEM_DAY_EMPTY -> bind()
            KNUData.Type.ITEM_DAY -> bindDayViewHolder(holder as DayViewHolder, item as KNUDay)
        }
    }

    private fun bind() {}

    private fun bindDayViewHolder(holder: DayViewHolder, day: KNUDay) {
        holder.itemView.tv_day.text = day.day.toString()

        /* 배경 초기화 */
        holder.itemView.tv_day.background = null
        holder.itemView.container.background = null

        when (day.dayType) {
            DayType.NONE -> {
                holder.itemView.tv_day.setTextColor(
                    if (!day.isWeekEnd) ContextCompat.getColor(context, R.color.item_day_text_default_weekday)
                    else ContextCompat.getColor(context, R.color.item_day_text_default_weekend)
                )
            }
            DayType.SINGLE -> {
                holder.itemView.tv_day.background = ContextCompat.getDrawable(context, R.drawable.day_single)
                holder.itemView.tv_day.setTextColor(ContextCompat.getColor(context, R.color.item_day_text_includeInSchedule_weekday))
            }
            DayType.DURATION -> {
                when (day.dayPos) {
                    DayPosition.START -> {
                        holder.itemView.container.background = ContextCompat.getDrawable(context, R.drawable.day_duration_start)
                        holder.itemView.tv_day.setTextColor(ContextCompat.getColor(context, R.color.item_day_text_includeInSchedule_weekday))
                    }
                    DayPosition.END -> {
                        holder.itemView.container.background = ContextCompat.getDrawable(context, R.drawable.day_duration_end)
                        holder.itemView.tv_day.setTextColor(ContextCompat.getColor(context, R.color.item_day_text_includeInSchedule_weekday))
                    }
                    DayPosition.IN -> {
                        holder.itemView.container.setBackgroundColor(ContextCompat.getColor(context, R.color.item_day_background_includeInSchedule))
                        holder.itemView.tv_day.setTextColor(ContextCompat.getColor(context, R.color.item_day_text_includeInSchedule_weekday))
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getRecyclerType()
    }

    override fun getItemCount(): Int { return items.size }

    fun getItems(): ArrayList<KNUData> {
        return items
    }

    fun setItems(items: ArrayList<KNUData>) {
        this.items = items
    }

    // 빈 공간 ViewHolder
    class EmptyDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    // 일 (Day) ViewHolder
    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}