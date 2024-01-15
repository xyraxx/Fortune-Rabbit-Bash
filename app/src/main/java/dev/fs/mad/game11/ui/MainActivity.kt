package dev.fs.mad.game11.ui

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import dev.fs.mad.game11.R
import dev.fs.mad.game11.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var bgsound: MediaPlayer



    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBars()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


            bgsound = MediaPlayer()
            try {
                val rawResourceId = R.raw.bgmusic
                val assetFileDescriptor = resources.openRawResourceFd(rawResourceId)
                bgsound.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                assetFileDescriptor.close()

                bgsound.setOnErrorListener { _, what, extra ->
                    Log.e("MediaPlayer: bgsound", "Error occurred: $what, $extra")
                    false
                }

                bgsound.prepare()
                bgsound.isLooping = true
                if (!bgsound.isPlaying) {
                    bgsound.start()
                }
            } catch (e: Exception) {
                Log.e("MediaPlayer: bgsound", "Error preparing MediaPlayer: ${e.message}")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, StartFragment())
                .commit()


    }

    override fun onPause() {
        super.onPause()
        bgsound.pause()

    }

    override fun onResume() {
        super.onResume()
        bgsound.start()
    }



    override fun onDestroy() {
        super.onDestroy()
        bgsound.release()
    }

    private fun setSystemBars() {
        this.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            WindowCompat.setDecorFitsSystemWindows(this, false)
            statusBarColor = Color.TRANSPARENT
            WindowInsetsControllerCompat(window, decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }



}