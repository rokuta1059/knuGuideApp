package com.knu.knuguide.core

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TAGOInterface {

    // 노선 정보(시작 지점, 끝 지점, 현재 운행 버스 수) 조회
    @GET("BusRouteInfoInqireService/getRouteInfoIem")
    fun getRouteInfo(
        @Query("ServiceKey", encoded = true) userKey: String,
        @Query("cityCode", encoded = true) cityCode: Int,
        @Query("routeId", encoded = true) routeId: String
    ): Single<ResponseBody>

    // 전체 노선 정보 조회
    @GET("BusRouteInfoInqireService/getRouteAcctoThrghSttnList")
    fun getAllRoutes(
        @Query("ServiceKey", encoded = true) userKey: String,
        @Query("cityCode", encoded = true) cityCode: Int,
        @Query("routeId", encoded = true) routeId: String,
        @Query("numOfRows", encoded = true) rows: Int
    ): Single<ResponseBody>

    // 현재 운행 중인 버스 위치 정보 조회
    @GET("BusLcInfoInqireService/getRouteAcctoBusLcList")
    fun getRouteBusList(
        @Query("ServiceKey", encoded = true) userKey: String,
        @Query("cityCode", encoded = true) cityCode: Int,
        @Query("routeId", encoded = true) routeId: String
    ): Single<ResponseBody>

    // 특정 노선 버스 도착 정보 조회
    @GET("ArvlInfoInqireService/getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList")
    fun getSpecifyArrivalBusList(
        @Query("ServiceKey", encoded = true) userKey: String,
        @Query("cityCode", encoded = true) cityCode: Int,
        @Query("nodeId", encoded = true) nodeId: String,
        @Query("routeId", encoded = true) routeId: String
    ): Single<ResponseBody>
}