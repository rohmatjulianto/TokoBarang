package com.joule.tokobarang.auth.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joule.tokobarang.Utils.IOUtils
import com.joule.tokobarang.api.ApiServices
import com.joule.tokobarang.api.GetApiServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, pass: String) {
        val apiServices = ApiServices.api().create(GetApiServices::class.java)

        val email = IOUtils.toRequestBody(email)
        val pass = IOUtils.toRequestBody(pass)
        apiServices.login(email, pass)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _token.value = IOUtils.getValueResponse(response.body()!!.string(), "token")
                    } else {
                        val value = response.errorBody()!!.string()
                        try {
                            val json = JSONObject(value)
                            if (json.has("error")) {
                                _error.value = IOUtils.getValueResponse(value, "error")
                            } else {
                                _error.value =
                                    JSONObject(value).getJSONArray("email").get(0).toString()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("yy", "onFailure: ${t.message}")
                }

            })
    }
}