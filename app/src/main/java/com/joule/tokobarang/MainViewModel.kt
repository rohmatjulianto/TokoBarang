package com.joule.tokobarang

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joule.tokobarang.Utils.IOUtils
import com.joule.tokobarang.api.ApiServices
import com.joule.tokobarang.api.GetApiServices
import com.joule.tokobarang.data.ProductItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val _product = MutableLiveData<ArrayList<ProductItem>>()
    val product: LiveData<ArrayList<ProductItem>> = _product

    private val _msg = MutableLiveData<String>()
    val msg : LiveData<String> = _msg

    fun getListBarang(token: String){
        val apiServices = ApiServices.api().create(GetApiServices::class.java)
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

    fun deleteProduct(token: String, position: Int){

    }
}