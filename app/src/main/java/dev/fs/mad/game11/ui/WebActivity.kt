package dev.fs.mad.game11.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.InvocationTargetException

class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")
        Log.e("TAG", "url  =$url")

        if (TextUtils.isEmpty(url)) {
            finish()
        }
        //com.alibaba.fastjson.JSONObject object = JSON.parseObject(url);
        //loadUrl = object.getString("url");
        //com.alibaba.fastjson.JSONObject object = JSON.parseObject(url);
        //loadUrl = object.getString("url");
        var loadUrl = url.toString()
        Log.e("TAG", "loadUrl  =$loadUrl")
        val relativeLayout = RelativeLayout(this)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        webView = WebView(this)
        setSetting()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                Log.e("TAG", " url  = $url")
                return try {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                    finish()
                    true
                } catch (e: Exception) {
                    true
                }
            }
        }

        webView.setLayoutParams(
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        webView.loadUrl(loadUrl)
        val textView = TextView(this)
        textView.setOnClickListener { view: View? ->
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
        textView.text = "X "
        textView.setTextColor(Color.RED)
        textView.textSize = 25f
        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 20, 20, 10)
        textView.layoutParams = layoutParams
        textView.id = View.generateViewId()
        (textView.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        relativeLayout.addView(webView)
        relativeLayout.addView(textView)
        setContentView(relativeLayout)
    }

    private fun setSetting() {
        val setting: WebSettings = webView.getSettings()
        setting.javaScriptEnabled = true
        setting.javaScriptCanOpenWindowsAutomatically = true
        setting.domStorageEnabled = true
        setting.cacheMode = WebSettings.LOAD_DEFAULT
        setting.allowContentAccess = true
        setting.databaseEnabled = true
        setting.setGeolocationEnabled(true)
        setting.useWideViewPort = true
        setting.setUserAgentString(setting.userAgentString.replace("; wv".toRegex(), ""))
        setting.mediaPlaybackRequiresUserGesture = false
        setting.setSupportZoom(false)
        try {
            val clazz: Class<*> = setting.javaClass
            val method = clazz.getMethod(
                "setAllowUniversalAccessFromFileURLs",
                Boolean::class.javaPrimitiveType
            )
            method.invoke(setting, true)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        webView.setDownloadListener { url: String, userAgent: String, contentDisposition: String, mimetype: String, contentLength: Long ->
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val uri = Uri.parse(url)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}