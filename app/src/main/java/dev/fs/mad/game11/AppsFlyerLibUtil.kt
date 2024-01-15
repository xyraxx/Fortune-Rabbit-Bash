package dev.fs.mad.game11

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import dev.fs.mad.game11.controller.VolleyController
import dev.fs.mad.game11.ui.MainActivity
import dev.fs.mad.game11.ui.WebActivity
import java.util.Objects


class AppsFlyerLibUtil {

    companion object{

        private val TAG = "AppsFlyerLibUtil"

        fun init(context: Context) {
            AppsFlyerLib.getInstance()
                .start(context, "2jAVWqgmQoeQmHCJyVUsRh", object : AppsFlyerRequestListener {
                    override fun onSuccess() {
                        Log.e(TAG, "Launch sent successfully, got 200 response code from server")
                    }

                    override fun onError(i: Int, s: String) {
                        Log.e(
                            TAG,
                            "Launch failed to be sent:\nError code: $i\nError description: $s"
                        )
                    }
                })
            AppsFlyerLib.getInstance().setDebugLog(true)
        }

        fun event(context: Activity, name: String, data: String) {
            val eventValue: MutableMap<String, Any> = HashMap()


            Log.d("AFLIB policy url",VolleyController.policyMain)
            if("UserConsent" == name){
                if(VolleyController.gameStatus == 1){
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("url", VolleyController.policyMain)
                    context.startActivity(intent)
                }else{
                    val volleyController = VolleyController(context)
                    volleyController.gameMaintenance(context.packageName)
                }
            }
            else if ("openWindow" == name) {
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra("url", data)
                context.startActivityForResult(intent, 1)
            } else if ("firstrecharge" == name || "recharge" == name) {
                try {
                    val maps = JSON.parse(data) as Map<*, *>
                    for (map in maps.entries) {
                        val key = (map as Map.Entry<*, *>).key.toString()
                        if ("amount" == key) {
                            eventValue[AFInAppEventParameterName.REVENUE] =
                                (map as Map.Entry<*, *>).value!!
                        } else if ("currency" == key) {
                            eventValue[AFInAppEventParameterName.CURRENCY] =
                                (map as Map.Entry<*, *>).value!!
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, Objects.requireNonNull(e.message).toString())
                }
            } else if ("withdrawOrderSuccess" == name) {
                try {
                    val maps = JSON.parse(data) as Map<*, *>
                    for (map in maps.entries) {
                        val key = (map as Map.Entry<*, *>).key.toString()
                        if ("amount" == key) {
                            var revenue = 0f
                            val value = (map as Map.Entry<*, *>).value.toString()
                            if (!TextUtils.isEmpty(value)) {
                                revenue = java.lang.Float.valueOf(value)
                                revenue = -revenue
                            }
                            eventValue[AFInAppEventParameterName.REVENUE] = revenue
                        } else if ("currency" == key) {
                            eventValue[AFInAppEventParameterName.CURRENCY] =
                                (map as Map.Entry<*, *>).value!!
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, Objects.requireNonNull(e.message).toString())
                }
            } else {
                eventValue[name] = data
            }
            AppsFlyerLib.getInstance().logEvent(context, name, eventValue)
            Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
        }
    }

}