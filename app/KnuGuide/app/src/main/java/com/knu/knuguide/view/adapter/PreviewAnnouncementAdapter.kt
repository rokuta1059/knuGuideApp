package com.knu.knuguide.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.announcement.Announcement
import kotlinx.android.synthetic.main.item_announcement.view.*

class PreviewAnnouncementAdapter(val context: Context, val items: ArrayList<KNUData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun bindAnnouncement(announcement: Announcement, holder: AnnouncementViewHolder) {
        holder.itemView.source.text = announcement.source
        holder.itemView.itemtitle.text = announcement.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        var holder: RecyclerView.ViewHolder? = null
        when (viewType) {
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
            KNUData.Type.ITEM_ANNOUNCEMENT -> {
                bindAnnouncement(item as Announcement, holder as AnnouncementViewHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getRecyclerType()
    }

    override fun getItemCount(): Int { return items.size }
}