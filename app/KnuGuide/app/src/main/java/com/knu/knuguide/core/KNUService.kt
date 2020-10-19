package com.knu.knuguide.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.knu.knuguide.BuildConfig
import com.knu.knuguide.core.logging.HttpPrettyLogging
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.data.bus.Bus
import com.knu.knuguide.data.bus.Route
import com.knu.knuguide.data.bus.RouteBus
import com.knu.knuguide.data.bus.RouteInfo
import com.knu.knuguide.data.calendar.Task
import com.knu.knuguide.data.department.Credit
import com.knu.knuguide.data.search.Department
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

class KNUService {
    init {
        api = makeRetrofitBuilder(BASE_URL).build().create(KNUInterface::class.java)
        tagoApi = makeRetrofitBuilder(TAGO_URL).build().create(TAGOInterface::class.java)

        gson = GsonBuilder().create()
    }

    private fun makeRetrofitBuilder(url: String): Retrofit.Builder {
        val loggingInterceptor = HttpLoggingInterceptor(HttpPrettyLogging())
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val baseClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())

        if (BuildConfig.DEBUG)
            baseClientBuilder.addInterceptor(loggingInterceptor)

        val baseClient = baseClientBuilder.build()

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(baseClient)
    }

    // 학사일정
    fun getSchedule(): Single<List<Task>> {
        return api!!.getSchedule()
            .map {
                val scheduleList = gson.fromJson<List<Task>>(it.string(), object : TypeToken<List<Task>>() {}.type)
                scheduleList
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // 전체 과 정보 가져오기
    fun getDepartment(): Single<List<Department>> {
        return api!!.getDepartment()
            .map {
                val items = gson.fromJson<List<Department>>(it.string(), object : TypeToken<List<Department>>() {}.type)
                items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // 특정 과 정보 가져오기
    fun getDepartmentById(id: String): Single<Department> {
        return api!!.getDepartmentById(id)
            .map {
                val items = gson.fromJson<Department>(it.string(), object : TypeToken<Department>() {}.type)
                items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // 특정 과 공지사항 가져오기
    fun getNotice(id: String): Single<List<Announcement>> {
        return api!!.getNotice(id)
            .map {
                val respJson = JSONArray(it.string())
                val items = gson.fromJson<List<Announcement>>(respJson.toString(), object : TypeToken<List<Announcement>>() {}.type)
                items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getDepartmentCredit(id: String): Single<List<Credit>> {
        return api!!.getDepartmentCredit(id)
            .map {
                val respJson = JSONArray(it.string())
                val items = gson.fromJson<List<Credit>>(respJson.toString(), object : TypeToken<List<Credit>>() {}.type)
                items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    
    fun getRouteInfo(cityCode: Int = 32010, routeId: String = "CCB250030000"): Single<RouteInfo> {
        return tagoApi!!.getRouteInfo(TAGO_API_KEY, cityCode, routeId)
            .map {
                val serializer = Persister()
                val routeInfo = serializer.read(RouteInfo::class.java, it.string())
                routeInfo
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getAllRoutes(cityCode: Int = 32010, routeId: String = "CCB250030000", numOfRows: Int = 100): Single<Route> {
        return tagoApi!!.getAllRoutes(TAGO_API_KEY, cityCode, routeId, numOfRows)
            .map {
                val serializer = Persister()
                val route = serializer.read(Route::class.java, it.string())
                route
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getRouteBusList(cityCode: Int = 32010, routeId: String = "CCB250030000"): Single<RouteBus> {
        return tagoApi!!.getRouteBusList(TAGO_API_KEY, cityCode, routeId)
            .map {
                val serializer = Persister()
                val routeBus = serializer.read(RouteBus::class.java, it.string())
                routeBus
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSpecifyArrivalBusList(cityCode: Int = 32010, nodeId: String, routeId: String = "CCB250030000"): Single<Bus> {
        return tagoApi!!.getSpecifyArrivalBusList(TAGO_API_KEY, cityCode, nodeId, routeId)
            .map {
                val serializer = Persister()
                val bus = serializer.read(Bus::class.java, it.string())
                bus
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private var service: KNUService? = null
        lateinit var gson: Gson

        private const val BASE_URL = "http://13.125.224.14/"
        private const val TAGO_URL = "http://openapi.tago.go.kr/openapi/service/"

        const val TAGO_API_KEY = "zv1uFSqtX4q8Bt%2FwEqX6m%2BsIuEWK9EcCfTz%2Frks5n2fLVePjJZaYhwrmSzWzC6ijhqiK92ZyJ0dKmc%2F6z%2FVqsQ%3D%3D"

        // 곰두리 서버 API
        private var api: KNUInterface? = null
        // TAGO 서버 API
        private var tagoApi: TAGOInterface? = null

        @Synchronized
        fun instance(): KNUService? {
            if (service == null) {
                service = KNUService()
            }
            return service
        }
    }
}