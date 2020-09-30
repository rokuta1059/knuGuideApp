package com.knu.knuguide.core

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface TAGOInterface {

    // 노선별 경유정류소 목록 조회
    @GET("BusRouteInfoInqireService/getRouteAcctoThrghSttnList/{userKey}/{cityCode}/{routeId}/{numOfRows}")
    fun getStationsByRouteId(
        @Path("userKey") userKey: String,
        @Path("cityCode") cityCode: Int,
        @Path("routeId") routeId: String,
        @Path("numOfRows") numOfRows: Int): Single<ResponseBody>
    
}