package dev.fs.mad.game11.ui

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.fragment.app.Fragment
import dev.fs.mad.game11.App
import dev.fs.mad.game11.R
import dev.fs.mad.game11.databinding.GameFragmentBinding
import dev.fs.mad.game11.presentation.GameViewModel
import javax.inject.Inject
import kotlin.random.Random


class GameFragment : Fragment() {
    @Inject
    lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding
    private lateinit var timer: CountDownTimer
    private var score = 0
    private lateinit var tableLayout : TableLayout
    private val arrayImages: Array<Array<FrameLayout?>> = Array(3) { arrayOfNulls(3) }
    private lateinit var mpHit: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity().applicationContext as App).appComponent.inject(this)
        binding = GameFragmentBinding.inflate(inflater, container, false)


        mpHit = MediaPlayer()
        try {
            val rawResourceId = R.raw.hitsoundmp3
            val assetFileDescriptor = resources.openRawResourceFd(rawResourceId)
            mpHit.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )
            assetFileDescriptor.close()

            mpHit.setOnErrorListener { _, what, extra ->
                Log.e("MediaPlayer: hitsound", "Error occurred: $what, $extra")
                false
            }
            mpHit.setVolume(2.0f, 2.0f)
            mpHit.prepare()

        } catch (e: Exception) {
            Log.e("MediaPlayer: hitsound catch", "Error preparing MediaPlayer: ${e.message}")
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tableLayout = binding.tableLayout
        binding.score.text = getString(R.string.score, score.toString())
        binding.timer.text = getString(R.string.time, String.format("%02d", 30))

        timer = object : CountDownTimer(30000, 500) {

            override fun onTick(millisUntilFinished: Long) {
                setGameView(millisUntilFinished)
            }

            override fun onFinish() {
                viewModel.setBestScore(score)
                val fragment = ResultFragment.newInstance(score)
                parentFragmentManager.popBackStack()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit()
            }
        }.start()
    }

    override fun onPause() {
        timer.cancel()
        super.onPause()
    }

    private fun setGameView(millisUntilFinished: Long) {
        val randomRow = Random.nextInt(HOLE_ROWS)
        val randomColumn = Random.nextInt(HOLE_COLUMNS)

        binding.timer.text =
            getString(R.string.time, String.format("%02d", millisUntilFinished / 1000))


        for (i in 0 until HOLE_ROWS) {
            for (j in 0 until HOLE_COLUMNS) {
                val frameLayout = FrameLayout(requireContext())
                val imageView = ImageView(requireContext())
                val params = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
                )

                params.setMargins(10, 10, 10, 10)
                frameLayout.layoutParams = params

                if (i == randomRow && j == randomColumn) {
                    imageView.setImageResource(R.drawable.rabbit)
                    imageView.setOnClickListener {
                        if (mpHit.isPlaying) {
                            if (mpHit.isPlaying) {
                                mpHit.stop()
                                mpHit.prepare()
                            }
                        }
                        mpHit.start()
                        score++
                        binding.score.text = getString(R.string.score, score.toString())
                        imageView.setImageResource(R.drawable.whack)
                        imageView.isEnabled = false
                    }
                    imageView.shakeView()
                }

                // First, add the imageView (rabbit)
                frameLayout.addView(imageView)

                // Then, add the background hole
                val holeImageView = ImageView(requireContext())
                holeImageView.setImageResource(R.drawable.hole)
                frameLayout.addView(holeImageView)

                arrayImages[i][j] = frameLayout
            }
        }

        tableLayout.removeAllViewsInLayout()

        for (i in 0 until HOLE_ROWS) {
            val tableRow = TableRow(requireContext())
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f
            )
            tableRow.gravity = Gravity.CENTER
            for (j in 0 until HOLE_COLUMNS) {
                tableRow.addView(arrayImages[i][j], j)
            }

            tableLayout.addView(tableRow, i)
        }
    }

    private fun View.shakeView() {
        ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 30F, 0F, 30F).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 500
            start()
        }
    }

    companion object {
        const val HOLE_ROWS = 3
        const val HOLE_COLUMNS = 3
    }

    override fun onDestroy() {
        super.onDestroy()
        mpHit.release()
    }

}