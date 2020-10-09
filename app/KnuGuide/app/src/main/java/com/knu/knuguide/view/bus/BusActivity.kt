package com.knu.knuguide.view.bus

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.bus.Bus
import com.knu.knuguide.data.bus.BusStop
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.BusAdapter
import com.knu.knuguide.view.adapter.decor.BusAdapterDecor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.activity_bus.rootLayout

class BusActivity : KNUActivity(), KNUAdapterListener {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var busAdapter: BusAdapter

    private val busStops = mutableMapOf<String, BusStop>()

    private val handler = Handler()

    private val fetchTask = object : Runnable {
        override fun run() {
            fetchAllArrivalBusList()
            handler.postDelayed(this, 30000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

        setUpRecyclerView()
        setUpItems()

        fab_refresh.setOnClickListener { fetchAllArrivalBusList() }
    }

    override fun onResume() {
        super.onResume()
        handler.post(fetchTask)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(fetchTask)
    }

    private fun setUpRecyclerView() {
        busAdapter = BusAdapter(this, this)

        rv_bus_stop.layoutManager = LinearLayoutManager(this)
        rv_bus_stop.adapter = busAdapter
        rv_bus_stop.addItemDecoration(BusAdapterDecor(this, 16f, 0f))
    }

    private fun setUpItems() {
        busStops["CCB250026903"] = BusStop("강원대중앙도서관", "강원대백록관 방면", "CCB250026903", 300)
        busStops["CCB250026904"] = BusStop("강원대중앙도서관", "강원대후문 방면", "CCB250026904", 300)
        busStops["CCB250026905"] = BusStop("강원대백록관", "강원대정문 방면", "CCB250026905", 300)
        busStops["CCB250026906"] = BusStop("강원대백록관", "강원대중앙도서관 방면", "CCB250026906", 300)

        busAdapter.addBusStopList(busStops.values.toList())
    }

    private fun fetchAllArrivalBusList() {
        busStops.keys.forEach {
            val busStop = busStops[it]!!
            busStop.isFetched = false

            compositeDisposable.add(KNUService.instance()!!.getSpecifyArrivalBusList(nodeId = busStop.nodeId).subscribeWith(object : DisposableSingleObserver<Bus>() {
                override fun onSuccess(bus: Bus) {
                    if (bus.body != null) {
                        busStop.busList = bus.body!!.items
                        busStop.isFetched = true

                        busAdapter.notifyItemChanged(busAdapter.getItemPosition(busStop))
                    }
                }

                override fun onError(e: Throwable) {
                    Snackbar.make(rootLayout, "네트워크 에러", Snackbar.LENGTH_SHORT).show()
                    Log.d("Error", e.message)
                }
            }))
        }
        busAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onBusStopClicked(item: BusStop) {
        val bundle = Bundle().apply {
            putInt(BusInfoActivity.KEY_BUS_NUMBER, item.busNumber)
            putString(BusInfoActivity.KEY_BUS_ROUTE, item.nodeId)
        }
        navigateTo(BusInfoActivity::class.java, bundle)
    }

    override fun getKNUID(): String = KNU_ID

    companion object {
        const val KNU_ID = "BusActivity"
    }
}