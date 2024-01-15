package dev.fs.mad.game11.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebChromeClient.FileChooserParams
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import dev.fs.mad.game11.AppsFlyerLibUtil
import dev.fs.mad.game11.controller.VolleyController
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.InvocationTargetException

class ConsentActivity : AppCompatActivity() {

    private lateinit var wv: WebView

    private var mUploadCallBack: ValueCallback<Uri>? = null
    private var mUploadCallBackAboveL: ValueCallback<Array<Uri>>? = null
    private val REQUEST_CODE_FILE_CHOOSER = 888


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(1024,1024)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)


        var urlPolicy = VolleyController.policyMain
        Log.d("POLICY MAIN IN CONSENT", urlPolicy)
        if (TextUtils.isEmpty(urlPolicy)) {
            finish()
        }

        wv = WebView(this)
        webSettings()
        wv.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val WgPackage =
                    ("javascript:window.WgPackage = {name:'" + packageName + "', version:'"
                            + getVersion(this@ConsentActivity) + "'}")
                wv.evaluateJavascript(WgPackage, ValueCallback<String> { value: String? -> })
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                val WgPackage =
                    ("javascript:window.WgPackage = {name:'" + packageName + "', version:'"
                            + getVersion(this@ConsentActivity) + "'}")
                wv.evaluateJavascript(WgPackage, ValueCallback<String> { value: String? -> })
            }
        })
        wv.addJavascriptInterface(JsInterface(), "jsBridge")
        wv.getSettings().setJavaScriptEnabled(true)
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)

        wv.loadUrl(urlPolicy)
        setContentView(wv)
        AppsFlyerLibUtil.init(this)
    }

    fun getVersion(context: Context): String {
        var versionName = ""
        try {
            val packageInfo = context.applicationContext.packageManager
                .getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message.toString())
        }
        return versionName
    }

    private fun webSettings() {
        val setting: WebSettings = wv.getSettings()
        setting.javaScriptEnabled = true
        setting.javaScriptCanOpenWindowsAutomatically = true
        setting.setSupportMultipleWindows(true)
        setting.domStorageEnabled = true
        setting.cacheMode = WebSettings.LOAD_DEFAULT
        setting.allowContentAccess = true
        setting.databaseEnabled = true
        setting.setGeolocationEnabled(true)
        setting.useWideViewPort = true
        setting.setUserAgentString(setting.userAgentString.replace("; wv".toRegex(), ""))
        setting.mediaPlaybackRequiresUserGesture = false
        setting.setSupportZoom(false)
        EventBus.getDefault().post(String())
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
        wv.setDownloadListener { url: String, userAgent: String, contentDisposition: String, mimetype: String, contentLength: Long ->
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val uri = Uri.parse(url)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        wv.setWebChromeClient(object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                this@ConsentActivity.mUploadCallBackAboveL = filePathCallback
                openFileChooseProcess()
                return true
            }
        })
    }

    private fun openFileChooseProcess() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_CODE_FILE_CHOOSER)
    }

    override fun onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack()
        } else {
            super.onBackPressed()
        }
    }

    inner class JsInterface {
        @JavascriptInterface
        fun postMessage(name: String, data: String) {
            Log.e(TAG,"name = $name    data = $data")
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(data)) {
                return
            }
            AppsFlyerLibUtil.event(this@ConsentActivity, name, data)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG,"---------requestCode = $requestCode      resultCode = $resultCode")
        if (requestCode == REQUEST_CODE_FILE_CHOOSER) {
            val result = if (data == null || resultCode != RESULT_OK) null else data.data
            if (result != null) {
                if (mUploadCallBackAboveL != null) {
                    mUploadCallBackAboveL!!.onReceiveValue(
                        FileChooserParams.parseResult(
                            resultCode,
                            data
                        )
                    )
                    mUploadCallBackAboveL = null
                    return
                }
            }
            clearUploadMessage()
        } else if (resultCode == RESULT_OK && requestCode == 1) {
            if (wv == null) {
                return
            }
            Log.e(TAG, "---------success-----")
            wv.evaluateJavascript("javascript:window.closeGame()"
            ) { _: String? -> }
        }
    }

    private fun clearUploadMessage() {
        if (mUploadCallBackAboveL != null) {
            mUploadCallBackAboveL!!.onReceiveValue(null)
            mUploadCallBackAboveL = null
        }
        if (mUploadCallBack != null) {
            mUploadCallBack!!.onReceiveValue(null)
            mUploadCallBack = null
        }
    }

    companion object{
        private const val TAG = "ConsentActivity"
    }

}