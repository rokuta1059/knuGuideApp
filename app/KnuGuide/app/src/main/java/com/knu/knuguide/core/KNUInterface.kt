package com.knu.knuguide.core

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface KNUInterface {

    @GET("schedule")
    fun getSchedule(): Single<ResponseBody>

    @GET("department")
    fun getDepartment(): Single<ResponseBody>

    @GET("department/{id}")
    fun getDepartmentById(@Path("id") id: String): Single<ResponseBody>

    @GET("department/notice/{id}")
    fun getNotice(@Path("id") id: String): Single<ResponseBody>

    @GET("department/credit/{id}/2020")
    fun getDepartmentCredit(@Path("id") id: String): Single<ResponseBody>
}