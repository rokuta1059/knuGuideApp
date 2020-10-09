package com.knu.knuguide.view.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.bus.RouteBusCount
import com.knu.knuguide.data.bus.RouteBusItem
import com.knu.knuguide.data.bus.RouteInfoItem
import com.knu.knuguide.data.bus.RouteItem
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.support.Utils
import kotlinx.android.synthetic.main.item_bus_station.view.*
import kotlinx.android.synthetic.main.item_bus_station_header.view.*
import kotlinx.android.synthetic.main.item_bus_station_interval.view.*
import kotlin.math.cos

class BusRouteAdapter(
    private val context: Context,
    private val listener: KNUAdapterListener,
    private val routeId: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<KNUData> = mutableListOf()

    private val CELL_HEIGHT = Utils.dp2px(88f) - Utils.dp2px(24f)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            KNUData.Type.ITEM_BUS_ROUTE_INTERVAL -> {
                val view = inflater.inflate(R.layout.item_bus_station_interval, parent, false)
                BusRouteIntervalViewHolder(view)
            }
            KNUData.Type.ITEM_BUS_ROUTE_HEADER -> {
                val view = inflater.inflate(R.layout.item_bus_station_header, parent, false)
                BusRouteHeaderViewHolder(view)
            }
            KNUData.Type.ITEM_BUS_ROUTE -> {
                val view = inflater.inflate(R.layout.item_bus_station, parent, false)
                BusRouteViewHolder(view)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (item.getRecyclerType()) {
            KNUData.Type.ITEM_BUS_ROUTE -> {
                val route = item as RouteItem

                holder.itemView.apply {
                    tv_bus_station_name.text = route.nodeName
                    tv_bus_station_number.text = "${route.nodeNumber}"

                    path_sticker_top.visibility = if (route.nodeOrder == 1) View.INVISIBLE else View.VISIBLE
                    path_sticker_bottom.visibility = if (route.nodeOrder == items.size - 2) View.INVISIBLE else View.VISIBLE
                    ct_bus_sticker.visibility = if (route.hasSticker) View.VISIBLE else View.GONE

                    if (route.hasSticker) {

                        if (position + 1 < items.size) {
                            val nextRoute = items[position + 1] as RouteItem

                            val stkDist = Utils.distance(route.gpsLatitude, route.gpsLongitude, route.stkGpsLatitude, route.stkGpsLongitude)
                            val nextDist = Utils.distance(route.gpsLatitude, route.gpsLongitude, nextRoute.gpsLatitude, nextRoute.gpsLongitude)

                            (ct_bus_sticker.layoutParams as ConstraintLayout.LayoutParams).topMargin = (stkDist / nextDist * CELL_HEIGHT).toInt()
                            Log.d("DEBUG", "거리 : $stkDist $nextDist ${stkDist / nextDist * CELL_HEIGHT}")
                        }
                        tv_bus_authority.text = route.getViechleNumberLastDigit()
                    }

                    ct_node.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))

                    if (!routeId.isNullOrEmpty()) {
                        if (route.nodeId == routeId) ct_node.setBackgroundColor(ContextCompat.getColor(context, R.color.item_bus_node_accent))
                    }
                }
            }
            KNUData.Type.ITEM_BUS_ROUTE_HEADER -> {
                (holder as BusRouteHeaderViewHolder).bind(item)
            }
            KNUData.Type.ITEM_BUS_ROUTE_INTERVAL -> {
                val interval = item as RouteInfoItem

                holder.itemView.apply {
                    bus_routes_interval.text = "${interval.startNodeName} ↔ ${interval.endNodeName}"
                }
            }
        }
    }

    fun add(item: KNUData) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun addRouteList(routeList: List<KNUData>) {
        var scrollTo = -1
        routeList.forEachIndexed { idx, it ->
            items.add(it)
            if ( (it as RouteItem).nodeId == routeId ) scrollTo = idx + 2
        }
        notifyDataSetChanged()

        if (scrollTo > -1) listener.scrollToNodePosition(scrollTo)
    }

    fun setInterval(item: RouteInfoItem) {
        (items[0] as RouteInfoItem).apply {
            startNodeName = item.startNodeName
            endNodeName = item.endNodeName
        }
        notifyItemRangeChanged(0, 1)
    }

    fun setHeaderValue(totalCount: Int) {
        if (items[1].getRecyclerType() == KNUData.Type.ITEM_BUS_ROUTE_HEADER) {
            (items[1] as RouteBusCount).apply { this.totalCount = totalCount }
            notifyItemRangeChanged(1, 1)
        }
    }

    fun updateBusSticker(list: MutableList<RouteBusItem>) {
        items.forEach {
            if (it.getRecyclerType() == KNUData.Type.ITEM_BUS_ROUTE) {
                (it as RouteItem).hasSticker = false
            }
        }

        list.forEach {
            if (it.nodeOrder + 1 < items.size) {
                val item = items[it.nodeOrder + 1]

                if (item.getRecyclerType() == KNUData.Type.ITEM_BUS_ROUTE) {
                    (item as RouteItem).apply {
                        hasSticker = true
                        stkGpsLatitude = it.gpsLatitude
                        stkGpsLongitude = it.gpsLongitude
                        vehicleNo = it.viechleNo
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun isNode(position: Int): Boolean = getItemViewType(position) == KNUData.Type.ITEM_BUS_ROUTE

    fun isStickyHeader(position: Int): Boolean = getItemViewType(position) == KNUData.Type.ITEM_BUS_ROUTE_HEADER

    fun getHeaderLayoutView(recyclerView: RecyclerView, position: Int): View? {
        val lastIndex = if (position < items.size) position else items.size - 1
        for (index in lastIndex downTo 0) {
            val model = items[index]
            if (model.getRecyclerType() == KNUData.Type.ITEM_BUS_ROUTE_HEADER) {
                return LayoutInflater.from(context)
                    .inflate(R.layout.item_bus_station_header, recyclerView, false).apply { BusRouteHeaderViewHolder(this).bind(model) }
            }
        }
        return null
    }

    override fun getItemViewType(position: Int): Int = items[position].getRecyclerType()

    override fun getItemCount(): Int = items.size

    inner class BusRouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class BusRouteHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: KNUData) {
            val busCount = item as RouteBusCount
            val spannableString = SpannableString("현재 ${busCount.totalCount}대 운행중")
                .apply {
                    setSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(
                                context,
                                R.color.appBackground
                            )
                        ),
                        3, 4 + "${busCount.totalCount}".length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            itemView.apply {
                tv_curr_activated_bus.text = if (busCount.totalCount > -1) spannableString else ""
                iv_bus.isVisible = busCount.totalCount > -1
            }
        }
    }
    inner class BusRouteIntervalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}