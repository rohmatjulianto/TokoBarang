package com.joule.tokobarang

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joule.tokobarang.Utils.IOUtils
import com.joule.tokobarang.api.ApiServices
import com.joule.tokobarang.api.GetApiServices
import com.joule.tokobarang.data.ProductItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val _product = MutableLiveData<ArrayList<ProductItem>>()
    val product: LiveData<ArrayList<ProductItem>> = _product

    private val _msg = MutableLiveData<String>()
    val msg : LiveData<String> = _msg


    companion object{
        val apiServices = ApiServices.api().create(GetApiServices::class.java)
    }

    fun getListBarang(token: String){
        apiServices.getListProduct(token)
            .enqueue(object : Callback<ArrayList<ProductItem>> {
                override fun onResponse(
                    call: Call<ArrayList<ProductItem>>,
                    response: Response<ArrayList<ProductItem>>
                ) {
                    if (response.isSuccessful) {
                        _product.postValue(response.body())
                    } else {
                        _msg.value = IOUtils.getValueResponse(response.errorBody()!!.string(), "error")
                    }
                }

                override fun onFailure(call: Call<ArrayList<ProductItem>>, t: Throwable) {
                    Log.d("yy", "onFailure: ${t.message}")
                }

            })
    }

    fun deleteProduct(token: String, kodeBarang: String ){
        apiServices.deleteProduct(token,IOUtils.toRequestBody(kodeBarang))
            .enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun addProduct(token: String, productItem: ProductItem){
        val kode = IOUtils.toRequestBody(productItem.kode_barang)
        val name = IOUtils.toRequestBody(productItem.nama_barang)
        val jumlah = IOUtils.toRequestBody(productItem.jumlah_barang.toString())
        val harga = IOUtils.toRequestBody(productItem.harga_barang.toString())
        val satuan = IOUtils.toRequestBody(productItem.satuan_barang)
        val status = IOUtils.toRequestBody(productItem.status_barang.toString())

        apiServices.addProduct(token, kode, name, jumlah, harga, satuan, status)
            .enqueue(object :Callback<ProductItem>{
                override fun onResponse(
                    call: Call<ProductItem>,
                    response: Response<ProductItem>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ProductItem>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun editProduct(token: String, productItem: ProductItem){
        val kode = IOUtils.toRequestBody(productItem.kode_barang)
        val name = IOUtils.toRequestBody(productItem.nama_barang)
        val jumlah = IOUtils.toRequestBody(productItem.jumlah_barang.toString())
        val harga = IOUtils.toRequestBody(productItem.harga_barang.toString())
        val satuan = IOUtils.toRequestBody(productItem.satuan_barang)
        val status = IOUtils.toRequestBody(productItem.status_barang.toString())

        apiServices.updateProduct(token, kode, name, jumlah, harga, satuan, status)
            .enqueue(object :Callback<ProductItem>{
                override fun onResponse(
                    call: Call<ProductItem>,
                    response: Response<ProductItem>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ProductItem>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

}