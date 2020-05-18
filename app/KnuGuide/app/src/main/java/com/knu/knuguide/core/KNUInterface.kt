package com.knu.knuguide.core

import com.knu.knuguide.data.UserResponse
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*


interface KNUInterface {
    @FormUrlEncoded
    @POST("realTimeBusInfoResult.do")
    fun postStation(@Field("prmOperation") prmOperation: String,
                    @Field("prmStationID") prmStationId: String,
                    @Field("prmStationName", encoded = true) prmStationName: String,
                    @Field("prmSearchCode") prmSearchCode: String,
                    @Field("prmCoordX") prmCoordX: String,
                    @Field("prmCoordY") prmCoordY: String): Single<ResponseBody>

//    @GET("weather")
//    fun getWeather(@Query("lat") latitude: Int, @Query("lon") longitude: Int, @Query("appid") appid: String) : Single<ResponseBody>
//
//    @GET("2.2/users")
//    fun getSQUsers(@Query("order") order: String, @Query("sort") sort: String, @Query("site") site: String, @Query("pagesize") howmany: Int) : Single<ResponseBody>
}