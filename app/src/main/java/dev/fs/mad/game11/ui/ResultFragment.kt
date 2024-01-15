package dev.fs.mad.game11.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import dev.fs.mad.game11.App
import dev.fs.mad.game11.R
import dev.fs.mad.game11.databinding.ResultFragmentBinding
import dev.fs.mad.game11.presentation.ResultViewModel
import javax.inject.Inject

class ResultFragment : Fragment() {
    @Inject
    lateinit var viewModel: ResultViewModel

    private lateinit var binding: ResultFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().applicationContext as App).appComponent.inject(this)
        binding = ResultFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val score = getScore() ?: return

        binding.score.text = score.toString()

        binding.bestScore.text = viewModel.getBestScore().toString()

        binding.menuButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, StartFragment())
                .commit()
        }

        binding.playAgainButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, GameFragment())
                .addToBackStack("GameFragment")
                .commit()
        }
    }

    private fun getScore(): Int? =
        arguments?.getInt(SCORE)

    companion object {
        fun newInstance(score: Int): ResultFragment =
            ResultFragment().apply {
                arguments = bundleOf(SCORE to score)
            }

        private const val SCORE = "SCORE"
    }

}