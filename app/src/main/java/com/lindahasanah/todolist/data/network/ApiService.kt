package com.lindahasanah.todolist.data.network

import com.lindahasanah.todolist.data.response.MessageResponse
import com.lindahasanah.todolist.data.response.CatatanResponseItem
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @GET("/mycatatan")
    fun getCatatan(): Call<List<CatatanResponseItem>>

    @FormUrlEncoded
    @POST("/addCatatan")
    fun addcatatan(
        @Field("task") task: String,
        @Field("deskripsi") deskripsi: String,
        @Field("status") status: String,
        @Field("tanggal") tanggal: String
    ): Call<MessageResponse>

    @FormUrlEncoded
    @PUT("/editcatatan")
    fun editcatatan(
        @Query("id") id : Int,
        @Field("task") task: String,
        @Field("deskripsi") deskripsi: String,
        @Field("status") status: String
    ): Call<MessageResponse>

    @GET("/detailcatatan")
    fun detailcatatan(
        @Query("id") id : Int
    ): Call<CatatanResponseItem>

    @DELETE("/hapusCatatan")
    fun deleteCatatan(
        @Query("id") id : Int
    ): Call<MessageResponse>
}