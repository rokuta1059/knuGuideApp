package com.knu.knuguide.view.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.cafeteria.Cafeteria
import com.knu.knuguide.data.cafeteria.Menu
import com.knu.knuguide.data.cafeteria.MenuType
import com.knu.knuguide.support.KNUAdapterListener
import kotlinx.android.synthetic.main.item_cafeteria.view.*

class CafeteriaAdapter(
    private val context: Context,
    private val listener: KNUAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = listOf<KNUData>(
        Menu("").apply { menuType = MenuType.BREAKFAST },
        Menu("").apply { menuType = MenuType.LUNCH },
        Menu("").apply { menuType = MenuType.DINNER })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_cafeteria, parent, false)

        return CafeteriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position] as Menu

        with(holder.itemView) {
            when (item.menuType) {
                MenuType.BREAKFAST -> {
                    tv_type.text = "아침"
                    iv_type.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_morning))

                    with(ContextCompat.getColor(context, R.color.item_cafeteria_morning)) {
                        tv_type.setTextColor(this)
                        iv_type.setColorFilter(this, PorterDuff.Mode.SRC_IN)
                    }
                }
                MenuType.LUNCH -> {
                    tv_type.text = "점심"
                    iv_type.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_afternoon))

                    with(ContextCompat.getColor(context, R.color.item_cafeteria_lunch)) {
                        tv_type.setTextColor(this)
                        iv_type.setColorFilter(this, PorterDuff.Mode.SRC_IN)
                    }
                }
                MenuType.DINNER -> {
                    tv_type.text = "저녁"
                    iv_type.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_moon))

                    with(ContextCompat.getColor(context, R.color.item_cafeteria_evening)) {
                        tv_type.setTextColor(this)
                        iv_type.setColorFilter(this, PorterDuff.Mode.SRC_IN)
                    }
                }
            }

            tv_foods.text = if (item.menus.isEmpty()) context.getString(R.string.cafeteria_is_foods_empty) else item.menus
        }
    }

    fun replace(item: List<Menu>) {
        for (i in items.indices) {
            (items[i] as Menu).apply { menus = item[i].menus }
        }

        notifyItemRangeChanged(0, items.size)
    }

    fun clear() {
        for (i in items.indices) {
            (items[i] as Menu).apply { menus = "" }
        }

        notifyItemRangeChanged(0, items.size)
    }

    override fun getItemViewType(position: Int): Int = items[position].getRecyclerType()

    override fun getItemCount(): Int = items.size

    class CafeteriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
