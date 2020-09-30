package com.knu.knuguide.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.knu.knuguide.BuildConfig
import com.knu.knuguide.core.logging.HttpPrettyLogging
import com.knu.knuguide.data.announcement.Announcement
import com.knu.knuguide.data.calendar.KNUDay
import com.knu.knuguide.data.calendar.Task
import com.knu.knuguide.data.search.Department
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit


class KNUService {
    init {
        val loggingInterceptor = HttpLoggingInterceptor(HttpPrettyLogging())
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val baseClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(HeaderInterceptor())

        if (BuildConfig.DEBUG)
            baseClientBuilder.addInterceptor(loggingInterceptor)

        val baseClient = baseClientBuilder.build()

        val baseRetrofitBuilder = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(baseClient)
        api = baseRetrofitBuilder.build().create(KNUInterface::class.java)

        gson = GsonBuilder().create()
    }

//    fun postStation(stationName: String): Single<ArrayList<String>> {
//        return api!!.postStation(prmOperation = "getStationListByStationName", prmStationId = "", prmStationName = URLEncoder.encode(stationName, "euc-kr"), prmSearchCode = "", prmCoordX = "", prmCoordY = "")
//            .map {
//                val html = it.string()
//                val document = Jsoup.parse(html)
//                val elements = document.select("body > table > tbody > tr:nth-child(3) > td > div > table > tbody > tr")
//
//                val list = ArrayList<String>()
//                for (e in elements) {
//                    list.add(e.text())
//                }
//
//                return@map list
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//    }

//    fun getSQUsers(howmany: Int): Single<ResponseBody> {
//        return api!!.getSQUsers("desc", "reputation", "stackoverflow", howmany)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    fun getWeather(): Single<ResponseBody> {
//        return api!!.getWeather(35, 139, "439d4b804bc8187953eb36d2a8c26a02")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//    }

    // 학사일정 가져오기
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
                val respJson = JSONObject(it.string())
                val contents = respJson.getJSONArray("content")
                val items = gson.fromJson<List<Department>>(contents.toString(), object : TypeToken<List<Department>>() {}.type)
                items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // 특정 과 정보 가져오기
    fun getDepartmentById(id: String): Single<List<Department>> {
        return api!!.getDepartmentById(id)
            .map {
                val respJson = JSONObject(it.string())
                val contents = respJson.getJSONArray("content")
                val items = gson.fromJson<List<Department>>(contents.toString(), object : TypeToken<List<Department>>() {}.type)
                items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // 특정 과 공지사항 가져오기
    fun getNotice(id: String): Single<List<Announcement>> {
        return api!!.getNotice(id)
            .map {
                val respJson = JSONObject(it.string())
                val contents = respJson.getJSONArray("notice")
                val items = gson.fromJson<List<Announcement>>(contents.toString(), object : TypeToken<List<Announcement>>() {}.type)
                items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private var service: KNUService? = null
        lateinit var gson: Gson

        private var baseURL = "http://13.125.224.14/"

        private var api: KNUInterface? = null

        @Synchronized
        fun instance(): KNUService? {
            if (service == null) {
                service = KNUService()
            }
            return service
        }
    }
}