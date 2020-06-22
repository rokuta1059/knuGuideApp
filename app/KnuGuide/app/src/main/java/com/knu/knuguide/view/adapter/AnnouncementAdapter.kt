package com.knu.knuguide.view.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.support.KNUAdapterListener
import kotlinx.android.synthetic.main.item_announcement.view.*
import kotlinx.android.synthetic.main.item_announcement.view.title
import kotlinx.android.synthetic.main.item_announcement_preview.view.*
import kotlinx.android.synthetic.main.item_search_department.view.*

class AnnouncementAdapter(
    private val context: Context,
    private var items: ArrayList<KNUData>,
    private val listener: KNUAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        var holder: RecyclerView.ViewHolder? = null
        when (viewType) {
            KNUData.Type.ITEM_ANNOUNCEMENT_PREVIEW -> {
                val view = inflater.inflate(R.layout.item_announcement_preview, parent, false)
                holder = AnnouncementViewHolder(view)
            }
            KNUData.Type.ITEM_ANNOUNCEMENT -> {
                val view = inflater.inflate(R.layout.item_announcement, parent, false)
                holder = AnnouncementViewHolder(view)
            }
        }
        return holder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item.getRecyclerType()) {
            KNUData.Type.ITEM_ANNOUNCEMENT_PREVIEW -> {
                bindAnnouncementPreview(item as Announcement, holder as AnnouncementViewHolder)
            }
            KNUData.Type.ITEM_ANNOUNCEMENT -> {
                bindAnnouncement(item as Announcement, holder as AnnouncementViewHolder)
            }
        }
    }

    class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun bindAnnouncement(announcement: Announcement, holder: AnnouncementViewHolder) {
        holder.itemView.department.text = announcement.department
        holder.itemView.title.text = announcement.title
        holder.itemView.date.text = announcement.date

        if (announcement.isFixed()) {
            holder.itemView.v_tag.setBackgroundResource(R.color.item_announcement_favorite)
            holder.itemView.iv_department.setColorFilter(ContextCompat.getColor(context, R.color.item_announcement_favorite))
            holder.itemView.iv_date.setColorFilter(ContextCompat.getColor(context, R.color.item_announcement_favorite))
            holder.itemView.bt_view.setColorFilter(ContextCompat.getColor(context, R.color.item_announcement_favorite))
        }
        else {
            holder.itemView.v_tag.setBackgroundResource(R.color.item_announcement_not_favorite)
            holder.itemView.iv_department.setColorFilter(ContextCompat.getColor(context, R.color.item_announcement_not_favorite))
            holder.itemView.iv_date.setColorFilter(ContextCompat.getColor(context, R.color.item_announcement_not_favorite))
            holder.itemView.bt_view.setColorFilter(ContextCompat.getColor(context, R.color.item_announcement_not_favorite))
        }

        holder.itemView.setOnClickListener { listener.onAnnouncementClick(announcement) }
        holder.itemView.bt_view.setOnClickListener { listener.onAnnouncementClick(announcement) }
    }

    private fun bindAnnouncementPreview(announcement: Announcement, holder: AnnouncementViewHolder) {
        holder.itemView.source.text = announcement.department
        holder.itemView.itemtitle.text = announcement.title

        holder.itemView.setOnClickListener {
            listener.onAnnouncementClick(announcement)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return items[position].getRecyclerType()
    }

    override fun getItemCount(): Int { return items.size }
}