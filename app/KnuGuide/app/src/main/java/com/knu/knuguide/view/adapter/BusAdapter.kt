package com.knu.knuguide.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.data.KNUData
import com.knu.knuguide.data.bus.BusStop
import com.knu.knuguide.support.KNUAdapterListener
import kotlinx.android.synthetic.main.item_bus_arrival.view.*

class BusAdapter(
    private val context: Context,
    private val listener: KNUAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<KNUData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            KNUData.Type.ITEM_BUS_STOP -> {
                val view = inflater.inflate(R.layout.item_bus_arrival, parent, false)
                BusViewHolder(view)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (item.getRecyclerType()) {
            KNUData.Type.ITEM_BUS_STOP -> {
                val busStop = item as BusStop
                holder.itemView.apply {
                    tv_bus_stop_name.text = busStop.busStopName
                    tv_bus_stop_direction.text = busStop.busStopDirection
                    tv_bus_number.text = "${busStop.busNumber}"

                    if (!busStop.isFetched) {
                        progressBar.visibility = View.VISIBLE
                        ct_arrival_time.visibility = View.GONE
                    }
                    else {
                        progressBar.visibility = View.GONE
                        ct_arrival_time.visibility = View.VISIBLE
                    }

                    tv_bus_arrival_first_time.text = if (busStop.busList.isNotEmpty()) busStop.busList[0].getArrTimeToString() else "도착 정보 없음"
                    tv_bus_arrival_first_cnt.text = if (busStop.busList.isNotEmpty()) busStop.busList[0].getArrPrevStationCntToString() else ""
                    tv_bus_arrival_second_time.text = if (busStop.busList.size > 1) busStop.busList[1].getArrTimeToString() else ""
                    tv_bus_arrival_second_cnt.text = if (busStop.busList.size > 1) busStop.busList[1].getArrPrevStationCntToString() else ""

                    setOnClickListener { listener.onBusStopClicked(busStop) }
                }
            }
        }
    }

    fun addBusStopList(busStopList: List<KNUData>) {
        items.addAll(busStopList)
        notifyDataSetChanged()
    }

    fun getItemPosition(busStop: BusStop): Int = items.indexOf(busStop)

    override fun getItemViewType(position: Int): Int = items[position].getRecyclerType()

    override fun getItemCount(): Int = items.size

    inner class BusViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}