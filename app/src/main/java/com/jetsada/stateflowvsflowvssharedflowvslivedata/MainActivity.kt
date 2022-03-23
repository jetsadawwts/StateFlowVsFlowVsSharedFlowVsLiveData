package com.jetsada.stateflowvsflowvssharedflowvslivedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.jetsada.stateflowvsflowvssharedflowvslivedata.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLiveData.setOnClickListener {
            viewModel.triggerLiveData()
        }

        binding.btnStateFlow.setOnClickListener {
            viewModel.triggerStateFlow()
        }

        binding.btnFlow.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModel.triggerFlow().collectLatest {
                    binding.flowTxt.text = it.toString()
                }
            }

        }

        binding.btnShardFlow.setOnClickListener {
            viewModel.triggerSharedFlow()
        }

        subscribeToObservables()

    }

    private fun subscribeToObservables() {
        //liveData
        viewModel.liveData.observe(this) {
            binding.liveDataTxt.text = it
        }
        //StateFlow
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                binding.stateFlowTxt.text = it
//                  Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
        //SharedFlow
        lifecycleScope.launchWhenStarted {
            viewModel.staredFlow.collectLatest {
                binding.stateFlowTxt.text = it
//                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}