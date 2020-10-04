package com.knu.knuguide.view.bus

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.knu.knuguide.R
import com.knu.knuguide.core.KNUService
import com.knu.knuguide.data.bus.RouteInfo
import com.knu.knuguide.view.KNUActivity
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
class BusInfoActivity : KNUActivity() {

    private val compositeDisposable = CompositeDisposable()

    private var busNumber: Int = -1
    private var routeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_node_info)

        getExtras()

        if (busNumber != -1 && !routeId.isNullOrEmpty()) {
            setTitle()
            setRoutesInterval()
        }
        else {
            Snackbar.make(rootLayout, "일시적으로 노선 정보를 불러올 수 없습니다.", Snackbar.LENGTH_SHORT).show()
        }
    }

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
        compositeDisposable.add(KNUService.instance()!!.getRouteInfo().subscribeWith(object : DisposableSingleObserver<RouteInfo>() {
            override fun onSuccess(item: RouteInfo) {
                val route = item.body!!.items[0]
                if (route != null) {
                    bus_routes_interval.text = "${route.startNodeName} ↔ ${route.endNodeName}"
                }
            }
            override fun onError(e: Throwable) {
                Snackbar.make(rootLayout, "네트워크 에러", Snackbar.LENGTH_SHORT).show()
                println(e.message)
            }
        }))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun getKNUID(): String {
        return KNU_ID
    }

    companion object {
        const val KNU_ID = "BusNodeInfoActivity"
        const val KEY_BUS_NUMBER = "number"
        const val KEY_BUS_ROUTE = "routeId"
    }
}