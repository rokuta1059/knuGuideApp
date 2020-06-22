package com.knu.knuguide.view.adapter

import android.content.Context
import android.os.DeadObjectException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.search.Department
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.support.Utils
import kotlinx.android.synthetic.main.activity_announcement.*
import kotlinx.android.synthetic.main.item_search_department.view.*

class SearchAdapter(
    private val context: Context,
    private var items: ArrayList<KNUData>,
    private val listener: KNUAdapterListener) :  RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var searchItems = items

    private var unFavorite = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_search_department, parent, false)

        return DepartmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = searchItems[position]

        bindDepartmentHolder(holder as DepartmentViewHolder, item as Department, position)
    }

    // 검색 기능을 위한 Filter 구현
    // performFiltering : 필터링을 수행
    // publishResult : 결과 핸들
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty())
                    searchItems = items
                else {
                    val filtered = ArrayList<KNUData>()

                    for (it in items) {
                        val item = it as Department
                        if (item.department.isNullOrEmpty())
                            continue
                        else
                            if (item.department!!.contains(charString))
                                filtered.add(it)
                    }
                    searchItems = filtered
                }
                // 결과 저장
                val filterResults = FilterResults()
                filterResults.values = searchItems
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchItems = results?.values as ArrayList<KNUData>
                searchItems.sortWith(object : Comparator<KNUData> {
                    override fun compare(o1: KNUData?, o2: KNUData?): Int {
                        val d1 = o1 as Department
                        val d2 = o2 as Department

                        if (d1.isFavorite != d2.isFavorite) {
                            return if (d1.isFavorite) -1
                            else 1
                        }
                        return 0
                    }
                })
                notifyDataSetChanged()
            }
        }
    }

    private fun bindDepartmentHolder(holder: DepartmentViewHolder, item: Department, position: Int) {
        holder.itemView.title.text = item.department

        if (unFavorite)
            holder.itemView.ic_star.visibility = View.INVISIBLE

        if (item.isFavorite)
            holder.itemView.ic_star.setImageResource(R.drawable.ic_star_filled)
        else
            holder.itemView.ic_star.setImageResource(R.drawable.ic_star_unfilled)

        holder.itemView.setOnClickListener { listener.onSearchItemClick(item) }

        holder.itemView.ic_star.setOnClickListener {
            if (item.isFavorite) {
                item.isFavorite = false

                holder.itemView.ic_star.setImageResource(R.drawable.ic_star_unfilled)

                Utils.showSnackbar(holder.itemView, "${item.department} (이)가 즐겨찾기에서 삭제되었습니다.")
            }
            else {
                item.isFavorite = true

                holder.itemView.ic_star.setImageResource(R.drawable.ic_star_filled)

                Utils.showSnackbar(holder.itemView, "${item.department} (이)가 즐겨찾기에 추가되었습니다.")
            }
        }
    }

    fun releaseFavorite() {
        unFavorite = true
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return searchItems[position].getRecyclerType()
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    class DepartmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}