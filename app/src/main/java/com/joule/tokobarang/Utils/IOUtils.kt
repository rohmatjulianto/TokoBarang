package com.joule.tokobarang.Utils

import android.annotation.SuppressLint
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.lang.Exception
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class IOUtils {
    companion object {
        val PREFERENCE_NAME = "user"
        fun toRequestBody(value: String): RequestBody {
            return RequestBody.create(MediaType.parse("text/plain"), value)
        }

        fun getValueResponse(value: String, name: String): String {
            val jObjError = JSONObject(value)
            return jObjError.getString(name)
        }

        @SuppressLint("SimpleDateFormat")
        fun getTime(date: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss a")
            val parsedDate = inputFormat.parse(date)
            return outputFormat.format(parsedDate!!)
        }

        fun getCurrency(price : Long) : String{
            val format = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance("IDR")
            return format.format(price)
        }
    }
}