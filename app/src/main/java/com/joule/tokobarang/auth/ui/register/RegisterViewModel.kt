package com.joule.tokobarang.auth.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joule.tokobarang.Utils.IOUtils
import com.joule.tokobarang.api.ApiServices
import com.joule.tokobarang.api.GetApiServices
import com.joule.tokobarang.data.RegisterResponse
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _msg = MutableLiveData<String>()
    val msg : LiveData<String> = _msg

    fun register(email: String, pass: String) {
        val apiServices = ApiServices.api().create(GetApiServices::class.java)

        val email = IOUtils.toRequestBody(email)
        val pass = IOUtils.toRequestBody(pass)
        apiServices.register(email, pass)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        _msg.value = "success"
                    } else {
                        val value = response.errorBody()!!.string()
                        val jsObj = JSONObject(value).getJSONArray("email").get(0)
                        _msg.value = jsObj.toString()
                    }

                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("yy", "onFailure: ${t.message}")
                }

            })
    }
}