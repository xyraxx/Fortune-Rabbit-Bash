package dev.fs.mad.game11.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import dev.fs.mad.game11.R
import dev.fs.mad.game11.controller.VolleyController


@SuppressLint("CustomSplashScreen")
class LoadingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(1024, 1024)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_loading)


        val volleyController = VolleyController(this)
        volleyController.getPolicy()
    }



}