package com.khush.gitsearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.khush.gitsearch.data.model.MainData
import com.khush.gitsearch.databinding.ItemMainBinding

class MainPagerAdapter: PagingDataAdapter<MainData, MainPagerAdapter.MainViewHolder>(
    MainDiffCallback
) {

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class MainViewHolder(private val binding: ItemMainBinding): ViewHolder(binding.root) {
        fun bind(mainData: MainData) {
            binding.name.text = mainData.name
            binding.owner.text = mainData.owner.login
            binding.description.text = mainData.description
            itemView.setOnClickListener {
                itemClickListener?.invoke(mainData)
            }
        }
    }

    private var itemClickListener: ((MainData)->Unit)? = null

    fun setOnItemClickListener(listener: (MainData)->Unit) {
        itemClickListener = listener
    }

    object MainDiffCallback: DiffUtil.ItemCallback<MainData>() {
        override fun areItemsTheSame(oldItem: MainData, newItem: MainData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MainData, newItem: MainData): Boolean {
            return oldItem == newItem
        }
    }
}