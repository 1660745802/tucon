package com.example.tucon.ui.favoriteimage

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tucon.R
import com.example.tucon.database.FavoriteImageApplication
import com.example.tucon.databinding.FragmentFavoriteImageBinding
import com.example.tucon.utils.AlertDialogBuilder
import com.example.tucon.viewmodel.FavoriteImageViewModel
import com.example.tucon.viewmodel.FavoriteImageViewModelFactory


class FavoriteImageFragment : Fragment() {

    private var _binding: FragmentFavoriteImageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteImageViewModel by activityViewModels {
        FavoriteImageViewModelFactory(
            (activity?.application as FavoriteImageApplication).database.imageFavoritedDao()
        )
    }
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteImageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        recyclerView = binding.favoritePhotos
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.favoritePhotos.adapter = FavoriteImageAdapter()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.personal_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_all -> {
                AlertDialogBuilder.build(requireContext(), "清除全部收藏?", "是的", "不") { dialog, _ ->
                    val p = viewModel.photos.value!!
                    for (photo in p) {
                        viewModel.dislikeImage(photo.imgSrcUrl)
                    }
                    Toast.makeText(context, "清除成功", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}