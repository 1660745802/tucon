package com.example.tucon.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil.load
import com.example.tucon.R
import com.example.tucon.database.FavoriteImageApplication
import com.example.tucon.databinding.FragmentSingleImageBinding
import com.example.tucon.viewmodel.FavoriteImageViewModel
import com.example.tucon.viewmodel.FavoriteImageViewModelFactory
import com.example.tucon.viewmodel.ImageListViewModel
import com.example.tucon.viewmodel.SingleImageViewModel


class SingleImageFragment : Fragment() {

    private var binding: FragmentSingleImageBinding? = null

    private val sharedViewModel: SingleImageViewModel by viewModels()

    private val databaseViewModel: FavoriteImageViewModel by activityViewModels {
        FavoriteImageViewModelFactory(
            (activity?.application as FavoriteImageApplication).database.imageFavoritedDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            viewModel.setUrl(it.getString().toString())
            sharedViewModel.setUrl(it.getString("imgUrl").toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleImageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            singleImageFragment = this@SingleImageFragment
            singleImage.load(sharedViewModel.imgUrl.value!!.toUri().buildUpon().scheme("https").build()) {
                placeholder(R.drawable.loading_img)
                error(R.drawable.ic_broken_image)
            }
        }
    }
    fun likeImage() {
        databaseViewModel.addNewImage(sharedViewModel.imgUrl.value.toString())
        Toast.makeText(context, "like success!", Toast.LENGTH_SHORT).show()

    }
    fun dislikeImage() {
        databaseViewModel.dislikeImage(sharedViewModel.imgUrl.value.toString())
        Toast.makeText(context, "dislike success!", Toast.LENGTH_SHORT).show()
    }

}