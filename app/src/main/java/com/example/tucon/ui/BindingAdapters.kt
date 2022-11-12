package com.example.tucon.ui

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tucon.R
import com.example.tucon.database.ImageFavorited
import com.example.tucon.network.MyPicture
import com.example.tucon.ui.favoriteimage.FavoriteImageAdapter
import com.example.tucon.ui.imagelist.PhotoGridAdapter


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MyPicture>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}
@BindingAdapter("listImage")
fun bindFavRecyclerView(recyclerView: RecyclerView, data: List<ImageFavorited>?) {
    val adapter = recyclerView.adapter as FavoriteImageAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_img)
            error(R.drawable.ic_broken_image)
        }
    }
}

class BindingAdapters {

}