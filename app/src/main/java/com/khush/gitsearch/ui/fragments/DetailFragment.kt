package com.khush.gitsearch.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.khush.gitsearch.data.model.MainData
import com.khush.gitsearch.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment: Fragment() {

    lateinit var binding: FragmentDetailBinding
    private var mainData: MainData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val jsonString = arguments?.getSerializable("data") as String
        mainData = Gson().fromJson(jsonString, MainData::class.java)
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUi() {
        if(mainData == null) return
        binding.name.text = mainData?.name
        binding.owner.text = mainData?.owner?.login
        binding.repoLink.text = mainData?.repoLink
        binding.stars.text = mainData?.stars.toString() + " stars"
        binding.description.text = mainData?.description
        Glide.with(binding.root)
            .load(mainData!!.owner.avatarUrl)
            .into(binding.ownerImage)
        binding.repoLink.setOnClickListener {
            binding.webView.visibility = View.VISIBLE
            binding.webView.loadUrl(mainData!!.repoLink)
        }
    }

    companion object {
        const val TAG = "DetailFragment"
        fun newInstance(mainDataJson: String): DetailFragment {
            val args = Bundle()
            args.putSerializable("data", mainDataJson)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

}