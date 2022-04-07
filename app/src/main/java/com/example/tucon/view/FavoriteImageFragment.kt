package com.example.tucon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tucon.database.FavoriteImageApplication
import com.example.tucon.database.ImageFavorited
import com.example.tucon.databinding.FragmentFavoriteImageBinding
import com.example.tucon.databinding.FragmentImageListBinding
import com.example.tucon.viewmodel.FavoriteImageViewModel
import com.example.tucon.viewmodel.FavoriteImageViewModelFactory


class FavoriteImageFragment : Fragment() {

    private var _binding: FragmentFavoriteImageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteImageViewModel by viewModels()
    private val databaseViewModel: FavoriteImageViewModel by activityViewModels {
        FavoriteImageViewModelFactory(
            (activity?.application as FavoriteImageApplication).database.imageFavoritedDao()
        )
    }
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteImageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = databaseViewModel
        recyclerView = binding.favoritePhotos
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.favoritePhotos.adapter = FavoriteImageAdapter()
        // Inflate the layout for this fragment
        return binding.root
    }
}