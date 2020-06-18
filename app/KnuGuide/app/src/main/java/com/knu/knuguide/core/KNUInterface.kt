package com.knu.knuguide.core

import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface KNUInterface {
//    @FormUrlEncoded
//    @POST("realTimeBusInfoResult.do")
//    fun postStation(@Field("prmOperation") prmOperation: String,
//                    @Field("prmStationID") prmStationId: String,
//                    @Field("prmStationName", encoded = true) prmStationName: String,
//                    @Field("prmSearchCode") prmSearchCode: String,
//                    @Field("prmCoordX") prmCoordX: String,
//                    @Field("prmCoordY") prmCoordY: String): Single<ResponseBody>

//    @GET("weather")
//    fun getWeather(@Query("lat") latitude: Int, @Query("lon") longitude: Int, @Query("appid") appid: String) : Single<ResponseBody>
//
//    @GET("2.2/users")
//    fun getSQUsers(@Query("order") order: String, @Query("sort") sort: String, @Query("site") site: String, @Query("pagesize") howmany: Int) : Single<ResponseBody>

    @GET("department/notice/컴퓨터공학과")
    fun getTest(): Single<ResponseBody>

    /*
        todo: 1. KNUInterface에 학사일정 HTTP Method 추가
              2. KNUService에 해당 URL 요청 함수 작성
              3. 학사일정 Activity에서 먼저 요청 함수 사용하여 데이터 load
              4. onSuccess 시 View 그리기
     */
    @GET("university/schedule")
    fun getSchedule(): Single<ResponseBody>
}