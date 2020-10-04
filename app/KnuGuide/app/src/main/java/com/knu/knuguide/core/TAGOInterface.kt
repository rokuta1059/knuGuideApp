package com.knu.knuguide.core

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TAGOInterface {

    // 노선별 경유정류소 목록 조회
    @GET("BusRouteInfoInqireService/getRouteAcctoThrghSttnList/{userKey}/{cityCode}/{routeId}/{numOfRows}")
    fun getStationsByRouteId(
        @Path("userKey") userKey: String,
        @Path("cityCode") cityCode: Int,
        @Path("routeId") routeId: String,
        @Path("numOfRows") numOfRows: Int): Single<ResponseBody>

    // 노선 정보(시작 지점, 끝 지점, 현재 운행 버스 수) 조회
    @Headers("Connection: close")
    @GET("BusRouteInfoInqireService/getRouteInfoIem")
    fun getRouteInfo(
        @Query("ServiceKey", encoded = true) userKey: String,
        @Query("cityCode", encoded = true) cityCode: Int,
        @Query("routeId", encoded = true) routeId: String
    ): Single<ResponseBody>
}