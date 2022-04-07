package com.example.tucon.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tucon.R
import com.example.tucon.data.SettingsDataStore
import com.example.tucon.databinding.FragmentImageListBinding
import com.example.tucon.viewmodel.ImageListViewModel
import kotlinx.coroutines.launch


class ImageListFragment : Fragment() {

    private val viewModel: ImageListViewModel by viewModels()
    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!

    private lateinit var SettingsDataStore: SettingsDataStore
    private var isLinearLayoutManager = false
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        recyclerView = binding.photosGrid
        chooseLayout()
        binding.photosGrid.adapter = PhotoGridAdapter()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SettingsDataStore = SettingsDataStore(requireContext())
        SettingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner, {
            isLinearLayoutManager = it
            chooseLayout()
            activity?.invalidateOptionsMenu()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return
        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                setIcon(item)
                lifecycleScope.launch {
                    SettingsDataStore.saveLayoutToPreferencesStore(isLinearLayoutManager, requireContext())
                }
                true
            }
            R.id.action_favorite -> {
                val action = ImageListFragmentDirections.actionImageListFragmentToFavoriteImageFragment()
                binding.photosGrid.findNavController().navigate(action)
                true
            }
            R.id.action_refresh -> {
                Toast.makeText(context, "refreshing!", Toast.LENGTH_SHORT).show()
                viewModel.getPhotosAgain()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}