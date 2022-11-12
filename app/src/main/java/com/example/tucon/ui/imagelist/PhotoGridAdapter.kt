package com.example.tucon.ui.imagelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tucon.databinding.ItemViewBinding
import com.example.tucon.network.MyPicture

class PhotoGridAdapter(val viewModel: ImageListViewModel): ListAdapter<MyPicture, PhotoGridAdapter.PictureViewHolder>(
    DiffCallback
) {

    class PictureViewHolder(private var binding: ItemViewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myPicture: MyPicture) {
            binding.photo = myPicture
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(ItemViewBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picture = getItem(position)
        holder.bind(picture)
        holder.itemView.setOnClickListener {
            val action = ImageListFragmentDirections.actionImageListFragmentToSingleImageFragment(imgUrl = picture.imgSrcUrl)
            viewModel.deletefore(position)
            holder.itemView.findNavController().navigate(action)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MyPicture>() {
        override fun areItemsTheSame(oldItem: MyPicture, newItem: MyPicture): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }

        override fun areContentsTheSame(oldItem: MyPicture, newItem: MyPicture): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }

    }

}