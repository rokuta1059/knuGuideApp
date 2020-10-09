package com.knu.knuguide.view.bus

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.bus.*
import com.knu.knuguide.support.KNUAdapterListener
import com.knu.knuguide.support.Utils
import com.knu.knuguide.view.KNUActivity
import com.knu.knuguide.view.adapter.BusRouteAdapter
import com.knu.knuguide.view.adapter.decor.BusRouteAdapterDecor
import com.knu.knuguide.view.adapter.decor.StickyHeaderDecor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_bus_node_info.*
import kotlinx.android.synthetic.main.knu_appbar.*

/**
 * 운행 노선 정보를 담는 액티비티
 *
 * 나타내야 하는 정보
 * 1. 운행 노선 시작 지점, 끝 지점
 * 2. 모든 노선 정류소 정보
 * 3. 현재 버스 위치
 *
 * 넘겨 받아야 하는 정보
 * 1. 버스 번호
 * 2. routeId
 */
class BusInfoActivity : KNUActivity(), KNUAdapterListener {

    private val compositeDisposable = CompositeDisposable()

    private var busNumber: Int = -1
    private var routeId: String? = null
    private var hasBusRoutes = false

    lateinit var busRouteAdapter: BusRouteAdapter

    private var taskCounter: Int = 0

    private val handler = Handler()

    private val fetchTask = object : Runnable {
        override fun run() {
            setBusRoutes()
            handler.postDelayed(this, 30000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_node_info)

        getExtras()
        setUpRecyclerView()

        if (busNumber != -1 && !routeId.isNullOrEmpty()) {
            setTitle()
            setRoutesInterval()
        }
        else {
            Snackbar.make(rootLayout, "일시적으로 노선 정보를 불러올 수 없습니다.", Snackbar.LENGTH_SHORT).show()
        }

        fab_goToTop.setOnClickListener { rv_nodes.scrollToPosition(0) }
        fab_refresh.setOnClickListener { setBusRoutes() }
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
        busRouteAdapter = BusRouteAdapter(this, this, routeId)

        rv_nodes.layoutManager = LinearLayoutManager(this)
        rv_nodes.adapter = busRouteAdapter
        rv_nodes.addItemDecoration(StickyHeaderDecor(object : StickyHeaderDecor.StickyHeaderInterface {
            override fun isStickyHeader(position: Int): Boolean = busRouteAdapter.isStickyHeader(position)
            override fun getHeaderLayoutView(recyclerView: RecyclerView, position: Int): View? = busRouteAdapter.getHeaderLayoutView(recyclerView, position)
        }))
        rv_nodes.addItemDecoration(BusRouteAdapterDecor(this, 0f, 0f, object : BusRouteAdapterDecor.BusRouteDecorInterface {
            override fun isNode(position: Int): Boolean = busRouteAdapter.isNode(position)
        }))
        rv_nodes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val topChild = recyclerView.getChildAt(0) ?: return
                val topChildPosition = recyclerView.getChildAdapterPosition(topChild)

                fab_goToTop.isVisible = topChildPosition > 0
            }
        })

        busRouteAdapter.add(RouteInfoItem("", ""))
        busRouteAdapter.add(RouteBusCount(-1))
    }

    // 버스 번호, Route Id
    private fun getExtras() {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(KEY_BUS_NUMBER)) busNumber = bundle.getInt(KEY_BUS_NUMBER)
            if (bundle.containsKey(KEY_BUS_ROUTE)) routeId = bundle.getString(KEY_BUS_ROUTE)
        }
    }

    private fun setTitle() {
        appbar_title.text = "$busNumber"
        appbar_subtitle.setBackgroundResource(R.drawable.bus_subtitle_background)
    }
    // 운행 구간 정보 입력
    private fun setRoutesInterval() {
        progress_bar.startProgressWithCounter(++taskCounter)

        compositeDisposable.add(KNUService.instance()!!.getRouteInfo().subscribeWith(object : DisposableSingleObserver<RouteInfo>() {
            override fun onSuccess(item: RouteInfo) {
                progress_bar.stopProgressWithCounter(--taskCounter)

                if (item.body != null) {
                    busRouteAdapter.setInterval(item.body!!.items[0])
                }
            }
            override fun onError(e: Throwable) {
                progress_bar.stopProgressWithCounter(--taskCounter)

                Snackbar.make(rootLayout, "네트워크 에러", Snackbar.LENGTH_SHORT).show()
            }
        }))
    }
    // 버스 전체 노선 정보
    private fun setBusRoutes() {
        if (!hasBusRoutes) {
            progress_bar.startProgressWithCounter(++taskCounter)

            compositeDisposable.add(KNUService.instance()!!.getAllRoutes().subscribeWith(object : DisposableSingleObserver<Route>() {
                override fun onSuccess(item: Route) {
                    progress_bar.stopProgressWithCounter(--taskCounter)

                    if (item.body != null) {
                        hasBusRoutes = true
                        busRouteAdapter.addRouteList(item.body!!.items)
                        fetchBusList()
                    }
                }

                override fun onError(e: Throwable) {
                    progress_bar.stopProgressWithCounter(--taskCounter)

                    Snackbar.make(rootLayout, "네트워크 에러", Snackbar.LENGTH_SHORT).show()
                }
            }))
        }
        else fetchBusList()
    }

    // 현재 운행 버스 정보
    private fun fetchBusList() {
        progress_bar.startProgressWithCounter(++taskCounter)

        compositeDisposable.add(KNUService.instance()!!.getRouteBusList().subscribeWith(object : DisposableSingleObserver<RouteBus>() {
            override fun onSuccess(item: RouteBus) {
                progress_bar.stopProgressWithCounter(--taskCounter)

                if (item.body != null) {
                    busRouteAdapter.setHeaderValue(item.body!!.totalCount)
                    busRouteAdapter.updateBusSticker(item.body!!.items)
                }
            }

            override fun onError(e: Throwable) {
                progress_bar.stopProgressWithCounter(--taskCounter)

                Snackbar.make(rootLayout, "네트워크 에러", Snackbar.LENGTH_SHORT).show()
            }
        }))
    }

    override fun scrollToNodePosition(position: Int) {
        (rv_nodes.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, (rv_nodes.height / 2 - Utils.dp2px(88f) / 2).toInt())
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun getKNUID(): String = KNU_ID

    companion object {
        const val KNU_ID = "BusInfoActivity"
        const val KEY_BUS_NUMBER = "number"
        const val KEY_BUS_ROUTE = "routeId"
    }
}