package com.onfido.techtask.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.onfido.techtask.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var disposable: Disposable? = null
    private val viewModel: MainViewModel by viewModels()
    private val adapter = FactAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.list.adapter = adapter
        bindState()
        bindListeners()
    }

    private fun bindState() {
        disposable = viewModel.observeState().subscribe { state ->
            if (state is UiState.Content) adapter.submitList(state.facts)
            if (state is UiState.Error) binding.textError.text = state.message
            binding.progressBar.isVisible = state is UiState.Loading
            binding.layoutInput.isVisible = state is UiState.Content
            binding.textError.isVisible = state is UiState.Error
            binding.btnRetry.isVisible = state is UiState.Error
        }
    }

    private fun bindListeners() {
        binding.submit.setOnClickListener {
            viewModel.onSearchClicked(binding.input.text.toString())
        }

        binding.btnRetry.setOnClickListener {
            viewModel.onRetryClicked()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
