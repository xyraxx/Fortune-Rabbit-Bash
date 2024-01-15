package dev.fs.mad.game11.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.fs.mad.game11.App
import dev.fs.mad.game11.R
import dev.fs.mad.game11.controller.VolleyController
import dev.fs.mad.game11.databinding.StartFragmentBinding
import dev.fs.mad.game11.presentation.StartViewModel
import javax.inject.Inject

class StartFragment : Fragment() {
    @Inject
    lateinit var viewModel: StartViewModel

    private lateinit var binding: StartFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().applicationContext as App).appComponent.inject(this)
        binding = StartFragmentBinding.inflate(inflater, container, false)
        return binding.root

        binding.policyView.loadUrl(VolleyController.policyMain)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bestScoreValue.text = viewModel.getBestScore().toString()

        binding.playButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, GameFragment())
                .addToBackStack("GameFragment")
                .commit()
        }

        binding.policyBtn.setOnClickListener {
            val intent = Intent(requireContext(), ConsentActivity::class.java)
            intent.putExtra("url", VolleyController.policyURL)
            startActivity(intent)
        }
    }

}