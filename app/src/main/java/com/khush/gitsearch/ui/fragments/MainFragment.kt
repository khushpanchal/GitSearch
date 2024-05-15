package com.khush.gitsearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.khush.gitsearch.R
import com.khush.gitsearch.databinding.FragmentMainBinding
import com.khush.gitsearch.ui.adapters.MainPagerAdapter
import com.khush.gitsearch.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment: Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var adapter: MainPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        collector()
        binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = MainPagerAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        adapter.setOnItemClickListener {
            val jsonStr = Gson().toJson(it)
            parentFragmentManager.beginTransaction().replace(R.id.container, DetailFragment.newInstance(jsonStr), DetailFragment.TAG)
                .addToBackStack(DetailFragment.TAG)
                .commit()
        }
        binding.rv.adapter = adapter
        binding.rv.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.etSearch.doAfterTextChanged {
            if(!it.isNullOrEmpty()) {
                mainViewModel.searchRepo(it.toString())
            }
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            )
                binding.progress.visibility = View.VISIBLE
            else {
                binding.progress.visibility = View.GONE
                binding.error.visibility = View.GONE
                // In case of error
                when {
                    loadState.refresh is LoadState.Error -> {
                        binding.error.visibility = View.VISIBLE
                        binding.error.text = (loadState.refresh as LoadState.Error).error.message
                    }

                    loadState.prepend is LoadState.Error -> {
                        loadState.prepend as LoadState.Error
                    }

                    loadState.append is LoadState.Error -> {
                        val message = (loadState.append as LoadState.Error).error.message
                        Toast.makeText(
                            this@MainFragment.requireContext(),
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun collector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.mainItem.collect {
                    binding.rv.visibility = View.VISIBLE
                    adapter.submitData(it)
                }
            }
        }
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance(): MainFragment {
            val args = Bundle()
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

}