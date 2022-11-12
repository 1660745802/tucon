package com.example.tucon.ui.favoriteimage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tucon.database.ImageFavorited
import com.example.tucon.databinding.ItemViewFavBinding

class FavoriteImageAdapter: ListAdapter<ImageFavorited, FavoriteImageAdapter.PictureViewHolder>(
    DiffCallback
) {

    class PictureViewHolder(private var binding: ItemViewFavBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageFavorited: ImageFavorited) {
            binding.favPhoto = imageFavorited
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(ItemViewFavBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picture = getItem(position)
        holder.bind(picture)
        holder.itemView.setOnClickListener {
            val action = FavoriteImageFragmentDirections.actionFavoriteImageFragmentToSingleImageFragment(imgUrl = picture.imgSrcUrl)
            holder.itemView.findNavController().navigate(action)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ImageFavorited>() {
        override fun areItemsTheSame(oldItem: ImageFavorited, newItem: ImageFavorited): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }

        override fun areContentsTheSame(oldItem: ImageFavorited, newItem: ImageFavorited): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }

    }

}