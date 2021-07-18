package com.joule.tokobarang.ui.main

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

//    message if token expired
    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

//    message no data while search
    private val _msgNoData = MutableLiveData<Boolean>()
    val msgNoData: LiveData<Boolean> = _msgNoData

//    list of product
    private val _product = MutableLiveData<ArrayList<ProductItem>>()
    val product: LiveData<ArrayList<ProductItem>> = _product

//    delete
    private val _delete = MutableLiveData<String>()
    val delete: LiveData<String> = _delete

//    edit
    private val _edit = MutableLiveData<ProductItem>()
    val edit: LiveData<ProductItem> = _edit

//    add
    private val _add = MutableLiveData<ProductItem>()
    val add: LiveData<ProductItem> = _add

//    add failure message
    private val _addProductFailure = MutableLiveData<String>()
    val addProductFailure: LiveData<String> = _addProductFailure

    companion object {
        val apiServices = ApiServices.api().create(GetApiServices::class.java)
    }

    fun getListBarang(token: String) {
        apiServices.getListProduct(token)
            .enqueue(object : Callback<ArrayList<ProductItem>> {
                override fun onResponse(
                    call: Call<ArrayList<ProductItem>>,
                    response: Response<ArrayList<ProductItem>>
                ) {
                    if (response.isSuccessful) {
                        _product.postValue(response.body())
                    } else {
                        _msg.value =
                            IOUtils.getValueResponse(response.errorBody()!!.string(), "error")
                    }
                }

                override fun onFailure(call: Call<ArrayList<ProductItem>>, t: Throwable) {
                    Log.d("yy", "onFailure: ${t.message}")
                }

            })
    }

    fun getListByKode(token: String, kodeBarang: String) {
        val kode = IOUtils.toRequestBody(kodeBarang)
        apiServices.searchByKode(token, kode)
            .enqueue(object : Callback<ProductItem> {
                override fun onResponse(
                    call: Call<ProductItem>,
                    response: Response<ProductItem>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.nama_barang != null) {
                            val data: ArrayList<ProductItem> = ArrayList()
                            response.body()?.let { data.add(it) }
                            _product.value = data
                            _msgNoData.value = false
                        } else {
                            _msgNoData.value = true
                        }
                    }
                }

                override fun onFailure(call: Call<ProductItem>, t: Throwable) {
                    Log.d("yy", "onFailure: ${t.message}")
                }

            })
    }

    fun deleteProduct(token: String, kodeBarang: String) {
        apiServices.deleteProduct(token, IOUtils.toRequestBody(kodeBarang))
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _delete.value = "Success"
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }

            })
    }

    fun addProduct(token: String, productItem: ProductItem) {
        val kode = IOUtils.toRequestBody(productItem.kode_barang)
        val name = IOUtils.toRequestBody(productItem.nama_barang)
        val jumlah = IOUtils.toRequestBody(productItem.jumlah_barang.toString())
        val harga = IOUtils.toRequestBody(productItem.harga_barang.toString())
        val satuan = IOUtils.toRequestBody(productItem.satuan_barang)
        val status = IOUtils.toRequestBody(productItem.status_barang.toString())

        apiServices.addProduct(token, kode, name, jumlah, harga, satuan, status)
            .enqueue(object : Callback<ProductItem> {
                override fun onResponse(
                    call: Call<ProductItem>,
                    response: Response<ProductItem>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.nama_barang != null){
                            _add.value = response.body()
                        }else{
                            _addProductFailure.value = "${productItem.kode_barang} already exists!"
                        }
                    }
                }

                override fun onFailure(call: Call<ProductItem>, t: Throwable) {
                }

            })
    }

    fun editProduct(token: String, productItem: ProductItem) {
        val kode = IOUtils.toRequestBody(productItem.kode_barang)
        val name = IOUtils.toRequestBody(productItem.nama_barang)
        val jumlah = IOUtils.toRequestBody(productItem.jumlah_barang.toString())
        val harga = IOUtils.toRequestBody(productItem.harga_barang.toString())
        val satuan = IOUtils.toRequestBody(productItem.satuan_barang)
        val status = IOUtils.toRequestBody(productItem.status_barang.toString())

        apiServices.updateProduct(token, kode, name, jumlah, harga, satuan, status)
            .enqueue(object : Callback<ProductItem> {
                override fun onResponse(
                    call: Call<ProductItem>,
                    response: Response<ProductItem>
                ) {
                    if (response.isSuccessful) {
                        _edit.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ProductItem>, t: Throwable) {
                }

            })
    }

}