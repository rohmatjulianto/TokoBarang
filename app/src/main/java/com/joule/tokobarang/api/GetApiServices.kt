package com.joule.tokobarang.api

import com.joule.tokobarang.data.ProductItem
import com.joule.tokobarang.data.RegisterResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface GetApiServices {
    @Multipart
    @POST("api/register")
    fun register(
        @Part("email") email: RequestBody,
        @Part("password") pass: RequestBody
    ): Call<RegisterResponse>

    @Multipart
    @POST("api/auth/login")
    fun login(
        @Part("email") email: RequestBody,
        @Part("password") pass: RequestBody
    ): Call<ResponseBody>

    @GET("api/items")
    fun getListProduct(@Header("Authorization") token: String): Call<ArrayList<ProductItem>>

    @Multipart
    @POST("api/item/add")
    fun addProduct(
        @Header("Authorization") token: String,
        @Part("kode_barang") kode_barang: String,
        @Part("nama_barang") nama_barang: String,
        @Part("jumlah_barang") jumlah: Int,
        @Part("harga_barang") harga: Long,
        @Part("satuan_barang") satuan: Int,
        @Part("status_barang") status: Int
    ): Call<ResponseBody>

    @Multipart
    @POST("api/item/update")
    fun updateProduct(
        @Header("Authorization") token: String,
        @Part("kode_barang") kode_barang: String,
        @Part("nama_barang") nama_barang: String,
        @Part("jumlah_barang") jumlah: Int,
        @Part("harga_barang") harga: Long,
        @Part("satuan_barang") satuan: Int,
        @Part("status_barang") status: Int
    ): Call<ResponseBody>

    @Multipart
    @POST("api/item/delete")
    fun deleteProduct(@Header("Authorization") token: String, @Part("kode_barang") kode_barang: String)

    @Multipart
    @POST("api/item/search")
    fun searchByKode(@Header("Authorization") token: String, @Part("kode_barang") kode_barang: String)
}