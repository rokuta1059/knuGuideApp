package com.knu.knuguide.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.knu.knuguide.BuildConfig
import com.knu.knuguide.core.logging.HttpPrettyLogging
import com.knu.knuguide.data.calendar.KNUDay
import com.knu.knuguide.data.calendar.Task
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class KNUService {
    init {
        val loggingInterceptor = HttpLoggingInterceptor(HttpPrettyLogging())
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val baseClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG)
            baseClientBuilder.addInterceptor(loggingInterceptor)

        val baseClient = baseClientBuilder.build()

        val baseRetrofitBuilder = Retrofit.Builder()
                .baseUrl(TEST_URL_2)
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

    fun getTest(): Single<ResponseBody> {
        return api!!.getTest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getSchedule(): Single<List<Task>> {
        return api!!.getSchedule()
            .map {
                val respJson = JSONObject(it.string())
                val contents = respJson.getJSONArray("content")
                val scheduleList = gson.fromJson<List<Task>>(contents.toString(), object : TypeToken<List<Task>>() {}.type)
                scheduleList
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private var service: KNUService? = null
        lateinit var gson: Gson

        private var TEST_URL = "http://www.chbis.kr/"
        private var TEST_URL_2 = "http://13.125.224.14:8888/"
        private var TEST_URL_3 = "https://samples.openweathermap.org/data/2.5/"

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